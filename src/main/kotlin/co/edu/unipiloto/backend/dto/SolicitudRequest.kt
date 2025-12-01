package co.edu.unipiloto.backend.dto

/**
 * 📦 Data Transfer Object (DTO) utilizado para **recibir** toda la información compuesta
 * y necesaria desde el front-end para **crear una nueva solicitud de envío/recolección**.
 *
 * Esta estructura de alto nivel agrupa varios DTOs anidados ([ClienteRequest], [DireccionRequest], [PaqueteRequest]).
 *
 * @property clientId ID del usuario ([User]) que está realizando o creando la solicitud. **Obligatorio**.
 * @property remitente DTO anidado con la información completa del remitente ([ClienteRequest]). **Obligatorio**.
 * @property receptor DTO anidado con la información completa del receptor ([ClienteRequest]). **Obligatorio**.
 * @property direccionRecoleccion DTO anidado con la dirección donde se debe recoger el paquete ([DireccionRequest]). Opcional, puede ser null si la entrega es en sucursal.
 * @property direccionEntrega DTO anidado con la dirección final de destino del paquete ([DireccionRequest]). **Obligatorio**.
 * @property paquete DTO anidado con las dimensiones y contenido del paquete ([PaqueteRequest]). **Obligatorio**.
 * @property fechaRecoleccion Fecha programada para la recolección, representada como String (Ej: "2025-12-31"). **Obligatorio**.
 * @property franjaHoraria El rango de tiempo programado para la recolección (ej. "08:00-10:00"). **Obligatorio**.
 * @property sucursalId ID de la sucursal de origen que ha sido asignada para gestionar la solicitud. **Obligatorio**.
 */
data class SolicitudRequest(
    val clientId: Long,
    val remitente: ClienteRequest,
    val receptor: ClienteRequest,
    val direccionRecoleccion: DireccionRequest?,
    val direccionEntrega: DireccionRequest,
    val paquete: PaqueteRequest,
    val fechaRecoleccion: String,
    val franjaHoraria: String,
    val sucursalId: Long
)