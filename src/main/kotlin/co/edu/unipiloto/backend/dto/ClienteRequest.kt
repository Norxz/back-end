package co.edu.unipiloto.backend.dto

/**
 * DTO utilizado para recibir información del cliente desde el front-end.
 *
 * Puede ser usado tanto para crear un cliente nuevo como para actualizar uno existente.
 *
 * @property id ID del cliente (null si es un nuevo cliente)
 * @property nombre Nombre completo del cliente
 * @property tipoId Tipo de identificación (ej. CC, NIT)
 * @property numeroId Número de identificación
 * @property telefono Teléfono del cliente
 * @property codigoPais Código del país del teléfono (ej. +57)
 */
data class ClienteRequest(
    val id: Long? = null,
    val nombre: String,
    val tipoId: String?,
    val numeroId: String?,
    val telefono: String?,
    val codigoPais: String?
)
