package co.edu.unipiloto.backend.utils

import com.lowagie.text.*
import com.lowagie.text.pdf.PdfWriter
import java.io.ByteArrayOutputStream

object PdfGenerator {

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
        val outputStream = ByteArrayOutputStream()
        val document = Document()

        PdfWriter.getInstance(document, outputStream)
        document.open()

        document.add(Paragraph("GUÍA DE ENVÍO - SOLICITUD #$id"))
        document.add(Paragraph("\nNúmero de Guía: $numeroGuia"))
        document.add(Paragraph("Tracking: $trackingNumber"))
        document.add(Paragraph("Estado: $estado"))
        document.add(Paragraph("\nRemitente: $remitente"))
        document.add(Paragraph("Receptor: $receptor"))
        document.add(Paragraph("Dirección de entrega: $direccion"))
        document.add(Paragraph("Fecha de Recolección: $fechaRecoleccion"))

        document.add(Paragraph("\n---\nGENERADO AUTOMÁTICAMENTE"))

        document.close()
        return outputStream.toByteArray()
    }
}
