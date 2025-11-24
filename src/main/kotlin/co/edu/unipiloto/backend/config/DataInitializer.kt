package co.edu.unipiloto.backend.config

import co.edu.unipiloto.backend.model.User
import co.edu.unipiloto.backend.repository.UserRepository
import co.edu.unipiloto.backend.security.PasswordService
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

@Component
class DataInitializer(
    private val userRepository: UserRepository,
    private val passwordService: PasswordService
) : CommandLineRunner {

    override fun run(vararg args: String?) {

        val adminEmail = "admin@empresa.com"

        // Verificar si ya existe un ADMIN con ese email
        if (userRepository.findByEmail(adminEmail) != null) {
            println("ADMIN ya existe, no se creará otro.")
            return
        }

        // Crear contraseña con tu PasswordService
        val hashedPassword = passwordService.hashPasswordSHA256("admin123")

        val admin = User(
            fullName = "Administrador del Sistema",
            email = adminEmail,
            passwordHash = hashedPassword,
            phoneNumber = "3000000000",
            role = "ADMIN",
            sucursal = null,
            isActive = true
        )

        userRepository.save(admin)
        println("ADMIN creado exitosamente.")
    }
}
