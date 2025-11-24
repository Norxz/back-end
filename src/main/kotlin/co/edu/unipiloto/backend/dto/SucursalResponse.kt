package co.edu.unipiloto.backend.dto

import co.edu.unipiloto.backend.model.Sucursal

data class SucursalResponse(
    val id: Long,
    val nombre: String
) {
    constructor(sucursal: Sucursal) : this(
        id = sucursal.id ?: 0,
        nombre = sucursal.nombre
    )
}
