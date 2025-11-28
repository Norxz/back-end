package co.edu.unipiloto.backend.dto

/**
 * DTO utilizado para enviar datos del usuario al cliente (Android/Postman)
 * después de un login o registro exitoso, omitiendo el campo `passwordHash`.
 *
 * @property id Identificador único del usuario
 * @property fullName Nombre completo del usuario
 * @property email Correo electrónico del usuario
 * @property phoneNumber Número de teléfono del usuario (opcional)
 * @property role Rol del usuario como cadena (ej: ADMIN, GESTOR, CONDUCTOR)
 * @property sucursal Información de la sucursal asociada (opcional)
 * @property isActive Indica si el usuario está activo
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
     * Constructor secundario para mapear la entidad `User` a este DTO.
     *
     * @param user Entidad `User` a mapear
     * @throws IllegalStateException si el `id` del usuario es nulo
     */
    constructor(user: co.edu.unipiloto.backend.model.User) : this(
        id = user.id ?: throw IllegalStateException("User ID cannot be null after save."),
        fullName = user.fullName,
        email = user.email,
        phoneNumber = user.phoneNumber,
        role = user.role.name,
        sucursal = user.sucursal?.let { SucursalResponse(it) },
        isActive = user.isActive
    )
}
