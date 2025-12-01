package co.edu.unipiloto.backend.config

import co.edu.unipiloto.backend.model.User
import co.edu.unipiloto.backend.model.enums.Role
import co.edu.unipiloto.backend.repository.UserRepository
import co.edu.unipiloto.backend.security.PasswordService
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

/**
 * 🚀 Componente de inicialización de datos (Data Seeding) al iniciar la aplicación.
 *
 * Se encarga de garantizar la existencia de datos esenciales en la base de datos,
 * como un usuario **ADMIN** por defecto, si este no existe.
 *
 * Implementa [CommandLineRunner], lo que asegura que el método [run] se ejecute
 * inmediatamente después de que el [ApplicationContext] de Spring Boot haya sido cargado.
 *
 * @property userRepository Repositorio para acceder a las operaciones de la entidad [User].
 * @property passwordService Servicio para encriptación de contraseñas.
 */
@Component
class DataInitializer(
    private val userRepository: UserRepository,
    private val passwordService: PasswordService
) : CommandLineRunner {

    /**
     * 🏁 Método principal que se ejecuta al inicio de la aplicación.
     * Implementa la lógica de inicialización de datos, asegurando la idempotencia
     * al verificar primero si el recurso a crear ya existe.
     *
     * @param args Argumentos de línea de comando (no utilizados en esta implementación).
     */
    override fun run(vararg args: String?) {

        val adminEmail = "admin@empresa.com" // Email predefinido para el usuario administrador.

        // 1. Verificar si ya existe un usuario ADMIN con el email predefinido.
        if (userRepository.findByEmail(adminEmail) != null) {
            println("ADMIN ya existe, no se creará otro.")
            return // Salir del método si ya existe para evitar duplicados.
        }

        // 2. Crear contraseña encriptada usando el servicio de seguridad.
        // Se utiliza SHA256 (según el nombre del método) para proteger la contraseña.
        val hashedPassword = passwordService.hashPasswordSHA256("admin123")

        // 3. Crear la entidad [User] para el administrador por defecto.
        val admin = User(
            fullName = "Administrador del Sistema",
            email = adminEmail,
            passwordHash = hashedPassword,
            phoneNumber = "3000000000",
            role = Role.ADMIN, // Establece el rol como ADMINISTRADOR.
            sucursal = null,   // El administrador del sistema no está atado a una sucursal específica.
            isActive = true
        )

        // 4. Guardar la nueva entidad en la base de datos.
        userRepository.save(admin)
        println("ADMIN creado exitosamente.")
    }
}