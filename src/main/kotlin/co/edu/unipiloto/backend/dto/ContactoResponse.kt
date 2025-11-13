package co.edu.unipiloto.backend.dto

import co.edu.unipiloto.backend.model.Contacto

data class ContactoResponse(
    val id: Long?,
    val nombreCompleto: String,
    val tipoIdentificacion: String,
    val numeroIdentificacion: String,
    val telefono: String
) {
    constructor(contacto: Contacto) : this(
        id = contacto.id,
        nombreCompleto = contacto.nombreCompleto,
        tipoIdentificacion = contacto.tipoIdentificacion,
        numeroIdentificacion = contacto.numeroIdentificacion,
        telefono = contacto.telefono
    )
}
