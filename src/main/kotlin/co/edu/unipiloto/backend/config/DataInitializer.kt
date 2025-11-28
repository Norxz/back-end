package co.edu.unipiloto.backend.config

import co.edu.unipiloto.backend.model.User
import co.edu.unipiloto.backend.model.enums.Role
import co.edu.unipiloto.backend.repository.UserRepository
import co.edu.unipiloto.backend.security.PasswordService
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

/**
 * Componente de inicialización de datos al iniciar la aplicación.
 *
 * Se encarga de crear un usuario ADMIN por defecto si no existe.
 * Implementa [CommandLineRunner] para ejecutar la lógica al arrancar Spring Boot.
 */
@Component
class DataInitializer(
    private val userRepository: UserRepository,
    private val passwordService: PasswordService
) : CommandLineRunner {

    /**
     * Método que se ejecuta al inicio de la aplicación.
     * Crea un usuario ADMIN con email y contraseña predefinidos si no existe.
     *
     * @param args Argumentos de línea de comando (no utilizados).
     */
    override fun run(vararg args: String?) {

        val adminEmail = "admin@empresa.com"

        // Verificar si ya existe un ADMIN con ese email
        if (userRepository.findByEmail(adminEmail) != null) {
            println("ADMIN ya existe, no se creará otro.")
            return
        }

        // Crear contraseña usando PasswordService
        val hashedPassword = passwordService.hashPasswordSHA256("admin123")

        // Crear usuario ADMIN por defecto
        val admin = User(
            fullName = "Administrador del Sistema",
            email = adminEmail,
            passwordHash = hashedPassword,
            phoneNumber = "3000000000",
            role = Role.ADMIN,
            sucursal = null,
            isActive = true
        )

        // Guardar en la base de datos
        userRepository.save(admin)
        println("ADMIN creado exitosamente.")
    }
}
