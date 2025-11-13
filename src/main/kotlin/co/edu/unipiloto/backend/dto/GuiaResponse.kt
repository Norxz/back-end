package co.edu.unipiloto.backend.dto

import co.edu.unipiloto.backend.model.Guia

data class GuiaResponse(
    val numeroGuia: String,
    val trackingNumber: String,
    val fechaCreacion: String // Simplificado a String
) {
    constructor(guia: Guia) : this(
        numeroGuia = guia.numeroGuia,
        trackingNumber = guia.trackingNumber,
        fechaCreacion = guia.fechaCreacion.toString()
    )
}