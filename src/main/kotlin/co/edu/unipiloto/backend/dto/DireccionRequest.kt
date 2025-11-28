package co.edu.unipiloto.backend.dto

/**
 * DTO utilizado para recibir información de una dirección desde el front-end.
 *
 * Se puede usar para crear o actualizar direcciones asociadas a clientes o sucursales.
 *
 * @property direccionCompleta Dirección completa en formato texto
 * @property ciudad Ciudad donde se encuentra la dirección
 * @property latitud Latitud geográfica de la dirección (opcional)
 * @property longitud Longitud geográfica de la dirección (opcional)
 * @property pisoApto Piso o apartamento, si aplica (opcional)
 * @property notasEntrega Notas adicionales para la entrega (opcional)
 * @property barrio Barrio o sector de la dirección (opcional)
 * @property codigoPostal Código postal de la dirección (opcional)
 * @property tipoDireccion Tipo de dirección (ej. residencial, comercial) (opcional)
 */
data class DireccionRequest(
    val direccionCompleta: String,
    val ciudad: String,
    val latitud: Double?,
    val longitud: Double?,
    val pisoApto: String?,
    val notasEntrega: String?,
    val barrio: String?,
    val codigoPostal: String?,
    val tipoDireccion: String?
)
