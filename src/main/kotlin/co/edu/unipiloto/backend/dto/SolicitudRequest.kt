package co.edu.unipiloto.backend.dto

data class ContactoDTO(
    val nombreCompleto: String,
    val tipoIdentificacion: String,
    val numeroIdentificacion: String,
    val telefono: String
)

data class DireccionDTO(
    val direccionCompleta: String,
    val ciudad: String,
    val latitud: Double,
    val longitud: Double,
    val pisoApto: String?,
    val notasEntrega: String?
)

data class SolicitudRequest(
    val clienteId: Long,
    val remitente: ContactoDTO,
    val destinatario: ContactoDTO,
    val origenDireccion: DireccionDTO,
    val destinoDireccion: DireccionDTO,
    val fechaRecoleccion: String,
    val franjaHoraria: String,
    val tipoServicio: String? = "NORMAL",
    val observaciones: String? = null,
    val peso: Double? = null,
    val alto: Double? = null,
    val ancho: Double? = null,
    val largo: Double? = null,
    val contenido: String? = null
)