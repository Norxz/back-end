package co.edu.unipiloto.backend.dto

data class LoginRequest(
    val email: String,
    val password: String // Contraseña en texto plano
)