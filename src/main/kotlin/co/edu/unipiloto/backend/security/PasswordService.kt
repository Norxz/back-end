package co.edu.unipiloto.backend.security

import org.springframework.stereotype.Service
import java.security.MessageDigest

/**
 * 🔒 Servicio de Spring (`@Service`) encargado de la lógica de seguridad relacionada con contraseñas,
 * específicamente implementando funciones de hashing y verificación compatibles
 * con la aplicación cliente (Android) del sistema, utilizando el algoritmo SHA-256.
 */
@Service
class PasswordService {

    /**
     * # Hashing de Contraseña (SHA-256)
     *
     * Genera un hash criptográfico **SHA-256** de la contraseña dada.
     * Este método es crucial para la seguridad, ya que debe ser **idéntico** al
     * método de hashing utilizado en la aplicación cliente (Android) para garantizar
     * que el hash calculado en el backend coincida con el hash almacenado.
     *
     * **Proceso de Hashing:**
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
            // 1. Convierte la contraseña a bytes usando UTF-8 (codificación estándar)
            val bytes = password.toByteArray(Charsets.UTF_8)

            // 2. Obtiene la instancia del algoritmo criptográfico
            val md = MessageDigest.getInstance("SHA-256")

            // 3. Genera el hash (digest) de los bytes
            val digest = md.digest(bytes)

            // 4. Convierte el array de bytes a una cadena hexadecimal (formato de 64 caracteres)
            digest.joinToString("") { "%02x".format(it) }
        } catch (e: Exception) {
            // Manejo de error: Si SHA-256 no existe, el sistema es inseguro o está mal configurado.
            throw RuntimeException("Error crítico de seguridad: Falló la inicialización del algoritmo SHA-256.", e)
        }
    }

    /**
     * # Verificación de Contraseña
     *
     * 🔑 Verifica si una contraseña proporcionada en texto plano es correcta al compararla con un hash almacenado.
     * Utilizado en la lógica de autenticación (Login).
     *
     * @param rawPassword La contraseña en texto plano ingresada por el usuario.
     * @param storedHash El hash de la contraseña almacenado en la base de datos (Ej: de 64 caracteres).
     * @return `true` si los hashes coinciden (contraseña correcta), `false` en caso contrario.
     */
    fun verifyPassword(rawPassword: String, storedHash: String): Boolean {
        // 1. Recalcula el hash de la contraseña ingresada por el usuario
        val calculatedHash = hashPasswordSHA256(rawPassword)

        // 2. Compara el hash calculado con el hash almacenado en la base de datos
        // NOTA DE SEGURIDAD: Para una comparación más segura (resistente a ataques de tiempo),
        // se debería usar MessageDigest.isEqual() en Java o una comparación constante de tiempo.
        return calculatedHash == storedHash
    }
}