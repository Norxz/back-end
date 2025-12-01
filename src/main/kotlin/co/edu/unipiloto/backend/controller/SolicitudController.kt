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
 * Controlador para manejar todas las operaciones relacionadas con las solicitudes.
 *
 * Permite crear solicitudes, consultar por cliente, actualizar estado,
 * generar PDF y asignar gestores o conductores.
 */
@RestController
@RequestMapping("/api/v1/solicitudes")
class SolicitudController(
    private val solicitudService: SolicitudService,
    private val asignacionService: AsignacionService
) {

// --- CREACIÓN ---

    /**
     * Crea una nueva solicitud.
     */
    @PostMapping
    fun crearSolicitud(@RequestBody request: SolicitudRequest): ResponseEntity<*> {
        return try {
            val nuevaSolicitud = solicitudService.crearSolicitud(request)
            val response = SolicitudResponse(nuevaSolicitud)
            ResponseEntity(response, HttpStatus.CREATED)
        } catch (e: ResourceNotFoundException) {
            ResponseEntity(e.message, HttpStatus.NOT_FOUND)
        } catch (e: Exception) {
            ResponseEntity("Error al crear la solicitud: ${e.message}", HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

// --- CONSULTA GENERAL ---

    /**
     * Obtiene todas las solicitudes del sistema (útil para administración o depuración).
     *
     * Mapea a: GET /api/v1/solicitudes
     * @return Lista de [SolicitudResponse]
     */
    @GetMapping
    fun listarTodasLasSolicitudes(): ResponseEntity<List<SolicitudResponse>> {
        // Asumiendo que existe un método 'listarTodas()' en SolicitudService
        val solicitudes: List<Solicitud> = solicitudService.listarTodas()
        val responseList = solicitudes.map { SolicitudResponse(it) }
        return ResponseEntity(responseList, HttpStatus.OK)
    }

    /**
     * Obtiene todas las solicitudes de un cliente específico.
     *
     * @param clientId ID del cliente
     * @return Lista de [SolicitudResponse]
     */
    @GetMapping("/client/{clientId}")
    fun getSolicitudesByClient(@PathVariable clientId: Long): ResponseEntity<List<SolicitudResponse>> {
        val solicitudes: List<Solicitud> = solicitudService.getSolicitudesByClientId(clientId)
        val responseList = solicitudes.map { SolicitudResponse(it) }
        return ResponseEntity(responseList, HttpStatus.OK)
    }

    /**
     * Obtiene una solicitud por su número de rastreo (trackingNumber) de la guía.
     *
     * Mapea a: GET /api/v1/solicitudes/tracking/{trackingNumber}
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
     * Genera un PDF de la solicitud.
     */
    @GetMapping("/{id}/pdf")
    fun generarPdf(@PathVariable id: Long): ResponseEntity<ByteArray> {
        return try {
            val pdf = solicitudService.generarPdfDeSolicitud(id)
            ResponseEntity(pdf, HttpStatus.OK)
        } catch (e: ResourceNotFoundException) {
            ResponseEntity(null, HttpStatus.NOT_FOUND)
        } catch (e: Exception) {
            ResponseEntity(null, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

// --- CONSULTA POR SUCURSAL ---

    /**
     * Obtiene las solicitudes **PENDIENTES** de una sucursal específica.
     */
    @GetMapping("/branch/{sucursalId}")
    fun getSolicitudesPendingBySucursal(@PathVariable sucursalId: Long): ResponseEntity<List<SolicitudResponse>> {
        // Asumiendo que esta función existe en SolicitudService y filtra por PENDIENTE
        val solicitudes: List<Solicitud> = solicitudService.getPendingBySucursalId(sucursalId)
        val responseList = solicitudes.map { SolicitudResponse(it) }
        return ResponseEntity(responseList, HttpStatus.OK)
    }

    /**
     * Obtiene las solicitudes **ASIGNADAS** de una sucursal específica.
     */
    @GetMapping("/branch/{sucursalId}/assigned")
    fun getSolicitudesAssignedBySucursal(@PathVariable sucursalId: Long): ResponseEntity<List<SolicitudResponse>> {
        // Asumiendo que esta función existe en SolicitudService y filtra por ASIGNADA
        val solicitudes: List<Solicitud> = solicitudService.getAssignedBySucursalId(sucursalId)
        val responseList = solicitudes.map { SolicitudResponse(it) }
        return ResponseEntity(responseList, HttpStatus.OK)
    }

// --- ACTUALIZACIÓN Y ASIGNACIÓN ---

    /**
     * Actualiza el estado de una solicitud.
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
            solicitudService.updateEstado(solicitudId, newState.uppercase())
            ResponseEntity<Void>(HttpStatus.NO_CONTENT)
        } catch (e: ResourceNotFoundException) {
            ResponseEntity("Solicitud $solicitudId no encontrada.", HttpStatus.NOT_FOUND)
        } catch (e: Exception) {
            ResponseEntity("Error al actualizar el estado: ${e.message}", HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    /**
     * Asigna un gestor a una solicitud.
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
     * Asigna un conductor a una solicitud (Usando RequestParam - Método antiguo).
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

    // 🏆 NUEVA FUNCIÓN PARA CORREGIR EL ERROR 404 DE ANDROID
    /**
     * Asigna o reasigna un conductor/recolector a una solicitud,
     * utilizando la ruta y el formato de cuerpo JSON que espera la app Android.
     *
     * Mapea a: PUT /api/v1/solicitudes/{solicitudId}/assign-driver
     * Cuerpo esperado: {"recolectorId": "3"}
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

        val recolectorId = recolectorIdString.toLongOrNull()
        if (recolectorId == null) {
            return ResponseEntity("El campo 'recolectorId' debe ser un número válido.", HttpStatus.BAD_REQUEST)
        }

        return try {
            // Asumiendo que el gestorId es manejado por la capa de servicio o es implícito,
            // usaremos la función de asignación de conductor, simplificando la lógica para
            // manejar la reasignación/asignación por ID de recolector.
            val solicitudActualizada = asignacionService.asignarRecolectorASolicitud(solicitudId, recolectorId)

            ResponseEntity(SolicitudResponse(solicitudActualizada), HttpStatus.OK)
        } catch (e: ResourceNotFoundException) {
            ResponseEntity(e.message, HttpStatus.NOT_FOUND)
        } catch (e: Exception) {
            ResponseEntity("Error al asignar conductor: ${e.message}", HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    /**
     * Obtiene todas las solicitudes de envío ASIGNADAS a un conductor específico.
     *
     * Mapea a: GET /api/v1/solicitudes/driver/{driverId}/routes
     * ESTE ES EL ENDPOINT QUE BUSCA LA APP ANDROID.
     *
     * @param driverId ID del conductor/recolector.
     * @return Lista de [SolicitudResponse] (Generalmente filtradas por estado 'ASIGNADA' o 'EN_RUTA').
     */
    @GetMapping("/driver/{driverId}/routes")
    fun getRoutesByDriverId(@PathVariable driverId: Long): ResponseEntity<List<SolicitudResponse>> {
        return try {
            // Asumiendo que existe un método en SolicitudService que trae las solicitudes
            // que están en curso (ASIGNADA, EN_RUTA, EN_DISTRIBUCION, etc.) para ese conductor.
            val solicitudes: List<Solicitud> = solicitudService.getRoutesByDriverId(driverId)
            val responseList = solicitudes.map { SolicitudResponse(it) }

            // Retorna 200 OK, incluso si la lista está vacía (cero rutas), no un 404.
            ResponseEntity(responseList, HttpStatus.OK)
        } catch (e: Exception) {
            ResponseEntity(emptyList(), HttpStatus.INTERNAL_SERVER_ERROR) // Manejo de errores
        }
    }
}