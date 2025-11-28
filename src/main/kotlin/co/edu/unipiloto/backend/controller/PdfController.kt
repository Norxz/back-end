package co.edu.unipiloto.backend.controller

import co.edu.unipiloto.backend.service.SolicitudService
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 * Controlador para la descarga de PDFs de guías de solicitudes.
 *
 * Permite generar y descargar en formato PDF la información de una solicitud específica.
 */
@RestController
class PdfController(
    private val solicitudService: SolicitudService
) {

    /**
     * Endpoint para descargar la guía de una solicitud en formato PDF.
     *
     * @param id ID de la solicitud
     * @return [ResponseEntity] con el PDF en bytes si se encontró la solicitud,
     *         o HTTP 404 Not Found si no existe o ocurre un error
     */
    @GetMapping("/api/v1/guia/download/{id}")
    fun downloadGuideV1(@PathVariable id: Long): ResponseEntity<ByteArray> {
        return try {
            val pdfBytes = solicitudService.generarPdfDeSolicitud(id)
            ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=guia_$id.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes)
        } catch (e: Exception) {
            ResponseEntity.notFound().build()
        }
    }
}
