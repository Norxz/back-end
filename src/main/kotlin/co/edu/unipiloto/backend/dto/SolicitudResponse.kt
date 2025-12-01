// co.edu.unipiloto.backend.dto.SolicitudResponse.kt

package co.edu.unipiloto.backend.dto

import co.edu.unipiloto.backend.model.Solicitud

/**
 * 📦 Data Transfer Object (DTO) utilizado para **enviar** la información esencial
 * y simplificada de una Solicitud de envío al front-end.
 *
 * Esta estructura se usa para listas de solicitudes, dashboards de clientes y conductores.
 *
 * @property id El identificador único de la solicitud.
 * @property clientId El ID del cliente que creó la solicitud.
 * @property estado El estado actual de la solicitud (Ej. "PENDIENTE", "ASIGNADA").
 * @property fechaRecoleccion La fecha programada para la recolección.
 * @property franjaHoraria La franja horaria programada para la recolección.
 * @property direccionCompleta La dirección de entrega completa (destino).
 * @property guia DTO anidado con la información de rastreo y número de guía ([GuiaResponse]).
 * @property recolectorId ID del conductor (recolector) asignado a la solicitud. Es `null` si no está asignado.
 * @property recolectorName Nombre completo del conductor (recolector) asignado. Es `null` si no está asignado.
 * @property createdAt Fecha de creación de la solicitud, representada como String.
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
     * 🏗️ Constructor secundario que mapea los campos de la entidad [Solicitud]
     * a los campos del DTO de respuesta.
     *
     * @param solicitud Entidad de la base de datos de la cual se extraen los datos.
     */
    constructor(solicitud: Solicitud) : this(
        // Los IDs son obligatorios en la entidad después de ser guardados (se usa !! para desempaquetar)
        id = solicitud.id!!,
        clientId = solicitud.client.id!!,

        // Convierte el Enum [EstadoSolicitud] a su representación en String.
        estado = solicitud.estado.name,

        fechaRecoleccion = solicitud.fechaRecoleccion,
        franjaHoraria = solicitud.franjaHoraria,

        // Extrae el campo de la entidad Direccion anidada.
        direccionCompleta = solicitud.direccionEntrega.direccionCompleta,

        // Mapeo anidado: Convierte la entidad Guia a su DTO de respuesta.
        guia = GuiaResponse(solicitud.guia),

        // 🛑 Mapeo seguro de conductor (recolector) usando el operador Elvis [?.].
        // Si `solicitud.conductor` es null, los campos `recolectorId` y `recolectorName` serán null.
        recolectorId = solicitud.conductor?.id,
        recolectorName = solicitud.conductor?.fullName,

        // Formato de fecha de creación.
        createdAt = solicitud.createdAt.toString()
    )
}