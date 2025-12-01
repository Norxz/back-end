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
import java.time.Instant

/**
 * 📨 Servicio encargado de la lógica de negocio central para la gestión de [Solicitud]es de envío.
 * Coordina la creación y gestión de múltiples entidades relacionadas (Guía, Dirección, Paquete, Cliente, Sucursal)
 * en una sola operación transaccional.
 *
 * @property solicitudRepository Repositorio para la entidad Solicitud.
 * @property userRepository Repositorio para la entidad User (clientes, conductores, etc.).
 * @property clienteRepository Repositorio para la entidad Cliente (remitentes, receptores).
 * @property guiaRepository Repositorio para la entidad Guia.
 * @property direccionRepository Repositorio para la entidad Direccion.
 * @property sucursalRepository Repositorio para la entidad Sucursal.
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
     * Esta operación es transaccional para asegurar la atomicidad en la creación de todas las entidades
     * dependientes (Cliente, Dirección, Guía, Paquete).
     *
     * Flujo de Creación:
     * 1. Verifica la existencia de [User] (client) y [Sucursal].
     * 2. Obtiene o crea las entidades [Cliente] para Remitente y Receptor.
     * 3. Obtiene o crea las entidades [Direccion] para Recolección (si existe) y Entrega.
     * 4. Crea [Paquete] y [Guia] con un código de rastreo único.
     * 5. Crea la [Solicitud] con el estado inicial **PENDIENTE** y la persiste.
     *
     * @param request El DTO con todos los datos necesarios para la Solicitud.
     * @return La entidad [Solicitud] recién creada y persistida.
     * @throws ResourceNotFoundException Si el usuario creador (`clientId`) o la [Sucursal] no existen.
     */
    @Transactional
    fun crearSolicitud(request: SolicitudRequest): Solicitud {

        // 1. Verificar existencia de entidades principales
        val client: User = userRepository.findById(request.clientId)
            .orElseThrow { ResourceNotFoundException("Cliente con ID ${request.clientId} no encontrado.") }

        val sucursal = sucursalRepository.findById(request.sucursalId)
            .orElseThrow { ResourceNotFoundException("Sucursal con ID ${request.sucursalId} no encontrada") }

        // 2. Obtener o crear Remitente/Receptor
        val remitente = obtenerOCrearCliente(request.remitente)
        val receptor = obtenerOCrearCliente(request.receptor)

        // 3. Procesar Direcciones (Usando findOrCreateDireccion para evitar duplicados)

        // Dirección de ENTREGA (Obligatoria)
        val direccionEntregaEntity = request.direccionEntrega.let { dto ->
            findOrCreateDireccion(
                Direccion(
                    direccionCompleta = dto.direccionCompleta,
                    ciudad = dto.ciudad,
                    latitud = dto.latitud,
                    longitud = dto.longitud,
                    pisoApto = dto.pisoApto,
                    notasEntrega = dto.notasEntrega
                )
            )
        }

        // Dirección de RECOLECCIÓN (Opcional)
        // Solo procesamos si el DTO de recolección fue enviado (no es null)
        val direccionRecoleccionEntity: Direccion? = request.direccionRecoleccion?.let { dto ->
            findOrCreateDireccion(
                Direccion(
                    direccionCompleta = dto.direccionCompleta,
                    ciudad = dto.ciudad,
                    latitud = dto.latitud,
                    longitud = dto.longitud,
                    pisoApto = dto.pisoApto,
                    notasEntrega = dto.notasEntrega
                )
            )
        }

        // 4. Guía y Paquete (Creación de entidades transaccionales)
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

        // 5. Crear la Solicitud COMPLETA
        val nuevaSolicitud = Solicitud(
            client = client,
            remitente = remitente,
            receptor = receptor,
            sucursal = sucursal,
            direccionRecoleccion = direccionRecoleccionEntity,
            direccionEntrega = direccionEntregaEntity,
            paquete = paquete,
            guia = nuevaGuia,
            fechaRecoleccion = request.fechaRecoleccion,
            franjaHoraria = request.franjaHoraria,
            estado = EstadoSolicitud.PENDIENTE
        )

        return solicitudRepository.save(nuevaSolicitud)
    }

    /**
     * 👥 Lógica auxiliar para buscar un [Cliente] por su documento o crearlo si no existe.
     * Utiliza la combinación de [tipoId] y [numeroId] para la búsqueda.
     *
     * @param clienteRequest DTO con los datos del Cliente (Remitente o Receptor).
     * @return La entidad [Cliente] existente o recién creada.
     * @throws IllegalArgumentException Si el número de documento ([numeroId]) es nulo en el DTO.
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
     * 📍 Busca una [Direccion] existente por su dirección completa y ciudad. Si no existe, la crea y la persiste.
     * Este método asegura que no se creen entradas duplicadas en la tabla de direcciones.
     *
     * @param dir La entidad [Direccion] a buscar/crear (con datos temporales, sin ID).
     * @return La entidad [Direccion] existente o recién creada.
     */
    fun findOrCreateDireccion(dir: Direccion): Direccion {
        val existing = direccionRepository.findByDireccionCompletaAndCiudad(
            dir.direccionCompleta,
            dir.ciudad
        )
        // Solo guardamos si no existe, si existe devolvemos el existente (evita duplicados)
        return existing ?: direccionRepository.save(dir)
    }

    // --- Funciones de Consulta y Actualización ---

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
     * 🔎 Recupera una [Solicitud] de envío utilizando el número de rastreo (trackingNumber) de su guía.
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

    /**
     * 📋 Obtiene una lista de todas las solicitudes registradas en el sistema.
     * Esto fue añadido para soportar la ruta GET /api/v1/solicitudes/all.
     *
     * @return Una [List] de todas las entidades [Solicitud].
     */
    fun listarTodas(): List<Solicitud> {
        // Asumiendo que tienes una SolicitudRepository inyectada
        return solicitudRepository.findAll()
    }

    /**
     * Busca todas las solicitudes con estado PENDIENTE para una sucursal específica.
     * @param sucursalId ID de la sucursal.
     * @return Lista de Solicitudes.
     */
    fun getPendingBySucursalId(sucursalId: Long): List<Solicitud> {
        // 🛑 CORRECCIÓN: Pasar el valor del ENUM (EstadoSolicitud.PENDIENTE) en lugar del String literal ("PENDIENTE").
        return solicitudRepository.findBySucursalIdAndEstado(
            sucursalId,
            EstadoSolicitud.PENDIENTE // <--- Tipo Enum
        )
    }

    /**
     * Busca todas las solicitudes con estado ASIGNADA o en tránsito para una sucursal específica.
     * @param sucursalId ID de la sucursal.
     * @return Lista de Solicitudes.
     */
    fun getAssignedBySucursalId(sucursalId: Long): List<Solicitud> {
        // Para simplificar, asumiremos que "ASIGNADA" es el estado correcto.
        // 🛑 CORRECCIÓN: Pasar el valor del ENUM (EstadoSolicitud.ASIGNADA) en lugar del String literal ("ASIGNADA").
        return solicitudRepository.findBySucursalIdAndEstado(
            sucursalId,
            EstadoSolicitud.ASIGNADA // <--- Tipo Enum
        )
    }
}