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

// --- CONSULTA POR SUCURSAL (Faltantes) ---

    /**
     * 🟢 **CORRECCIÓN:** Obtiene las solicitudes **PENDIENTES** de una sucursal específica.
     *
     * Mapea a: GET /api/v1/solicitudes/branch/{sucursalId} ⬅️ **Ruta que estaba dando 404**
     *
     * @param sucursalId ID de la sucursal.
     * @return Lista de [SolicitudResponse] en estado PENDIENTE.
     */
    @GetMapping("/branch/{sucursalId}")
    fun getSolicitudesPendingBySucursal(@PathVariable sucursalId: Long): ResponseEntity<List<SolicitudResponse>> {
        // Asumiendo que esta función existe en SolicitudService y filtra por PENDIENTE
        val solicitudes: List<Solicitud> = solicitudService.getPendingBySucursalId(sucursalId)
        val responseList = solicitudes.map { SolicitudResponse(it) }
        return ResponseEntity(responseList, HttpStatus.OK)
    }

    /**
     * 🟢 **ADICIONAL:** Obtiene las solicitudes **ASIGNADAS** de una sucursal específica.
     *
     * Mapea a: GET /api/v1/solicitudes/branch/{sucursalId}/assigned
     *
     * @param sucursalId ID de la sucursal.
     * @return Lista de [SolicitudResponse] en estado ASIGNADA o en tránsito.
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
     *
     * Nota: La lógica de servicio (AsignacionService) fue corregida anteriormente para cambiar el estado a "ASIGNADA".
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
     * Asigna un conductor a una solicitud.
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
}