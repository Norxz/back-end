package co.edu.unipiloto.backend.dto



data class SucursalRequest(
    val nombre: String,
    val direccion: DireccionRequest
)