package co.edu.unipiloto.backend.controller

import co.edu.unipiloto.backend.dto.SolicitudRequest
import co.edu.unipiloto.backend.dto.SolicitudResponse
import co.edu.unipiloto.backend.service.SolicitudService
import co.edu.unipiloto.backend.exception.ResourceNotFoundException
import co.edu.unipiloto.backend.model.Solicitud
import co.edu.unipiloto.backend.model.Cliente
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/solicitudes")
class SolicitudController(private val solicitudService: SolicitudService) {

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

    @GetMapping("/client/{clientId}")
    // 🏆 Cambia a devolver List<SolicitudResponse> (necesitarías crear este DTO en el backend)
    fun getSolicitudesByClient(@PathVariable clientId: Long): ResponseEntity<List<SolicitudResponse>> {
        val solicitudes: List<Solicitud> = solicitudService.getSolicitudesByClientId(clientId)

        // 🏆 Mapear la lista de Entidades a la lista de DTOs de Respuesta
        val responseList = solicitudes.map { SolicitudResponse(it) } // Necesitas definir SolicitudResponse en el backend

        return ResponseEntity(responseList, HttpStatus.OK)
    }

    /**
     * Endpoint para actualizar el estado de una solicitud (usado para Cancelar y Confirmar Entrega).
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
}