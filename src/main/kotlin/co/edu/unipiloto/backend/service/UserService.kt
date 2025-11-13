package co.edu.unipiloto.backend.service

import co.edu.unipiloto.backend.dto.RegisterRequest
import co.edu.unipiloto.backend.exception.ResourceAlreadyExistsException
import co.edu.unipiloto.backend.model.User
import co.edu.unipiloto.backend.repository.UserRepository
import co.edu.unipiloto.backend.security.PasswordService
import org.springframework.stereotype.Service

/**
 * Servicio encargado de la lógica de negocio para la gestión de usuarios (Registro, Login, etc.).
 * Utiliza UserRepository para la persistencia y PasswordService para el hashing.
 */
@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordService: PasswordService // 👈 Inyección del servicio de hashing
) {

    /**
     * Registra un nuevo usuario en la base de datos.
     * @throws ResourceAlreadyExistsException si el email ya está en uso.
     */
    fun register(request: RegisterRequest): User {

        // 1. Validar duplicados
        if (userRepository.findByEmail(request.email) != null) {
            throw ResourceAlreadyExistsException("El email ${request.email} ya está registrado.")
        }

        // 2. 🌟 HASHING DE CONTRASEÑA DELEGADO 🌟
        // Se llama al servicio para generar el hash SHA-256
        val passwordHash = passwordService.hashPasswordSHA256(request.password)

        // 3. Crear Entidad
        val newUser = User(
            fullName = request.fullName,
            email = request.email,
            passwordHash = passwordHash, // Usamos el hash generado
            phoneNumber = request.phoneNumber,
            role = request.role.uppercase(),
            sucursal = request.sucursal,
            isActive = request.isActive
        )

        // 4. Guardar y retornar
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

        // 2. Verificar la contraseña usando el servicio de hashing
        val storedHash = user.passwordHash

        return if (passwordService.verifyPassword(rawPassword, storedHash)) {
            user // Login exitoso
        } else {
            null // Contraseña incorrecta
        }
    }
}