package co.edu.unipiloto.backend.service

import co.edu.unipiloto.backend.dto.SolicitudRequest
import co.edu.unipiloto.backend.exception.ResourceNotFoundException
import co.edu.unipiloto.backend.model.Direccion
import co.edu.unipiloto.backend.model.Guia
import co.edu.unipiloto.backend.model.Solicitud
import co.edu.unipiloto.backend.model.User
import co.edu.unipiloto.backend.repository.DireccionRepository
import co.edu.unipiloto.backend.repository.GuiaRepository
import co.edu.unipiloto.backend.repository.SolicitudRepository
import co.edu.unipiloto.backend.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class SolicitudService(
    private val solicitudRepository: SolicitudRepository,
    private val userRepository: UserRepository,
    private val guiaRepository: GuiaRepository
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

        // 2. Dirección
        val nuevaDireccion = Direccion(
            direccionCompleta = request.direccionCompleta,
            ciudad = request.ciudad,
            latitud = request.latitud,
            longitud = request.longitud,
            pisoApto = request.pisoApto,
            notasEntrega = request.notasEntrega
        )

        // 3. Guía
        val trackingCode = UUID.randomUUID().toString().substring(0, 10).uppercase()
        val nuevaGuia = Guia(
            numeroGuia = trackingCode.substring(0, 8),
            trackingNumber = trackingCode,
            volumenM3 = null
        )

        // ⭐⭐ 4. Crear la Solicitud COMPLETA (con todos los campos)
        val nuevaSolicitud = Solicitud(
            client = client,

            // Remitente
            remitenteNombre = request.remitenteNombre,
            remitenteTipoId = request.remitenteTipoId,
            remitenteNumeroId = request.remitenteNumeroId,
            remitenteTelefono = request.remitenteTelefono,
            remitenteCodigoPais = request.remitenteCodigoPais,

            // Receptor
            receptorNombre = request.receptorNombre,
            receptorTipoId = request.receptorTipoId,
            receptorNumeroId = request.receptorNumeroId,
            receptorTelefono = request.receptorTelefono,
            receptorCodigoPais = request.receptorCodigoPais,

            // Paquete
            alto = request.alto,
            ancho = request.ancho,
            largo = request.largo,
            contenido = request.contenido,

            // Relacionados
            direccion = nuevaDireccion,
            guia = nuevaGuia,

            // Logística
            fechaRecoleccion = request.fechaRecoleccion,
            franjaHoraria = request.franjaHoraria,
            estado = "PENDIENTE",

            // Peso y precio
            pesoKg = request.pesoKg,
            precio = request.precio
        )

        return solicitudRepository.save(nuevaSolicitud)
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
}
