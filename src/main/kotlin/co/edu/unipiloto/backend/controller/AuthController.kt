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
 * Controlador para la autenticación de usuarios.
 *
 * Expone endpoints para:
 *  - Registro de nuevos usuarios
 *  - Login de usuarios existentes
 */
@RestController
@RequestMapping("/api/v1/auth")
class AuthController(private val authService: AuthService) {

    /**
     * Endpoint para registrar un nuevo usuario.
     *
     * @param request DTO con la información de registro (nombre, email, contraseña, etc.)
     * @return [ResponseEntity] con el usuario registrado o mensaje de error
     *         - HTTP 201 CREATED si se registró correctamente
     *         - HTTP 409 CONFLICT si el usuario ya existe
     *         - HTTP 500 INTERNAL_SERVER_ERROR en caso de error inesperado
     */
    @PostMapping("/register")
    fun registerUser(@RequestBody request: RegisterRequest): ResponseEntity<*> {
        return try {
            val newUser = authService.register(request)
            ResponseEntity(UserResponse(newUser), HttpStatus.CREATED)
        } catch (e: ResourceAlreadyExistsException) {
            ResponseEntity(e.message, HttpStatus.CONFLICT)
        } catch (e: Exception) {
            ResponseEntity("Error interno del servidor.", HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    /**
     * Endpoint para iniciar sesión de un usuario existente.
     *
     * @param request DTO con email y contraseña del usuario
     * @return [ResponseEntity] con el usuario autenticado o mensaje de error
     *         - HTTP 200 OK si el login es exitoso
     *         - HTTP 401 UNAUTHORIZED si las credenciales son incorrectas
     *         - HTTP 500 INTERNAL_SERVER_ERROR en caso de error inesperado
     */
    @PostMapping("/login")
    fun loginUser(@RequestBody request: LoginRequest): ResponseEntity<*> {
        return try {
            val user = authService.login(request.email, request.password)

            if (user != null) {
                // 🌟 Mapear la Entidad User al DTO de Respuesta
                ResponseEntity(UserResponse(user), HttpStatus.OK)
            } else {
                ResponseEntity("Credenciales inválidas.", HttpStatus.UNAUTHORIZED)
            }
        } catch (e: Exception) {
            ResponseEntity("Error interno del servidor.", HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
}
