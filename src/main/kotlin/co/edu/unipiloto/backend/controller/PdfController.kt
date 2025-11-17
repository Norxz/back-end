package co.edu.unipiloto.backend.controller

import co.edu.unipiloto.backend.utils.PdfGenerator
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class PdfController {

    @GetMapping("/api/guia/pdf")
    fun downloadGuide(
        @RequestParam id: Long = 1L // Puedes pasar dinámicamente el ID si necesitas
    ): ResponseEntity<ByteArray> {

        val pdfBytes = PdfGenerator.createGuidePdf(
            id = id,
            remitente = "Juan Pérez",
            receptor = "María Gómez",
            numeroGuia = "123456",
            trackingNumber = "TRACKD123",
            direccion = "Calle 123 #45-67",
            fechaRecoleccion = "2025-01-15",
            estado = "EN RUTA"
        )

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=guia_$id.pdf")
            .contentType(MediaType.APPLICATION_PDF)
            .body(pdfBytes)
    }
}
