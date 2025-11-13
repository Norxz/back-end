package co.edu.unipiloto.backend.dto

import co.edu.unipiloto.backend.model.Solicitud

data class SolicitudResponse(
    val id: Long,
    val clientId: Long, // Solo el ID del cliente
    val estado: String,
    val fechaRecoleccion: String,
    val franjaHoraria: String,
    val direccionCompleta: String, // Campo plano para la UI
    val guia: GuiaResponse // Usamos el DTO simplificado
) {
    constructor(solicitud: Solicitud) : this(
        id = solicitud.id ?: 0,
        clientId = solicitud.client.id ?: 0,
        estado = solicitud.estado,
        fechaRecoleccion = solicitud.fechaRecoleccion,
        franjaHoraria = solicitud.franjaHoraria,
        direccionCompleta = solicitud.direccion.direccionCompleta,
        guia = GuiaResponse(solicitud.guia)
    )
}