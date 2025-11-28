package co.edu.unipiloto.backend.dto

import co.edu.unipiloto.backend.model.Guia

/**
 * DTO utilizado para enviar información de una guía al cliente.
 *
 * Contiene los datos esenciales de la guía para su visualización.
 *
 * @property numeroGuia Número identificador de la guía
 * @property trackingNumber Código de rastreo de la guía
 * @property fechaCreacion Fecha de creación de la guía, representada como String
 */
data class GuiaResponse(
    val numeroGuia: String,
    val trackingNumber: String,
    val fechaCreacion: String
) {
    /**
     * Constructor que mapea la entidad [Guia] a este DTO.
     *
     * @param guia Entidad de tipo [Guia] que se quiere convertir a DTO
     */
    constructor(guia: Guia) : this(
        numeroGuia = guia.numeroGuia,
        trackingNumber = guia.trackingNumber,
        fechaCreacion = guia.fechaCreacion.toString()
    )
}
