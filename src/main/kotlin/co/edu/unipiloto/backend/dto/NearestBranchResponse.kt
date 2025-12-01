package co.edu.unipiloto.backend.dto

/**
 * 📍 Data Transfer Object (DTO) utilizado para **enviar** la respuesta de
 * la consulta de sucursal más cercana (geolocalización) al front-end.
 *
 * Esta estructura simplificada solo necesita contener el identificador
 * de la sucursal encontrada.
 *
 * @property id El identificador único ([Long]) de la sucursal más cercana encontrada.
 */
data class NearestBranchResponse(
    val id: Long
)