package co.edu.unipiloto.backend.dto

import co.edu.unipiloto.backend.model.Guia
import co.edu.unipiloto.backend.model.Contacto

data class GuiaResponse(
    val numeroGuia: String,
    val trackingNumber: String,
    val fechaCreacion: String, // Simplificado a String
    val remitente: ContactoResponse,
    val destinatario: ContactoResponse
) {
    constructor(guia: Guia) : this(
        numeroGuia = guia.numeroGuia,
        trackingNumber = guia.trackingNumber,
        fechaCreacion = guia.fechaCreacion.toString(),
        remitente = ContactoResponse(guia.remitente),
        destinatario = ContactoResponse(guia.destinatario)
    )
}
