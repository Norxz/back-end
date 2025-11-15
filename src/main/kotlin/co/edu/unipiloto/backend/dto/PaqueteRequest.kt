package co.edu.unipiloto.backend.dto

data class PaqueteRequest(
    val peso: Double,
    val alto: Double?,
    val ancho: Double?,
    val largo: Double?,
    val contenido: String?
)