package co.edu.unipiloto.backend.dto

/**
 * 🗺️ Data Transfer Object (DTO) utilizado para **recibir** información de una dirección
 * desde el front-end.
 *
 * Esta estructura de datos se emplea para:
 * 1. Crear una nueva dirección (de recolección o entrega) para una solicitud.
 * 2. Actualizar una dirección existente.
 *
 * @property direccionCompleta La dirección física completa en formato texto (Ej: Calle 10 # 5-45). **Obligatorio**.
 * @property ciudad La ciudad o municipio donde se encuentra la dirección. **Obligatorio**.
 * @property latitud Latitud geográfica precisa de la dirección. Opcional, pero recomendado para logística.
 * @property longitud Longitud geográfica precisa de la dirección. Opcional, pero recomendado para logística.
 * @property pisoApto Información adicional como número de piso o apartamento, si aplica. Opcional.
 * @property notasEntrega Instrucciones adicionales o puntos de referencia para el conductor/repartidor. Opcional.
 * @property barrio Barrio o sector específico de la dirección. Opcional.
 * @property codigoPostal Código postal de la zona. Opcional.
 * @property tipoDireccion Clasificación de la dirección (ej. "residencial", "comercial", "sucursal"). Opcional.
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