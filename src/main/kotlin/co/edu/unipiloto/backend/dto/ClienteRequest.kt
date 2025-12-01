package co.edu.unipiloto.backend.dto

/**
 * 📨 Data Transfer Object (DTO) utilizado para **recibir** información del cliente
 * desde el front-end (Cliente, Remitente o Receptor).
 *
 * Es la estructura de datos para:
 * 1. Crear un cliente nuevo.
 * 2. Actualizar un cliente existente.
 *
 * @property id ID del cliente. Es opcional ([null]) si se está creando un nuevo cliente.
 * @property nombre Nombre completo del cliente o razón social.
 * @property tipoId Tipo de identificación del cliente (ej. "CC", "NIT", "CE"). Opcional.
 * @property numeroId Número único de identificación del cliente. Opcional.
 * @property telefono Número de teléfono de contacto. Opcional.
 * @property codigoPais Código telefónico internacional (ej. "+57", "+1"). Opcional.
 */
data class ClienteRequest(
    val id: Long? = null,
    val nombre: String,
    val tipoId: String?,
    val numeroId: String?,
    val telefono: String?,
    val codigoPais: String?
)