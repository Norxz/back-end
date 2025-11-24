package co.edu.unipiloto.backend.service

import co.edu.unipiloto.backend.dto.ClienteRequest
import co.edu.unipiloto.backend.dto.SolicitudRequest
import co.edu.unipiloto.backend.exception.ResourceNotFoundException
import co.edu.unipiloto.backend.model.*
import co.edu.unipiloto.backend.repository.*
import co.edu.unipiloto.backend.utils.PdfGenerator
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class SolicitudService(
    private val solicitudRepository: SolicitudRepository,
    private val userRepository: UserRepository,
    private val clienteRepository: ClienteRepository,
    private val guiaRepository: GuiaRepository,
    private val direccionRepository: DireccionRepository,
    private val sucursalRepository: SucursalRepository

) {

    /**
     * Crea una nueva solicitud, incluyendo la generación de la Guía y la Dirección asociada.
     * Esta operación es transaccional.
     */
    @Transactional
    fun crearSolicitud(request: SolicitudRequest): Solicitud {

        // 1. Verificar cliente
        val client: User = userRepository.findById(request.clientId)
            .orElseThrow { ResourceNotFoundException("Cliente con ID ${request.clientId} no encontrado.") }

        // 2. Remitente
        val remitente = obtenerOCrearCliente(request.remitente)
        val receptor = obtenerOCrearCliente(request.receptor)

        // 2. Dirección
        val nuevaDireccion = Direccion(
            direccionCompleta = request.direccion.direccionCompleta,
            ciudad = request.direccion.ciudad,
            latitud = request.direccion.latitud,
            longitud = request.direccion.longitud,
            pisoApto = request.direccion.pisoApto,
            notasEntrega = request.direccion.notasEntrega
        )

        // 3. Guía
        val trackingCode = UUID.randomUUID().toString().substring(0, 10).uppercase()
        val nuevaGuia = Guia(
            numeroGuia = trackingCode.substring(0, 8),
            trackingNumber = trackingCode
        )

        val paquete = Paquete(
            peso = request.paquete.peso,
            alto = request.paquete.alto,
            ancho = request.paquete.ancho,
            largo = request.paquete.largo,
            contenido = request.paquete.contenido,
        )

        val sucursal = sucursalRepository.findById(request.sucursalId)
            .orElseThrow { ResourceNotFoundException("Sucursal con ID ${request.sucursalId} no encontrada") }

        // ⭐⭐ 4. Crear la Solicitud COMPLETA (con todos los campos)
        val nuevaSolicitud = Solicitud(
            client = client,
            remitente = remitente,
            receptor = receptor,
            sucursal = sucursal,
            direccion = nuevaDireccion,
            paquete = paquete,
            guia = nuevaGuia,
            fechaRecoleccion = request.fechaRecoleccion,
            franjaHoraria = request.franjaHoraria,
            estado = "PENDIENTE"
        )


        return solicitudRepository.save(nuevaSolicitud)
    }

    fun generarPdfDeSolicitud(id: Long): ByteArray {
        val solicitud = solicitudRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Solicitud con ID $id no encontrada.") }

        return PdfGenerator.createGuidePdf(
            id = solicitud.id!!,
            remitente = solicitud.remitente.nombre,
            receptor = solicitud.receptor.nombre,
            numeroGuia = solicitud.guia.numeroGuia,
            trackingNumber = solicitud.guia.trackingNumber,
            direccion = solicitud.direccion.direccionCompleta,
            fechaRecoleccion = solicitud.fechaRecoleccion,
            estado = solicitud.estado
        )
    }


    private fun obtenerOCrearCliente(clienteRequest: ClienteRequest): Cliente {
        // Intenta buscar por tipoId + numeroId
        val existente = clienteRepository.findByTipoIdAndNumeroId(
            clienteRequest.tipoId ?: "",
            clienteRequest.numeroId ?: ""
        )
        return existente ?: clienteRepository.save(
            Cliente(
                nombre = clienteRequest.nombre,
                tipoId = clienteRequest.tipoId,
                numeroId = clienteRequest.numeroId,
                telefono = clienteRequest.telefono,
                codigoPais = clienteRequest.codigoPais
            )
        )
    }

    fun getSolicitudesByClientId(clientId: Long): List<Solicitud> {
        // Llama al método de Spring Data JPA que definiste en SolicitudRepository
        return solicitudRepository.findAllByClientId(clientId)
    }


    /**
     * Actualiza el estado de una solicitud en la base de datos.
     */
    @Transactional
    fun updateEstado(solicitudId: Long, newState: String): Solicitud {
        val solicitud = solicitudRepository.findById(solicitudId)
            .orElseThrow { ResourceNotFoundException("Solicitud con ID $solicitudId no encontrada.") }

        // Cambia el estado (Ahora es posible porque 'estado' es 'var')
        solicitud.estado = newState

        // Guarda el cambio en la base de datos
        return solicitudRepository.save(solicitud)
    }

    fun findOrCreateDireccion(dir: Direccion): Direccion {
        val existing = direccionRepository.findByDireccionCompletaAndCiudad(
            dir.direccionCompleta,
            dir.ciudad
        )
        return existing ?: direccionRepository.save(dir)
    }
}
