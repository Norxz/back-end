package co.edu.unipiloto.backend.dto

/**
 * 📦 Data Transfer Object (DTO) utilizado para **recibir** la información física
 * y descriptiva de un paquete desde el front-end, necesaria para calcular costos
 * y asignar logística.
 *
 * @property peso Peso del paquete, expresado en kilogramos (kg). **Obligatorio** para el cálculo de flete.
 * @property alto Altura del paquete, expresada en centímetros (cm). Opcional.
 * @property ancho Ancho del paquete, expresado en centímetros (cm). Opcional.
 * @property largo Largo del paquete, expresado en centímetros (cm). Opcional.
 * @property contenido Breve descripción del contenido del paquete (ej. "Documentos", "Electrónica"). Opcional.
 */
data class PaqueteRequest(
    val peso: Double,
    val alto: Double?,
    val ancho: Double?,
    val largo: Double?,
    val contenido: String?
)