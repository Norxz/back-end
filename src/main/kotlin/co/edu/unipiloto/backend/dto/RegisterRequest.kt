package co.edu.unipiloto.backend.dto

data class RegisterRequest(
    val fullName: String,
    val email: String,
    val password: String,
    val phoneNumber: String?,
    val role: String,
    val sucursalId: Long?,
    val isActive: Boolean = true
)