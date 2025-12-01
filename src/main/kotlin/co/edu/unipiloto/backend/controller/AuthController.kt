package co.edu.unipiloto.backend.controller

import co.edu.unipiloto.backend.dto.LoginRequest
import co.edu.unipiloto.backend.dto.RegisterRequest
import co.edu.unipiloto.backend.exception.ResourceAlreadyExistsException
import co.edu.unipiloto.backend.service.AuthService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import co.edu.unipiloto.backend.dto.UserResponse
import org.springframework.web.bind.annotation.*

/**
 * 🔒 Controlador REST para la autenticación y gestión de sesiones de usuarios.
 *
 * Expone endpoints clave para:
 * - Registro (`/register`) de nuevos usuarios.
 * - Inicio de sesión (`/login`) de usuarios existentes.
 *
 * @property authService El servicio que contiene la lógica de negocio para la autenticación y registro.
 */
@RestController
@RequestMapping("/api/v1/auth")
class AuthController(private val authService: AuthService) {

    /**
     * 📝 Endpoint para registrar un nuevo usuario en el sistema.
     *
     * Mapea a: `POST /api/v1/auth/register`
     *
     * @param request DTO ([RegisterRequest]) que contiene todos los datos de registro
     * (nombre completo, email, contraseña, rol, etc.).
     * @return [ResponseEntity] con:
     * - HTTP **201 CREATED** y los datos del usuario registrado ([UserResponse]) si tiene éxito.
     * - HTTP **409 CONFLICT** si el usuario (ej. el email) ya existe en el sistema.
     * - HTTP **500 INTERNAL_SERVER_ERROR** en caso de cualquier otro error inesperado.
     */
    @PostMapping("/register")
    fun registerUser(@RequestBody request: RegisterRequest): ResponseEntity<*> {
        return try {
            // Llama al servicio para ejecutar la lógica de registro, hashing de contraseña, etc.
            val newUser = authService.register(request)
            // Retorna 201 CREATED con la representación DTO del nuevo usuario.
            ResponseEntity(UserResponse(newUser), HttpStatus.CREATED)
        } catch (e: ResourceAlreadyExistsException) {
            // Maneja el caso específico de que el recurso (ej. email) ya esté en uso.
            ResponseEntity(e.message, HttpStatus.CONFLICT)
        } catch (e: Exception) {
            // Manejo de excepciones genéricas.
            ResponseEntity("Error interno del servidor.", HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    /**
     * 🚪 Endpoint para iniciar sesión (login) de un usuario existente.
     *
     * Mapea a: `POST /api/v1/auth/login`
     *
     * @param request DTO ([LoginRequest]) que contiene las credenciales (email y contraseña).
     * @return [ResponseEntity] con:
     * - HTTP **200 OK** y los datos del usuario autenticado ([UserResponse]) si las credenciales son válidas.
     * - HTTP **401 UNAUTHORIZED** si la autenticación falla (credenciales incorrectas o usuario inactivo).
     * - HTTP **500 INTERNAL_SERVER_ERROR** en caso de error inesperado.
     */
    @PostMapping("/login")
    fun loginUser(@RequestBody request: LoginRequest): ResponseEntity<*> {
        return try {
            // Llama al servicio para verificar credenciales y obtener el objeto User.
            val user = authService.login(request.email, request.password)

            if (user != null) {
                // Autenticación exitosa. Retorna 200 OK.
                // 🌟 Mapear la Entidad User al DTO de Respuesta ([UserResponse]) para no exponer la contraseña hash.
                ResponseEntity(UserResponse(user), HttpStatus.OK)
            } else {
                // Usuario no encontrado o contraseña incorrecta. Retorna 401 UNAUTHORIZED.
                ResponseEntity("Credenciales inválidas.", HttpStatus.UNAUTHORIZED)
            }
        } catch (e: Exception) {
            // Manejo de excepciones genéricas.
            ResponseEntity("Error interno del servidor.", HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
}
