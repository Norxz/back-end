package co.edu.unipiloto.backend.dto

data class ClienteRequest(
    val id: Long? = null,       // null si es nuevo
    val nombre: String,
    val tipoId: String?,
    val numeroId: String?,
    val telefono: String?,
    val codigoPais: String?
)