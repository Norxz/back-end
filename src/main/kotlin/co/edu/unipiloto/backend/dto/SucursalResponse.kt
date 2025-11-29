// Archivo: co.edu.unipiloto.backend.dto/SucursalResponse.kt

package co.edu.unipiloto.backend.dto

import co.edu.unipiloto.backend.model.Sucursal
import co.edu.unipiloto.backend.model.Direccion // Importación necesaria para el campo anidado 'direccion'

/**
 * 🏢 Data Transfer Object (DTO) de respuesta utilizado para enviar la información
 * esencial de una Sucursal al cliente de la aplicación.
 *
 * Este DTO incluye el objeto [Direccion] anidado, permitiendo al frontend
 * acceder a la ciudad y otros detalles de la ubicación.
 *
 * @property id Identificador único de la sucursal.
 * @property nombre Nombre comercial de la sucursal.
 * @property direccion Objeto que contiene los detalles de la dirección física de la sucursal.
 */
data class SucursalResponse(
    val id: Long,
    val nombre: String,
    val direccion: Direccion
) {
    /**
     * Constructor secundario utilizado para mapear la entidad de base de datos [Sucursal]
     * a este DTO de respuesta.
     *
     * Permite una conversión clara y desacopla la entidad JPA de la capa de presentación.
     *
     * @param sucursal La entidad [Sucursal] desde la cual se extraen los datos.
     */
    constructor(sucursal: Sucursal) : this(
        // Utiliza el operador Elvis (?:) para manejar el caso de IDs nulos (ej. antes de persistencia).
        id = sucursal.id ?: 0,
        nombre = sucursal.nombre,
        // Mapea la entidad anidada Direccion directamente, asumiendo que ya está cargada.
        direccion = sucursal.direccion
    )
}