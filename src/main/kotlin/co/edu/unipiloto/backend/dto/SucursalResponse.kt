// Archivo: co.edu.unipiloto.backend.dto/SucursalResponse.kt

package co.edu.unipiloto.backend.dto

import co.edu.unipiloto.backend.model.Sucursal
import co.edu.unipiloto.backend.model.Direccion // Importación necesaria para el campo anidado 'direccion'

/**
 * 🏢 Data Transfer Object (DTO) de respuesta utilizado para **enviar** la información
 * esencial de una **Sucursal** al cliente de la aplicación (front-end).
 *
 * Este DTO incluye la entidad [Direccion] anidada, permitiendo al frontend
 * acceder a la ciudad, coordenadas geográficas y otros detalles de la ubicación de la sucursal.
 *
 * @property id Identificador único ([Long]) de la sucursal.
 * @property nombre Nombre comercial o descriptivo de la sucursal.
 * @property direccion La entidad [Direccion] anidada que contiene los detalles de la ubicación física de la sucursal.
 */
data class SucursalResponse(
    val id: Long,
    val nombre: String,
    val direccion: Direccion // Se expone la entidad Direccion directamente, no su DTO.
) {
    /**
     * 🏗️ Constructor secundario utilizado para mapear la entidad de base de datos [Sucursal]
     * a este DTO de respuesta ([SucursalResponse]).
     *
     * Permite una conversión clara y desacopla la entidad JPA de la capa de presentación.
     *
     * @param sucursal La entidad [Sucursal] desde la cual se extraen los datos.
     */
    constructor(sucursal: Sucursal) : this(
        // Utiliza el operador Elvis (?:) para manejar el caso de IDs nulos (ej. antes de persistencia).
        // Se asume que el ID ya está persistido y no debería ser null en un response.
        id = sucursal.id ?: 0,
        nombre = sucursal.nombre,
        // Mapea la entidad anidada Direccion directamente, asumiendo que ya está cargada por JPA.
        direccion = sucursal.direccion
    )
}