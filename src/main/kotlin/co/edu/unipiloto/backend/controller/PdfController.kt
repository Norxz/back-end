package co.edu.unipiloto.backend.controller

import co.edu.unipiloto.backend.service.SolicitudService
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class PdfController(
    private val solicitudService: SolicitudService
) {

    @GetMapping("/api/guia/pdf")
    fun downloadGuide(
        @RequestParam id: Long
    ): ResponseEntity<ByteArray> {

        val pdfBytes = solicitudService.generarPdfDeSolicitud(id)

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=guia_$id.pdf")
            .contentType(MediaType.APPLICATION_PDF)
            .body(pdfBytes)
    }
}
