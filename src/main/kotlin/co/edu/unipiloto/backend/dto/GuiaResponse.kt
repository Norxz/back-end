package co.edu.unipiloto.backend.dto

import co.edu.unipiloto.backend.model.Guia

/**
 * 🏷️ Data Transfer Object (DTO) utilizado para **enviar** información de una guía
 * (número de seguimiento) al cliente o a otros servicios.
 *
 * Contiene los datos esenciales de la guía, optimizados para su visualización y rastreo.
 *
 * @property numeroGuia Número interno identificador de la guía (puede ser el ID o un código correlativo).
 * @property trackingNumber Código de rastreo único (código alfanumérico) que el cliente usa para seguir el envío.
 * @property fechaCreacion Fecha y hora de creación de la guía, representada como String (generalmente ISO 8601).
 */
data class GuiaResponse(
    val numeroGuia: String,
    val trackingNumber: String,
    val fechaCreacion: String
) {
    /**
     * 🏗️ Constructor secundario que facilita el mapeo de la entidad de la base de datos
     * ([Guia]) a este DTO ([GuiaResponse]).
     *
     * @param guia Entidad de tipo [Guia] que se quiere convertir a DTO de respuesta.
     */
    constructor(guia: Guia) : this(
        numeroGuia = guia.numeroGuia,
        trackingNumber = guia.trackingNumber,
        // Convierte la fecha de creación (probablemente un Instant) a su representación String.
        fechaCreacion = guia.fechaCreacion.toString()
    )
}