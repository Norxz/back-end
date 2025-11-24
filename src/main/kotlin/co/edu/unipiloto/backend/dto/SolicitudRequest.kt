package co.edu.unipiloto.backend.dto

data class SolicitudRequest(
    val clientId: Long,
    val remitente: ClienteRequest,
    val receptor: ClienteRequest,
    val direccion: DireccionRequest,
    val paquete: PaqueteRequest,
    val fechaRecoleccion: String,
    val franjaHoraria: String,
    val sucursalId: Long
)
