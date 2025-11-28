package co.edu.unipiloto.backend.dto

/**
 * DTO utilizado para recibir la información necesaria para crear una nueva solicitud de recolección.
 *
 * @property clientId ID del cliente que realiza la solicitud
 * @property remitente Información del remitente (DTO ClienteRequest)
 * @property receptor Información del receptor (DTO ClienteRequest)
 * @property direccion Dirección de entrega o recolección (DTO DireccionRequest)
 * @property paquete Información del paquete (DTO PaqueteRequest)
 * @property fechaRecoleccion Fecha en la que se debe realizar la recolección, como String
 * @property franjaHoraria Franja horaria de recolección (ej. "08:00-10:00")
 * @property sucursalId ID de la sucursal asignada a la solicitud
 */
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
