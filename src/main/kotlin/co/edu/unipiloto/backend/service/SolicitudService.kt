package co.edu.unipiloto.backend.service

import co.edu.unipiloto.backend.dto.ClienteRequest
import co.edu.unipiloto.backend.dto.SolicitudRequest
import co.edu.unipiloto.backend.exception.ResourceNotFoundException
import co.edu.unipiloto.backend.model.*
import co.edu.unipiloto.backend.model.enums.EstadoSolicitud
import co.edu.unipiloto.backend.repository.*
import co.edu.unipiloto.backend.utils.PdfGenerator
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

/**
 * 📨 Servicio encargado de la lógica de negocio central para la gestión de [Solicitud]es de envío.
 * Coordina la creación de múltiples entidades relacionadas (Guía, Dirección, Paquete) en una sola operación transaccional.
 */
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
     * 📝 Crea una nueva solicitud de envío a partir de los datos del DTO [SolicitudRequest].
     * Esta operación es transaccional para asegurar que todas las entidades relacionadas
     * (Solicitud, Guía, Dirección, Paquete) se creen o actualicen correctamente.
     *
     * Flujo de Creación:
     * 1. Verifica la existencia del usuario que crea la solicitud (`clientId`).
     * 2. Obtiene o crea las entidades [Cliente] para Remitente y Receptor.
     * 3. Crea las entidades [Direccion], [Paquete], y [Guia] con sus identificadores únicos.
     * 4. Asigna la [Sucursal] de gestión.
     * 5. Crea la [Solicitud] con el estado inicial **PENDIENTE** y la guarda.
     *
     * @param request El DTO con todos los datos necesarios para la Solicitud.
     * @return La entidad [Solicitud] recién creada.
     * @throws ResourceNotFoundException Si el cliente o la sucursal no existen.
     */
    @Transactional
    fun crearSolicitud(request: SolicitudRequest): Solicitud {

        // 1. Verificar cliente y obtener Remitente/Receptor (código existente)
        val client: User = userRepository.findById(request.clientId)
            .orElseThrow { ResourceNotFoundException("Cliente con ID ${request.clientId} no encontrado.") }

        val remitente = obtenerOCrearCliente(request.remitente)
        val receptor = obtenerOCrearCliente(request.receptor)

        // 2. Dirección de ENTREGA (Destino)
        // Usamos el DTO de la solicitud (asumiendo que request.direccion es la de entrega)
        val nuevaDireccionEntrega = Direccion(
            direccionCompleta = request.direccionEntrega.direccionCompleta,
            ciudad = request.direccionEntrega.ciudad,
            latitud = request.direccionEntrega.latitud,
            longitud = request.direccionEntrega.longitud,
            pisoApto = request.direccionEntrega.pisoApto,
            notasEntrega = request.direccionEntrega.notasEntrega
        )
        // Nota: El DTO de SolicitudRequest necesita un campo para la Dirección de Recolección
        // si esta debe ser creada, de lo contrario, la dejamos null.
        val nuevaDireccionRecoleccion: Direccion? = null // Dejamos como null por ahora

        // 3. Guía y Paquete (código existente)
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

        // 4. Sucursal (código existente)
        val sucursal = sucursalRepository.findById(request.sucursalId)
            .orElseThrow { ResourceNotFoundException("Sucursal con ID ${request.sucursalId} no encontrada") }

        // 5. Crear la Solicitud COMPLETA
        val nuevaSolicitud = Solicitud(
            client = client,
            remitente = remitente,
            receptor = receptor,
            sucursal = sucursal,
            // 🚨 Asignamos explícitamente los dos nuevos campos de dirección
            direccionRecoleccion = nuevaDireccionRecoleccion, // Asignado a null
            direccionEntrega = nuevaDireccionEntrega, // Asignado al valor obligatorio
            paquete = paquete,
            guia = nuevaGuia,
            fechaRecoleccion = request.fechaRecoleccion,
            franjaHoraria = request.franjaHoraria,
            estado = EstadoSolicitud.PENDIENTE
        )

        return solicitudRepository.save(nuevaSolicitud)
    }

    /**
     * 📄 Genera un documento PDF para la solicitud de envío, utilizando la clase auxiliar [PdfGenerator].
     *
     * @param id El ID de la Solicitud.
     * @return Un array de Bytes que representa el archivo PDF generado.
     * @throws ResourceNotFoundException si la Solicitud no es encontrada.
     */
    fun generarPdfDeSolicitud(id: Long): ByteArray {
        val solicitud = solicitudRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Solicitud con ID $id no encontrada.") }

        // Los datos necesarios se extraen de la entidad Solicitud y sus relaciones (remitente, guia, direccion, etc.)
        return PdfGenerator.createGuidePdf(
            id = solicitud.id!!,
            remitente = solicitud.remitente.nombre,
            receptor = solicitud.receptor.nombre,
            numeroGuia = solicitud.guia.numeroGuia,
            trackingNumber = solicitud.guia.trackingNumber,
            direccion = solicitud.direccionEntrega.direccionCompleta,
            fechaRecoleccion = solicitud.fechaRecoleccion,
            estado = solicitud.estado.name
        )
    }

    /**
     * 👥 Lógica auxiliar para buscar un [Cliente] por su documento o crearlo si no existe.
     *
     * @param clienteRequest DTO con los datos del Cliente (Remitente o Receptor).
     * @return La entidad [Cliente] existente o recién creada.
     * @throws IllegalArgumentException Si el número de documento es nulo.
     */
    private fun obtenerOCrearCliente(clienteRequest: ClienteRequest): Cliente {
        val numeroId = clienteRequest.numeroId ?: throw IllegalArgumentException("Número de documento es obligatorio")

        // Intenta buscar el cliente por la combinación de tipo y número de ID
        val existente = clienteRepository.findByTipoIdAndNumeroId(
            clienteRequest.tipoId ?: "",
            numeroId
        )

        // Si existe, lo retorna; si no existe, lo crea y lo guarda.
        return existente ?: clienteRepository.save(
            Cliente(
                nombre = clienteRequest.nombre,
                tipoId = clienteRequest.tipoId,
                numeroId = numeroId,
                telefono = clienteRequest.telefono,
                codigoPais = clienteRequest.codigoPais
            )
        )
    }

    /**
     * 🔍 Recupera todas las solicitudes creadas por un usuario (cliente) específico del sistema.
     *
     * @param clientId El ID del usuario creador (entidad User).
     * @return Una lista de entidades [Solicitud].
     */
    fun getSolicitudesByClientId(clientId: Long): List<Solicitud> {
        return solicitudRepository.findAllByClientId(clientId)
    }

    /**
     * 🔄 Actualiza el [EstadoSolicitud] de una solicitud existente.
     *
     * @param solicitudId El ID de la Solicitud a actualizar.
     * @param newState La cadena de texto del nuevo estado (ej: "EN_TRANSITO").
     * @return La entidad [Solicitud] con el estado actualizado.
     * @throws ResourceNotFoundException si la Solicitud no existe.
     * @throws IllegalArgumentException si la cadena de estado no corresponde a un [EstadoSolicitud] válido.
     */
    @Transactional
    fun updateEstado(solicitudId: Long, newState: String): Solicitud {
        val solicitud = solicitudRepository.findById(solicitudId)
            .orElseThrow { ResourceNotFoundException("Solicitud con ID $solicitudId no encontrada.") }

        // Valida que la cadena de estado sea un valor válido del Enum EstadoSolicitud
        val estadoEnum = try {
            EstadoSolicitud.valueOf(newState.uppercase())
        } catch (e: IllegalArgumentException) {
            throw IllegalArgumentException("Estado inválido: $newState. Debe ser uno de los valores definidos en EstadoSolicitud.")
        }

        solicitud.estado = estadoEnum
        return solicitudRepository.save(solicitud)
    }

    /**
     * 📍 Busca una [Direccion] existente por su dirección completa y ciudad. Si no existe, la crea.
     *
     * @param dir La entidad [Direccion] a buscar/crear.
     * @return La entidad [Direccion] existente o recién creada.
     */
    fun findOrCreateDireccion(dir: Direccion): Direccion {
        val existing = direccionRepository.findByDireccionCompletaAndCiudad(
            dir.direccionCompleta,
            dir.ciudad
        )
        return existing ?: direccionRepository.save(dir)
    }

    /**
     * 🔎 Recupera una [Solicitud] de envío utilizando el número de rastreo (trackingNumber) de su guía.
     *
     * Utiliza el método del repositorio que navega a través de la relación de la Guía.
     *
     * @param trackingNumber El código de guía único.
     * @return La entidad [Solicitud] encontrada.
     * @throws ResourceNotFoundException si no se encuentra la solicitud con el trackingNumber dado.
     */
    @Transactional
    fun getSolicitudByTrackingNumber(trackingNumber: String): Solicitud {
        return solicitudRepository.findByGuia_TrackingNumber(trackingNumber)
            .orElseThrow {
                // Si el Optional está vacío, lanza la excepción 404
                ResourceNotFoundException("Solicitud con Tracking Number '$trackingNumber' no encontrada.")
            }
    }
}