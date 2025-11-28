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
 * Servicio encargado de la lógica de negocio para la gestión de usuarios (Registro, Login, etc.).
 * Utiliza UserRepository para la persistencia y PasswordService para el hashing.
 */
@Service
class AuthService(
    private val userRepository: UserRepository,
    private val passwordService: PasswordService,
    private val sucursalRepository: SucursalRepository
) {

    /**
     * Registra un nuevo usuario en la base de datos.
     * @throws ResourceAlreadyExistsException si el email ya está en uso.
     */
    fun register(request: RegisterRequest): User {

        // 1. Validar duplicados
        if (userRepository.existsByEmail(request.email)) {
            throw ResourceAlreadyExistsException("El email ${request.email} ya está registrado.")
        }

        // 2. Hashing de contraseña
        val passwordHash = passwordService.hashPasswordSHA256(request.password)

        // 3. Buscar sucursal si viene
        val sucursal: Sucursal? = request.sucursalId?.let { id ->
            sucursalRepository.findById(id).orElseThrow {
                IllegalArgumentException("La sucursal con ID $id no existe.")
            }
        }

        // 4. Crear la entidad User real
        val newUser = User(
            fullName = request.fullName,
            email = request.email,
            passwordHash = passwordHash,
            phoneNumber = request.phoneNumber,
            role = Role.valueOf(request.role.uppercase()),
            sucursal = sucursal,
            isActive = request.isActive
        )

        // 5. Guardar y retornar
        return userRepository.save(newUser)
    }


    /**
     * Intenta autenticar un usuario.
     * @param email Email del usuario.
     * @param rawPassword Contraseña en texto plano (recibida del cliente).
     * @return El objeto User si la autenticación es exitosa, o null si falla.
     */
    fun login(email: String, rawPassword: String): User? {
        val user = userRepository.findByEmail(email) ?: return null

        return if (passwordService.verifyPassword(rawPassword, user.passwordHash)) {
            user
        } else null
    }
}