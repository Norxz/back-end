package co.edu.unipiloto.backend.dto

/**
 * 📝 Data Transfer Object (DTO) utilizado para **recibir** la información de
 * un **nuevo usuario** al registrarse en el sistema.
 *
 * Esta estructura contiene todos los datos necesarios para crear la entidad [User]
 * en la base de datos (antes de la encriptación de la contraseña).
 *
 * @property fullName Nombre completo del usuario. **Obligatorio**.
 * @property email Correo electrónico único del usuario. **Obligatorio**.
 * @property password Contraseña proporcionada por el usuario en texto plano. **Obligatorio** (Será hasheada en el servicio).
 * @property phoneNumber Número de teléfono de contacto. Opcional.
 * @property role Rol del usuario dentro del sistema (ej. "ADMIN", "GESTOR", "CONDUCTOR", "CLIENTE"). **Obligatorio**.
 * @property sucursalId ID de la sucursal a la que será asignado el usuario (si su rol lo requiere, ej. GESTOR o CONDUCTOR). Opcional.
 * @property isActive Indica si la cuenta del usuario debe estar activa inmediatamente después del registro. Por defecto es `true`.
 */
data class RegisterRequest(
    val fullName: String,
    val email: String,
    val password: String,
    val phoneNumber: String?,
    val role: String,
    val sucursalId: Long?,
    val isActive: Boolean = true
)