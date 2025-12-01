package co.edu.unipiloto.backend.controller

import co.edu.unipiloto.backend.service.SolicitudService
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 * 📄 Controlador REST dedicado a la generación y descarga de documentos PDF.
 *
 * Su principal función es tomar una solicitud existente y convertir sus datos
 * en un archivo binario (PDF) que el cliente puede descargar como guía de envío.
 *
 * @property solicitudService Servicio que contiene la lógica para generar el PDF a partir de la solicitud.
 */
@RestController
class PdfController(
    private val solicitudService: SolicitudService
) {

    /**
     * 📥 Endpoint para descargar la guía de una solicitud específica en formato PDF.
     *
     * Mapea a: `GET /api/v1/guia/download/{id}`
     *
     * @param id ID ([Long]) de la solicitud para la cual se debe generar la guía.
     * @return [ResponseEntity] con:
     * - HTTP **200 OK** si se encuentra la solicitud, con el cuerpo binario del PDF y los headers de descarga.
     * - HTTP **404 Not Found** si la solicitud no existe o si el servicio falla (capturado por el bloque catch).
     */
    @GetMapping("/api/v1/guia/download/{id}")
    fun downloadGuideV1(@PathVariable id: Long): ResponseEntity<ByteArray> {
        return try {
            // 1. Obtiene los bytes del PDF llamando a la lógica de negocio del servicio.
            val pdfBytes = solicitudService.generarPdfDeSolicitud(id)

            // 2. Construye la respuesta exitosa (HTTP 200 OK).
            ResponseEntity.ok()
                // Header que fuerza al navegador/cliente a descargar el archivo en lugar de mostrarlo en línea.
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=guia_$id.pdf")
                // Define el tipo de contenido como archivo PDF.
                .contentType(MediaType.APPLICATION_PDF)
                // Incluye el contenido binario (ByteArray) del PDF.
                .body(pdfBytes)
        } catch (e: Exception) {
            // Manejo de errores: Si la solicitud no existe (ResourceNotFoundException) o hay otro fallo.
            // Retorna un HTTP 404 para indicar que el recurso no está disponible o no se pudo generar.
            ResponseEntity.notFound().build()
        }
    }
}