package co.edu.unipiloto.backend.dto

import co.edu.unipiloto.backend.model.Sucursal

/**
 * DTO utilizado para enviar la información de una sucursal al cliente.
 *
 * @property id Identificador único de la sucursal
 * @property nombre Nombre de la sucursal
 */
data class SucursalResponse(
    val id: Long,
    val nombre: String
) {
    /**
     * Constructor secundario para mapear la entidad `Sucursal` a este DTO.
     *
     * @param sucursal Entidad `Sucursal` a mapear
     */
    constructor(sucursal: Sucursal) : this(
        id = sucursal.id ?: 0,
        nombre = sucursal.nombre
    )
}
