package co.edu.unipiloto.backend.controller

import co.edu.unipiloto.backend.dto.SolicitudRequest
import co.edu.unipiloto.backend.dto.SolicitudResponse
import co.edu.unipiloto.backend.service.SolicitudService
import co.edu.unipiloto.backend.service.AsignacionService
import co.edu.unipiloto.backend.exception.ResourceNotFoundException
import co.edu.unipiloto.backend.model.Solicitud
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 * 📦 Controlador REST principal para manejar todas las operaciones relacionadas con las **Solicitudes** de envío.
 *
 * Expone la API para:
 * 1. Crear nuevas solicitudes.
 * 2. Consultar solicitudes por cliente, tracking number, o sucursal/estado.
 * 3. Actualizar el estado de una solicitud.
 * 4. Asignar gestores y conductores.
 * 5. Consultar rutas asignadas a un conductor (Dashboard del conductor).
 *
 * Mapea a la ruta base: `/api/v1/solicitudes`
 *
 * @property solicitudService Servicio con la lógica de negocio para las solicitudes.
 * @property asignacionService Servicio con la lógica de negocio para la asignación de personal.
 */
@RestController
@RequestMapping("/api/v1/solicitudes")
class SolicitudController(
    private val solicitudService: SolicitudService,
    private val asignacionService: AsignacionService
) {

// --- CREACIÓN ---

    /**
     * 📝 Crea una nueva solicitud de envío en el sistema.
     *
     * Mapea a: `POST /api/v1/solicitudes`
     *
     * @param request DTO ([SolicitudRequest]) con todos los detalles de la solicitud (remitente, receptor, paquete, etc.).
     * @return [ResponseEntity] con:
     * - HTTP **201 CREATED** y la solicitud creada ([SolicitudResponse]).
     * - HTTP **404 NOT FOUND** si una entidad relacionada (cliente, sucursal) no existe.
     * - HTTP **500 INTERNAL_SERVER_ERROR** en caso de error inesperado durante la creación.
     */
    @PostMapping
    fun crearSolicitud(@RequestBody request: SolicitudRequest): ResponseEntity<*> {
        return try {
            val nuevaSolicitud = solicitudService.crearSolicitud(request)
            val response = SolicitudResponse(nuevaSolicitud)
            ResponseEntity(response, HttpStatus.CREATED)
        } catch (e: ResourceNotFoundException) {
            // Maneja el caso de que IDs relacionados (como el cliente o la sucursal) no existan.
            ResponseEntity(e.message, HttpStatus.NOT_FOUND)
        } catch (e: Exception) {
            ResponseEntity("Error al crear la solicitud: ${e.message}", HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

// --- CONSULTA GENERAL ---

    /**
     * 📋 Obtiene todas las solicitudes de envío registradas en el sistema.
     *
     * Mapea a: `GET /api/v1/solicitudes`
     * @return Lista de [SolicitudResponse] (Útil principalmente para roles de administración).
     */
    @GetMapping
    fun listarTodasLasSolicitudes(): ResponseEntity<List<SolicitudResponse>> {
        val solicitudes: List<Solicitud> = solicitudService.listarTodas()
        val responseList = solicitudes.map { SolicitudResponse(it) }
        return ResponseEntity(responseList, HttpStatus.OK)
    }

    /**
     * 👤 Obtiene todas las solicitudes creadas por un cliente específico.
     *
     * Mapea a: `GET /api/v1/solicitudes/client/{clientId}`
     *
     * @param clientId ID del cliente ([User]) creador de las solicitudes.
     * @return Lista de [SolicitudResponse] (Historial de solicitudes del cliente).
     */
    @GetMapping("/client/{clientId}")
    fun getSolicitudesByClient(@PathVariable clientId: Long): ResponseEntity<List<SolicitudResponse>> {
        val solicitudes: List<Solicitud> = solicitudService.getSolicitudesByClientId(clientId)
        val responseList = solicitudes.map { SolicitudResponse(it) }
        return ResponseEntity(responseList, HttpStatus.OK)
    }

    /**
     * 🔎 Obtiene una solicitud por su número de rastreo (`trackingNumber`) de la guía.
     *
     * Mapea a: `GET /api/v1/solicitudes/tracking/{trackingNumber}`
     *
     * @param trackingNumber El código de rastreo único asociado a la guía.
     * @return [ResponseEntity] con la solicitud ([SolicitudResponse]) o un error.
     * - HTTP **404 NOT FOUND** si el tracking number no corresponde a ninguna solicitud.
     */
    @GetMapping("/tracking/{trackingNumber}")
    fun getSolicitudByTrackingNumber(@PathVariable trackingNumber: String): ResponseEntity<*> {
        return try {
            val solicitud = solicitudService.getSolicitudByTrackingNumber(trackingNumber)
            val response = SolicitudResponse(solicitud)
            ResponseEntity(response, HttpStatus.OK)
        } catch (e: ResourceNotFoundException) {
            ResponseEntity(e.message, HttpStatus.NOT_FOUND)
        } catch (e: Exception) {
            ResponseEntity("Error interno al buscar la solicitud: ${e.message}", HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    /**
     * 📄 Genera un PDF de la guía de la solicitud.
     *
     * Mapea a: `GET /api/v1/solicitudes/{id}/pdf`
     *
     * @param id ID de la solicitud.
     * @return [ResponseEntity] con el PDF en bytes si tiene éxito, o HTTP 404/500 si falla.
     * - Nota: Este endpoint puede ser redundante si ya existe el endpoint `/api/v1/guia/download/{id}` en `PdfController`.
     */
    @GetMapping("/{id}/pdf")
    fun generarPdf(@PathVariable id: Long): ResponseEntity<ByteArray> {
        return try {
            val pdf = solicitudService.generarPdfDeSolicitud(id)
            // Retorna 200 OK con el contenido binario.
            ResponseEntity(pdf, HttpStatus.OK)
        } catch (e: ResourceNotFoundException) {
            ResponseEntity(null, HttpStatus.NOT_FOUND)
        } catch (e: Exception) {
            ResponseEntity(null, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    /**
     * 🚚 Obtiene las rutas (solicitudes en curso) asignadas a un conductor específico.
     * Este endpoint es crucial para el Dashboard de la aplicación Android del conductor.
     *
     * Mapea a: `GET /api/v1/solicitudes/driver/{driverId}/routes`
     *
     * @param driverId ID del conductor/recolector ([User]).
     * @return Lista de [SolicitudResponse] filtradas por conductor y estado activo (no finalizado/cancelado).
     */
    @GetMapping("/driver/{driverId}/routes")
    fun getRoutesByDriverId(@PathVariable driverId: Long): ResponseEntity<List<SolicitudResponse>> {
        return try {
            val solicitudes: List<Solicitud> = solicitudService.getRoutesByDriverId(driverId)
            val responseList = solicitudes.map { SolicitudResponse(it) }
            // Retorna 200 OK. La lista vacía indica que no hay rutas asignadas actualmente.
            ResponseEntity(responseList, HttpStatus.OK)
        } catch (e: Exception) {
            // Manejo de error interno.
            ResponseEntity(emptyList(), HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

// --- CONSULTA POR SUCURSAL ---

    /**
     * 🏭 Obtiene las solicitudes que están en estado **PENDIENTE** para una sucursal específica.
     *
     * Mapea a: `GET /api/v1/solicitudes/branch/{sucursalId}`
     *
     * @param sucursalId ID de la sucursal.
     * @return Lista de [SolicitudResponse] pendientes de asignación o procesamiento en la sucursal.
     */
    @GetMapping("/branch/{sucursalId}")
    fun getSolicitudesPendingBySucursal(@PathVariable sucursalId: Long): ResponseEntity<List<SolicitudResponse>> {
        val solicitudes: List<Solicitud> = solicitudService.getPendingBySucursalId(sucursalId)
        val responseList = solicitudes.map { SolicitudResponse(it) }
        return ResponseEntity(responseList, HttpStatus.OK)
    }

    /**
     * ⚙️ Obtiene las solicitudes que ya han sido **ASIGNADAS** a personal (gestor/conductor) dentro de una sucursal específica.
     *
     * Mapea a: `GET /api/v1/solicitudes/branch/{sucursalId}/assigned`
     *
     * @param sucursalId ID de la sucursal.
     * @return Lista de [SolicitudResponse] en estado asignado.
     */
    @GetMapping("/branch/{sucursalId}/assigned")
    fun getSolicitudesAssignedBySucursal(@PathVariable sucursalId: Long): ResponseEntity<List<SolicitudResponse>> {
        val solicitudes: List<Solicitud> = solicitudService.getAssignedBySucursalId(sucursalId)
        val responseList = solicitudes.map { SolicitudResponse(it) }
        return ResponseEntity(responseList, HttpStatus.OK)
    }

// --- ACTUALIZACIÓN Y ASIGNACIÓN ---

    /**
     * ➡️ Actualiza el estado de una solicitud específica.
     * Usado por gestores o automáticamente por el sistema/conductor.
     *
     * Mapea a: `PUT /api/v1/solicitudes/{solicitudId}/estado`
     * Cuerpo esperado: `{"estado": "NUEVO_ESTADO_EN_MAYUSCULAS"}`
     *
     * @param solicitudId ID de la solicitud a actualizar.
     * @param estadoUpdate Mapa que contiene la clave "estado" con el nuevo valor.
     * @return HTTP **204 NO CONTENT** si la actualización es exitosa.
     * - HTTP **400 BAD REQUEST** si falta el campo 'estado'.
     * - HTTP **404 NOT FOUND** si la solicitud no existe.
     */
    @PutMapping("/{solicitudId}/estado")
    fun updateEstado(
        @PathVariable solicitudId: Long,
        @RequestBody estadoUpdate: Map<String, String>
    ): ResponseEntity<*> {
        val newState = estadoUpdate["estado"]

        if (newState.isNullOrEmpty()) {
            return ResponseEntity("Falta el campo 'estado' en la petición.", HttpStatus.BAD_REQUEST)
        }

        return try {
            // Llama al servicio, convirtiendo el estado a mayúsculas (ENUM_STRING).
            solicitudService.updateEstado(solicitudId, newState.uppercase())
            ResponseEntity<Void>(HttpStatus.NO_CONTENT) // Éxito sin contenido de respuesta.
        } catch (e: ResourceNotFoundException) {
            ResponseEntity("Solicitud $solicitudId no encontrada.", HttpStatus.NOT_FOUND)
        } catch (e: Exception) {
            // Puede capturar IllegalArgumentException si el estado enviado no es válido.
            ResponseEntity("Error al actualizar el estado: ${e.message}", HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    /**
     * 👥 Asigna un gestor a una solicitud específica.
     *
     * Mapea a: `POST /api/v1/solicitudes/{solicitudId}/asignar-gestor/{gestorId}`
     *
     * @param solicitudId ID de la solicitud.
     * @param gestorId ID del gestor ([User]) a asignar.
     * @return [ResponseEntity] con la solicitud actualizada ([SolicitudResponse]).
     */
    @PostMapping("/{solicitudId}/asignar-gestor/{gestorId}")
    fun asignarGestor(
        @PathVariable solicitudId: Long,
        @PathVariable gestorId: Long
    ): ResponseEntity<*> {
        return try {
            val solicitud = asignacionService.asignarGestorASolicitud(solicitudId, gestorId)
            ResponseEntity(SolicitudResponse(solicitud), HttpStatus.OK)
        } catch (e: ResourceNotFoundException) {
            ResponseEntity(e.message, HttpStatus.NOT_FOUND)
        } catch (e: Exception) {
            ResponseEntity("Error al asignar gestor: ${e.message}", HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    /**
     * 🚛 Asigna un conductor a una solicitud (Método que utiliza RequestParam).
     *
     * Mapea a: `POST /api/v1/solicitudes/{solicitudId}/asignar-conductor?gestorId={id}&conductorId={id}`
     *
     * @param solicitudId ID de la solicitud.
     * @param gestorId ID del gestor que realiza la asignación.
     * @param conductorId ID del conductor a asignar.
     * @return [ResponseEntity] con la solicitud actualizada ([SolicitudResponse]).
     */
    @PostMapping("/{solicitudId}/asignar-conductor")
    fun asignarConductor(
        @PathVariable solicitudId: Long,
        @RequestParam gestorId: Long,
        @RequestParam conductorId: Long
    ): ResponseEntity<*> {
        return try {
            val solicitud = asignacionService.asignarConductorASolicitud(solicitudId, gestorId, conductorId)
            ResponseEntity(SolicitudResponse(solicitud), HttpStatus.OK)
        } catch (e: ResourceNotFoundException) {
            ResponseEntity(e.message, HttpStatus.NOT_FOUND)
        } catch (e: Exception) {
            ResponseEntity("Error al asignar conductor: ${e.message}", HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    /**
     * 📱 Asigna o reasigna un conductor/recolector a una solicitud, utilizando el formato JSON
     * que espera la aplicación Android (moderno/reestructurado).
     *
     * Mapea a: `PUT /api/v1/solicitudes/{solicitudId}/assign-driver`
     * Cuerpo esperado: `{"recolectorId": "3"}`
     *
     * @param solicitudId ID de la solicitud.
     * @param body Mapa que contiene el `recolectorId` (ID del conductor).
     * @return [ResponseEntity] con la solicitud actualizada ([SolicitudResponse]) o error.
     */
    @PutMapping("/{solicitudId}/assign-driver")
    fun assignDriver(
        @PathVariable solicitudId: Long,
        @RequestBody body: Map<String, String>
    ): ResponseEntity<*> {
        val recolectorIdString = body["recolectorId"]

        if (recolectorIdString.isNullOrEmpty()) {
            return ResponseEntity("Falta el campo 'recolectorId' en la petición.", HttpStatus.BAD_REQUEST)
        }

        // 1. Intenta convertir el String ID a Long.
        val recolectorId = recolectorIdString.toLongOrNull()
        if (recolectorId == null) {
            return ResponseEntity("El campo 'recolectorId' debe ser un número válido.", HttpStatus.BAD_REQUEST)
        }

        return try {
            // Llama a la lógica de asignación simplificada del servicio.
            val solicitudActualizada = asignacionService.asignarRecolectorASolicitud(solicitudId, recolectorId)

            ResponseEntity(SolicitudResponse(solicitudActualizada), HttpStatus.OK)
        } catch (e: ResourceNotFoundException) {
            ResponseEntity(e.message, HttpStatus.NOT_FOUND)
        } catch (e: Exception) {
            ResponseEntity("Error al asignar conductor: ${e.message}", HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
}