package co.edu.unipiloto.backend.controller

import co.edu.unipiloto.backend.dto.LoginRequest
import co.edu.unipiloto.backend.dto.RegisterRequest
import co.edu.unipiloto.backend.exception.ResourceAlreadyExistsException
import co.edu.unipiloto.backend.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import co.edu.unipiloto.backend.dto.UserResponse
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/auth")
class UserController(private val userService: UserService) {

    // --- REGISTRO ---
    @PostMapping("/register")
    fun registerUser(@RequestBody request: RegisterRequest): ResponseEntity<*> {
        return try {
            val newUser = userService.register(request)
            ResponseEntity(UserResponse(newUser), HttpStatus.CREATED)
        } catch (e: ResourceAlreadyExistsException) {
            ResponseEntity(e.message, HttpStatus.CONFLICT)
        } catch (e: Exception) {
            ResponseEntity("Error interno del servidor.", HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    // --- LOGIN ---
    @PostMapping("/login")
    fun loginUser(@RequestBody request: LoginRequest): ResponseEntity<*> {
        return try {
            val user = userService.login(request.email, request.password)

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