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
 * 🔑 Servicio encargado de la lógica de negocio para la gestión de usuarios (Registro y Login).
 * Actúa como la capa de autenticación principal del sistema, orquestando la persistencia
 * de usuarios y el manejo seguro de contraseñas.
 */
@Service
class AuthService(
    private val userRepository: UserRepository,
    private val passwordService: PasswordService,
    private val sucursalRepository: SucursalRepository
) {

    /**
     * 📝 Registra un nuevo usuario en la base de datos.
     * Implementa la lógica de validación de unicidad de email, hashing de contraseñas
     * y la correcta asignación de rol y sucursal.
     *
     * @param request El objeto [RegisterRequest] que contiene los datos del nuevo usuario.
     * @return El objeto [User] recién creado y persistido.
     * @throws ResourceAlreadyExistsException si el email ya está en uso.
     * @throws IllegalArgumentException si el ID de sucursal proporcionado no existe.
     */
    fun register(request: RegisterRequest): User {

        // 1. Validar duplicados
        if (userRepository.existsByEmail(request.email)) {
            // Lanza una excepción si el email ya existe para prevenir la duplicidad.
            throw ResourceAlreadyExistsException("El email ${request.email} ya está registrado.")
        }

        // 2. Hashing de contraseña
        // Utiliza PasswordService para generar un hash seguro (SHA-256) de la contraseña en texto plano.
        val passwordHash = passwordService.hashPasswordSHA256(request.password)

        // 3. Buscar sucursal si viene
        // Si se proporciona un sucursalId, se busca la Sucursal correspondiente.
        val sucursal: Sucursal? = request.sucursalId?.let { id ->
            sucursalRepository.findById(id).orElseThrow {
                // Lanza error si el ID de sucursal es proporcionado pero no es válido.
                IllegalArgumentException("La sucursal con ID $id no existe.")
            }
        }

        // 4. Crear la entidad User real
        val newUser = User(
            fullName = request.fullName,
            email = request.email,
            passwordHash = passwordHash,
            phoneNumber = request.phoneNumber,
            // Convierte el String del DTO a la enumeración Role, asegurando que sea en mayúsculas.
            role = Role.valueOf(request.role.uppercase()),
            sucursal = sucursal,
            isActive = request.isActive
        )

        // 5. Guardar y retornar
        return userRepository.save(newUser)
    }


    /**
     * ✅ Intenta autenticar un usuario verificando su email y contraseña.
     *
     * El flujo de login es:
     * 1. Buscar el usuario por email.
     * 2. Si el usuario existe, se verifica la `rawPassword` (texto plano) contra el `passwordHash` almacenado
     * usando el `PasswordService`.
     * 3. Si la verificación es exitosa, se retorna el objeto [User].
     *
     * @param email Email del usuario.
     * @param rawPassword Contraseña en texto plano (recibida del cliente).
     * @return El objeto [User] si la autenticación es exitosa, o null si el usuario no existe o la contraseña es incorrecta.
     */
    fun login(email: String, rawPassword: String): User? {
        // Busca el usuario. Si no lo encuentra, retorna null inmediatamente.
        val user = userRepository.findByEmail(email) ?: return null

        // Verifica la contraseña utilizando el servicio de seguridad
        return if (passwordService.verifyPassword(rawPassword, user.passwordHash)) {
            user // Autenticación exitosa
        } else null // Contraseña incorrecta
    }
}