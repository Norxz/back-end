package co.edu.unipiloto.backend.utils

import com.lowagie.text.*
import com.lowagie.text.pdf.PdfWriter
import java.io.ByteArrayOutputStream
import java.time.format.DateTimeFormatter
import java.util.*

/**
 * 📄 Objeto utilitario (`object`) encargado de la generación de documentos PDF.
 *
 * Utiliza la librería **OpenPDF** (antiguamente iText 2.1.7) para crear documentos
 * ligeros de guías de envío, destinados a la impresión o visualización.
 */
object PdfGenerator {

    /**
     * ## 📝 Generar Guía de Envío en PDF
     *
     * Crea un documento PDF básico que contiene la información esencial de una solicitud de envío
     * ([Solicitud] en la capa de negocio), incluyendo códigos de rastreo, remitente, receptor
     * y detalles de la entrega.
     *
     * @param id El ID interno de la Solicitud.
     * @param remitente Nombre completo del remitente.
     * @param receptor Nombre completo del receptor.
     * @param numeroGuia El número corto de la Guía de Envío.
     * @param trackingNumber El código de rastreo (tracking) único.
     * @param direccion La dirección completa de entrega.
     * @param fechaRecoleccion La fecha programada para la recolección.
     * @param estado El estado actual de la Solicitud (e.g., "PENDIENTE", "ASIGNADA").
     * @return Un [ByteArray] que representa el contenido binario del archivo PDF generado.
     */
    fun createGuidePdf(
        id: Long,
        remitente: String,
        receptor: String,
        numeroGuia: String,
        trackingNumber: String,
        direccion: String,
        fechaRecoleccion: String,
        estado: String
    ): ByteArray {
        // Objeto para capturar el contenido binario del PDF
        val outputStream = ByteArrayOutputStream()
        // Creación del documento PDF (clase principal)
        val document = Document()

        try {
            // Asocia el documento al flujo de salida y abre el documento para escritura
            PdfWriter.getInstance(document, outputStream)
            document.open()

            // --- Título y encabezado ---
            // Título principal con ID de la solicitud
            document.add(Paragraph("GUÍA DE ENVÍO - SOLICITUD #$id", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12f)))
            document.add(Paragraph("-------------------------------------------------------------------------------------"))

            // --- Códigos de seguimiento ---
            document.add(Paragraph("\n**Información de Seguimiento**", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10f)))
            document.add(Paragraph("Número de Guía: $numeroGuia"))
            document.add(Paragraph("Tracking: $trackingNumber"))
            document.add(Paragraph("Estado: $estado"))

            // --- Detalles de Clientes y Direcciones ---
            document.add(Paragraph("\n**Detalles del Envío**", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10f)))
            document.add(Paragraph("Remitente: $remitente"))
            document.add(Paragraph("Receptor: $receptor"))
            document.add(Paragraph("Dirección de entrega: $direccion"))
            document.add(Paragraph("Fecha de Recolección: $fechaRecoleccion"))

            // --- Pie de página ---
            document.add(Paragraph("\n---\nGENERADO AUTOMÁTICAMENTE", FontFactory.getFont(FontFactory.HELVETICA, 8f)))

        } catch (e: Exception) {
            // Manejo básico de excepciones en la generación del PDF
            e.printStackTrace()
        } finally {
            // Siempre se debe cerrar el documento para finalizar la escritura del PDF
            if (document.isOpen) {
                document.close()
            }
        }

        // Retorna el PDF como un array de bytes
        return outputStream.toByteArray()
    }
}