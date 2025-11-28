package co.edu.unipiloto.backend.dto

/**
 * DTO utilizado para recibir la información de un paquete desde el cliente.
 *
 * @property peso Peso del paquete en kilogramos
 * @property alto Alto del paquete en centímetros (opcional)
 * @property ancho Ancho del paquete en centímetros (opcional)
 * @property largo Largo del paquete en centímetros (opcional)
 * @property contenido Descripción del contenido del paquete (opcional)
 */
data class PaqueteRequest(
    val peso: Double,
    val alto: Double?,
    val ancho: Double?,
    val largo: Double?,
    val contenido: String?
)
