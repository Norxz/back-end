package co.edu.unipiloto.backend.dto

/**
 * DTO utilizado para recibir los datos de login desde el cliente.
 *
 * @property email Correo electrónico del usuario
 * @property password Contraseña del usuario en texto plano
 */
data class LoginRequest(
    val email: String,
    val password: String
)
