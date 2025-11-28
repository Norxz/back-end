package co.edu.unipiloto.backend.security

import org.springframework.stereotype.Service
import java.security.MessageDigest

/**
 * 🔒 Servicio encargado de la lógica de seguridad relacionada con contraseñas,
 * específicamente implementando funciones de hashing y verificación compatibles
 * con la aplicación Android del sistema.
 */
@Service
class PasswordService {

    /**
     * Genera un hash SHA-256 de la contraseña dada.
     * Este método es crucial para la seguridad, ya que debe ser **idéntico** al
     * método de hashing utilizado en la aplicación cliente (Android) para garantizar
     * que el hash calculado en el backend coincida con el hash almacenado.
     *
     * Pasos del proceso de Hashing:
     * 1. La contraseña en texto plano se convierte a bytes usando UTF-8.
     * 2. Se obtiene una instancia del algoritmo criptográfico **SHA-256**.
     * 3. Se genera el hash (digest) de los bytes de la contraseña.
     * 4. El array de bytes del hash se convierte a una cadena hexadecimal de 64 caracteres.
     *
     * @param password La contraseña en texto plano (sin cifrar).
     * @return El hash de la contraseña como una cadena hexadecimal de 64 caracteres.
     * @throws RuntimeException Si el algoritmo SHA-256 no está disponible en el entorno (fallo crítico de seguridad).
     */
    fun hashPasswordSHA256(password: String): String {
        return try {
            // Convierte la contraseña a bytes usando UTF-8
            val bytes = password.toByteArray(Charsets.UTF_8)

            // Obtiene la instancia del algoritmo SHA-256
            val md = MessageDigest.getInstance("SHA-256")

            // Genera el hash
            val digest = md.digest(bytes)

            // Convierte el array de bytes a una cadena hexadecimal (64 caracteres)
            digest.joinToString("") { "%02x".format(it) }
        } catch (e: Exception) {
            // En un entorno de servidor, esto es un fallo crítico de seguridad.
            throw RuntimeException("Error crítico de seguridad: Falló la inicialización del algoritmo SHA-256.", e)
        }
    }

    /**
     * 🔑 Verifica si una contraseña proporcionada en texto plano es correcta al compararla con un hash almacenado.
     * Utilizado principalmente en la lógica de autenticación (Login).
     *
     * El proceso consiste en:
     * 1. Calcular el hash de la `rawPassword` utilizando `hashPasswordSHA256`.
     * 2. Comparar el hash calculado con el `storedHash` recuperado de la base de datos.
     *
     * @param rawPassword La contraseña en texto plano ingresada por el usuario.
     * @param storedHash El hash de la contraseña almacenado en la base de datos.
     * @return true si los hashes coinciden (contraseña correcta), false en caso contrario.
     */
    fun verifyPassword(rawPassword: String, storedHash: String): Boolean {
        // Recalcula el hash de la contraseña ingresada
        val calculatedHash = hashPasswordSHA256(rawPassword)
        // Compara el hash calculado con el hash almacenado
        return calculatedHash == storedHash
    }
}