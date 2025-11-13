package co.edu.unipiloto.backend.security

import org.springframework.stereotype.Service
import java.security.MessageDigest

/**
 * Servicio encargado de la lógica de seguridad relacionada con contraseñas,
 * incluyendo el hashing compatible con la aplicación Android (SHA-256).
 */
@Service
class PasswordService {

    /**
     * Genera un hash SHA-256 de la contraseña dada.
     * Este método debe ser idéntico al que se usa en la aplicación Android.
     * * @param password La contraseña en texto plano (recibida de Postman).
     * @return El hash de la contraseña como una cadena hexadecimal de 64 caracteres.
     */
    fun hashPasswordSHA256(password: String): String {
        return try {
            // Convierte la contraseña a bytes usando UTF-8
            val bytes = password.toByteArray(Charsets.UTF_8)

            // Obtiene la instancia del algoritmo SHA-256
            val md = MessageDigest.getInstance("SHA-256")

            // Genera el hash
            val digest = md.digest(bytes)

            // Convierte el array de bytes a una cadena hexadecimal
            digest.joinToString("") { "%02x".format(it) }
        } catch (e: Exception) {
            // En un entorno de servidor, esto es un fallo crítico de seguridad.
            // Es mejor lanzar un error para evitar continuar con un hash inválido.
            throw RuntimeException("Error crítico de seguridad: Falló la inicialización del algoritmo SHA-256.", e)
        }
    }

    /**
     * Verifica si una contraseña de texto plano coincide con un hash almacenado.
     * Utilizado para la lógica de Login.
     */
    fun verifyPassword(rawPassword: String, storedHash: String): Boolean {
        val calculatedHash = hashPasswordSHA256(rawPassword)
        return calculatedHash == storedHash
    }
}