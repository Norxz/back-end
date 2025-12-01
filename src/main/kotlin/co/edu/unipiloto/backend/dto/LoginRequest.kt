package co.edu.unipiloto.backend.dto

/**
 * 🔑 Data Transfer Object (DTO) utilizado para **recibir** las credenciales de
 * inicio de sesión (login) desde el front-end (cliente).
 *
 * Es la estructura mínima necesaria para la autenticación de un usuario.
 *
 * @property email Correo electrónico del usuario que intenta iniciar sesión. **Obligatorio**.
 * @property password Contraseña del usuario en texto plano. Esta contraseña debe ser
 * enviada a la capa de servicio para ser hasheada y comparada con el hash almacenado en la DB. **Obligatorio**.
 */
data class LoginRequest(
    val email: String,
    val password: String
)