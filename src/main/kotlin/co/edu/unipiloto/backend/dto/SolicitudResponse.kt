package co.edu.unipiloto.backend.dto

import co.edu.unipiloto.backend.model.Solicitud

/**
 * DTO utilizado para enviar la información de una solicitud al cliente (Android/Postman).
 *
 * @property id ID de la solicitud
 * @property clientId ID del cliente que realizó la solicitud
 * @property estado Estado actual de la solicitud (por ejemplo, PENDIENTE, CANCELADA, ENTREGADA)
 * @property fechaRecoleccion Fecha programada para la recolección
 * @property franjaHoraria Franja horaria de la recolección
 * @property direccionCompleta Dirección completa del punto de entrega o recolección, para mostrar en la UI
 * @property guia Información de la guía asociada a la solicitud (DTO GuiaResponse)
 */
data class SolicitudResponse(
    val id: Long,
    val clientId: Long, // Solo el ID del cliente
    val estado: String,
    val fechaRecoleccion: String,
    val franjaHoraria: String,
    val direccionCompleta: String, // Campo plano para la UI
    val guia: GuiaResponse // Usamos el DTO simplificado
) {
    /**
     * Constructor que mapea una entidad [Solicitud] al DTO de respuesta.
     */
    constructor(solicitud: Solicitud) : this(
        id = solicitud.id ?: 0,
        clientId = solicitud.client.id ?: 0,
        estado = solicitud.estado.name,
        fechaRecoleccion = solicitud.fechaRecoleccion,
        franjaHoraria = solicitud.franjaHoraria,
        direccionCompleta = solicitud.direccion.direccionCompleta,
        guia = GuiaResponse(solicitud.guia)
    )
}
