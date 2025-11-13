package co.edu.unipiloto.backend.controller

import co.edu.unipiloto.backend.dto.SolicitudRequest
import co.edu.unipiloto.backend.dto.SolicitudResponse
import co.edu.unipiloto.backend.service.SolicitudService
import co.edu.unipiloto.backend.exception.ResourceNotFoundException
import co.edu.unipiloto.backend.model.Solicitud
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
            // Retorna la solicitud creada con código 201 Created
            ResponseEntity(nuevaSolicitud, HttpStatus.CREATED)
        } catch (e: ResourceNotFoundException) {
            // Error 404 si el cliente ID no existe
            ResponseEntity(e.message, HttpStatus.NOT_FOUND)
        } catch (e: Exception) {
            // Error 500 para cualquier otro fallo transaccional
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

}