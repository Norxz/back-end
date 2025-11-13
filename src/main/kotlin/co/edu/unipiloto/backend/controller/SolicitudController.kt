package co.edu.unipiloto.backend.controller

import co.edu.unipiloto.backend.dto.ContactoDTO
import co.edu.unipiloto.backend.dto.DireccionDTO
import co.edu.unipiloto.backend.dto.SolicitudRequest
import co.edu.unipiloto.backend.model.Solicitud
import co.edu.unipiloto.backend.service.SolicitudService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/solicitudes")
class SolicitudController(
    private val solicitudService: SolicitudService
) {

    @PostMapping
    fun crearSolicitud(@RequestBody request: SolicitudRequest): ResponseEntity<Solicitud> {
        val solicitud = solicitudService.crearSolicitud(request)
        return ResponseEntity.ok(solicitud)
    }

    @GetMapping
    fun obtenerTodas(): ResponseEntity<List<Solicitud>> {
        return ResponseEntity.ok(solicitudService.obtenerTodas())
    }

    @GetMapping("/{id}")
    fun obtenerPorId(@PathVariable id: Long): ResponseEntity<Solicitud?> {
        val solicitud = solicitudService.obtenerPorId(id)
        return if (solicitud != null) ResponseEntity.ok(solicitud)
        else ResponseEntity.notFound().build()
    }

    @DeleteMapping("/{id}")
    fun eliminar(@PathVariable id: Long): ResponseEntity<Void> {
        solicitudService.eliminarSolicitud(id)
        return ResponseEntity.noContent().build()
    }
}
