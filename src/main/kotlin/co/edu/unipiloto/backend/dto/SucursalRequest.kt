package co.edu.unipiloto.backend.dto

/**
 * DTO utilizado para recibir la información necesaria para crear o actualizar una sucursal.
 *
 * @property nombre Nombre de la sucursal
 * @property direccion Información de la dirección asociada a la sucursal
 */
data class SucursalRequest(
    val nombre: String,
    val direccion: DireccionRequest
)
