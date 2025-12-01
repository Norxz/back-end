package co.edu.unipiloto.backend.dto

/**
 * 🏢 Data Transfer Object (DTO) utilizado para **recibir** la información necesaria
 * desde el front-end para **crear o actualizar una entidad Sucursal**.
 *
 * Esta estructura de datos combina el nombre de la sucursal con su información de ubicación detallada.
 *
 * @property nombre Nombre o identificador comercial de la sucursal (Ej. "Sucursal Centro", "Bodega Norte"). **Obligatorio**.
 * @property direccion DTO anidado que contiene la información geográfica y de texto de la ubicación de la sucursal ([DireccionRequest]). **Obligatorio**.
 */
data class SucursalRequest(
    val nombre: String,
    val direccion: DireccionRequest
)