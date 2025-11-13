package co.edu.unipiloto.backend.dto

import co.edu.unipiloto.backend.model.Direccion

/**
 * DTO utilizado para representar direcciones en respuestas al frontend.
 * Simplifica la entidad Direccion y evita problemas con referencias cíclicas.
 */
data class DireccionResponse(
    val id: Long?,
    val direccionCompleta: String,
    val latitud: Double?,
    val longitud: Double?,
    val ciudad: String?,
    val pisoApto: String?,
    val notasEntrega: String?
) {
    constructor(direccion: Direccion) : this(
        id = direccion.id,
        direccionCompleta = direccion.direccionCompleta,
        latitud = direccion.latitud,
        longitud = direccion.longitud,
        ciudad = direccion.ciudad,
        pisoApto = direccion.pisoApto,
        notasEntrega = direccion.notasEntrega
    )
}
