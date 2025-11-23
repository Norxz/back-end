package co.edu.unipiloto.backend.dto

/**
 * DTO utilizado para enviar datos del usuario al cliente (Android/Postman)
 * después de un login/registro exitoso, omitiendo el passwordHash.
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
    // Constructor de mapeo (para mapear la Entidad User a este DTO)
    constructor(user: co.edu.unipiloto.backend.model.User) : this(
        id = user.id ?: throw IllegalStateException("User ID cannot be null after save."),
        fullName = user.fullName,
        email = user.email,
        phoneNumber = user.phoneNumber,
        role = user.role,
        sucursal = user.sucursal?.let { SucursalResponse(it) },
        isActive = user.isActive
    )
}