package co.edu.unipiloto.backend.service

import co.edu.unipiloto.backend.dto.RegisterRequest
import co.edu.unipiloto.backend.exception.ResourceAlreadyExistsException
import co.edu.unipiloto.backend.model.User
import co.edu.unipiloto.backend.model.Sucursal
import co.edu.unipiloto.backend.model.enums.Role
import co.edu.unipiloto.backend.repository.SucursalRepository
import co.edu.unipiloto.backend.repository.UserRepository
import co.edu.unipiloto.backend.security.PasswordService
import org.springframework.stereotype.Service
import java.time.Instant

/**
 * 🔑 Servicio de Spring (`@Service`) encargado de la lógica de negocio para la **gestión de usuarios** (Registro y Login).
 *
 * Actúa como la capa de autenticación principal del sistema, orquestando la persistencia
 * de usuarios y el manejo seguro de contraseñas mediante el [PasswordService].
 */
@Service
class AuthService(
    private val userRepository: UserRepository,
    private val passwordService: PasswordService,
    private val sucursalRepository: SucursalRepository
) {

    /**
     * ## 📝 Registro de Nuevo Usuario
     *
     * Persiste un nuevo usuario en la base de datos tras validar la unicidad del email,
     * aplicar el hashing a la contraseña y asignar correctamente el rol y la sucursal.
     *
     * @param request El objeto [RegisterRequest] que contiene los datos del nuevo usuario.
     * @return El objeto [User] recién creado y persistido.
     * @throws ResourceAlreadyExistsException si el email ya está en uso.
     * @throws IllegalArgumentException si el ID de sucursal proporcionado no existe.
     */
    fun register(request: RegisterRequest): User {

        // 1. **Validar unicidad de email**
        if (userRepository.existsByEmail(request.email)) {
            throw ResourceAlreadyExistsException("El email ${request.email} ya está registrado.")
        }

        // 2. **Hashing de contraseña**
        // Se asegura que la contraseña nunca se almacene en texto plano.
        val passwordHash = passwordService.hashPasswordSHA256(request.password)

        // 3. **Buscar sucursal (si aplica)**
        // Si el DTO incluye un ID de sucursal (para roles operativos), se busca la entidad.
        val sucursal: Sucursal? = request.sucursalId?.let { id ->
            sucursalRepository.findById(id).orElseThrow {
                // Lanza error si el ID de sucursal es proporcionado pero es inválido.
                IllegalArgumentException("La sucursal con ID $id no existe.")
            }
        }

        // 4. **Crear y mapear la entidad User**
        val newUser = User(
            fullName = request.fullName,
            email = request.email,
            passwordHash = passwordHash,
            phoneNumber = request.phoneNumber,
            // Convierte el String del DTO a la enumeración Role, asegurando que el caso sea correcto.
            role = Role.valueOf(request.role.uppercase()),
            sucursal = sucursal,
            isActive = request.isActive,
            fechaCreacion = Instant.now()
        )

        // 5. **Guardar y retornar**
        return userRepository.save(newUser)
    }

    /**
     * ## ✅ Autenticación de Usuario (Login)
     *
     * Intenta autenticar un usuario verificando su email y la contraseña en texto plano
     * contra el hash almacenado en la base de datos.
     *
     * **Flujo de Login:**
     * 1. Busca el usuario por email.
     * 2. Si existe, verifica la `rawPassword` contra el `passwordHash` almacenado.
     * 3. Si ambos son correctos, retorna el [User].
     *
     * @param email Email del usuario.
     * @param rawPassword Contraseña en texto plano (recibida del cliente).
     * @return El objeto [User] si la autenticación es exitosa, o `null` si falla (usuario no existe o contraseña incorrecta).
     */
    fun login(email: String, rawPassword: String): User? {
        // 1. Busca el usuario. Si no lo encuentra, retorna null.
        val user = userRepository.findByEmail(email) ?: return null

        // 2. Verifica la contraseña utilizando el servicio de seguridad
        val isPasswordCorrect = passwordService.verifyPassword(rawPassword, user.passwordHash)

        // 3. Retorna el usuario si la verificación es exitosa
        return if (isPasswordCorrect) {
            user
        } else {
            null
        }
    }
}