package co.edu.unipiloto.backend.dto

import co.edu.unipiloto.backend.model.User // Importación necesaria para el constructor secundario

/**
 * 👤 Data Transfer Object (DTO) utilizado para **enviar** la información del usuario
 * al cliente (Android/Web) después de un login o registro exitoso, o al listar usuarios.
 *
 * **Propósito clave:** Omitir campos sensibles como `passwordHash` para mantener la seguridad.
 *
 * @property id Identificador único ([Long]) del usuario.
 * @property fullName Nombre completo del usuario.
 * @property email Correo electrónico del usuario.
 * @property phoneNumber Número de teléfono del usuario. Opcional.
 * @property role Rol del usuario como cadena (Ej: "ADMIN", "GESTOR", "CONDUCTOR").
 * @property sucursal DTO anidado que contiene la información de la sucursal asociada al usuario ([SucursalResponse]). Es `null` si el usuario no tiene sucursal asignada (Ej: Cliente, Admin Global).
 * @property isActive Indica si la cuenta del usuario está actualmente activa.
 */
data class UserResponse(
    val id: Long,
    val fullName: String,
    val email: String,
    val phoneNumber: String?,
    val role: String,
    val sucursal: SucursalResponse?,
    val isActive: Boolean
) {
    /**
     * 🏗️ Constructor secundario para mapear la entidad de base de datos [User]
     * a este DTO de respuesta ([UserResponse]).
     *
     * @param user Entidad `User` a mapear.
     * @throws IllegalStateException Si el `id` de la entidad [User] es nulo, lo cual indica un error de persistencia.
     */
    constructor(user: co.edu.unipiloto.backend.model.User) : this(
        // Desempaqueta el ID, lanzando una excepción si es nulo (no debería ocurrir en una entidad guardada).
        id = user.id ?: throw IllegalStateException("User ID cannot be null after save."),
        fullName = user.fullName,
        email = user.email,
        phoneNumber = user.phoneNumber,

        // Convierte el Enum [Role] a su nombre en String.
        role = user.role.name,

        // Mapeo condicional: Si `user.sucursal` no es null, se mapea a [SucursalResponse].
        sucursal = user.sucursal?.let { SucursalResponse(it) },

        isActive = user.isActive
    )
}