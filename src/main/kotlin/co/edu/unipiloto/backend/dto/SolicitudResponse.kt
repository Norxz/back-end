// co.edu.unipiloto.backend.dto.SolicitudResponse.kt

package co.edu.unipiloto.backend.dto

import co.edu.unipiloto.backend.model.Solicitud

/**
 * 📊 DTO de respuesta simplificado para Solicitudes.
 * Se construye a partir de la entidad Solicitud para la API.
 */
data class SolicitudResponse(
    val id: Long,
    val clientId: Long,
    val estado: String,
    val fechaRecoleccion: String,
    val franjaHoraria: String,
    val direccionCompleta: String,
    val guia: GuiaResponse,
    val recolectorId: Long? = null,
    val recolectorName: String? = null,

    val createdAt: String? = null

) {
    /**
     * Constructor que mapea la entidad [Solicitud] al DTO de respuesta.
     */
    constructor(solicitud: Solicitud) : this(
        id = solicitud.id!!,
        clientId = solicitud.client.id!!,
        estado = solicitud.estado.name,
        fechaRecoleccion = solicitud.fechaRecoleccion,
        franjaHoraria = solicitud.franjaHoraria,

        // Asumiendo que esta propiedad extrae la dirección de entrega de la Solicitud
        direccionCompleta = solicitud.direccionEntrega.direccionCompleta,

        // Mapeo anidado
        guia = GuiaResponse(solicitud.guia),

        // 🛑 CORRECCIÓN CLAVE: Usamos '?.id' y '?.nombre'
        // Si el conductor es null, asigna null a recolectorId y recolectorName.
        recolectorId = solicitud.conductor?.id,
        recolectorName = solicitud.conductor?.fullName,

        // Formateo de fecha para el cliente, si se requiere
        createdAt = solicitud.createdAt.toString()
    )
}