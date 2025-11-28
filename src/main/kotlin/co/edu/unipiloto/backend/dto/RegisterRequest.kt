package co.edu.unipiloto.backend.dto

/**
 * DTO utilizado para recibir la información de un nuevo usuario al registrarse.
 *
 * @property fullName Nombre completo del usuario
 * @property email Correo electrónico del usuario
 * @property password Contraseña en texto plano
 * @property phoneNumber Número de teléfono del usuario (opcional)
 * @property role Rol del usuario (ej. ADMIN, GESTOR, CONDUCTOR, CLIENTE)
 * @property sucursalId ID de la sucursal a la que pertenece el usuario (opcional)
 * @property isActive Indica si el usuario está activo (por defecto true)
 */
data class RegisterRequest(
    val fullName: String,
    val email: String,
    val password: String,
    val phoneNumber: String?,
    val role: String,
    val sucursalId: Long?,
    val isActive: Boolean = true
)
