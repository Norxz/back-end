package co.edu.unipiloto.backend.controller

import co.edu.unipiloto.backend.dto.UserResponse
import co.edu.unipiloto.backend.exception.ResourceNotFoundException
import co.edu.unipiloto.backend.model.User
import co.edu.unipiloto.backend.model.enums.Role
import co.edu.unipiloto.backend.repository.UserRepository
import co.edu.unipiloto.backend.repository.SucursalRepository
import co.edu.unipiloto.backend.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 * Controlador REST para la gestión de usuarios del sistema.
 * Permite asignar sucursales, listar usuarios por rol, eliminar y desactivar usuarios.
 */
@RestController
@RequestMapping("/api/v1/users")
class UserController(
    private val userRepository: UserRepository,
    private val sucursalRepository: SucursalRepository,
    private val userService: UserService
) {

    // --------------------------------------
    // 1. Asignar sucursal a un usuario
    // --------------------------------------

    /**
     * Asigna una sucursal existente a un usuario.
     *
     * @param userId ID del usuario a actualizar.
     * @param sucursalId ID de la sucursal a asignar.
     * @return Usuario actualizado como [UserResponse], o 404 si no se encuentra el usuario o la sucursal.
     */
    @PutMapping("/{userId}/sucursal/{sucursalId}")
    fun asignarSucursal(
        @PathVariable userId: Long,
        @PathVariable sucursalId: Long
    ): ResponseEntity<UserResponse> {
        val user = userRepository.findById(userId).orElse(null) ?: return ResponseEntity.notFound().build()
        val sucursal = sucursalRepository.findById(sucursalId).orElse(null) ?: return ResponseEntity.notFound().build()
        val actualizado = user.copy(sucursal = sucursal)
        return ResponseEntity.ok(UserResponse(userRepository.save(actualizado)))
    }

    // --------------------------------------
    // 2. Listar usuarios logísticos activos
    // --------------------------------------

    /**
     * Obtiene la lista de todos los usuarios logísticos activos (excluye ADMIN y CLIENTE).
     *
     * @return Lista de [UserResponse] de usuarios logísticos activos.
     */
    @GetMapping("/logistic")
    fun getLogisticUsers(): ResponseEntity<List<UserResponse>> {
        val logistic = userRepository.findAll()
            .filter { it.role != Role.ADMIN && it.role != Role.CLIENTE && it.isActive }
            .map { UserResponse(it) }
        return ResponseEntity.ok(logistic)
    }

    // --------------------------------------
    // 3. Listar gestores por sucursal
    // --------------------------------------

    /**
     * Obtiene todos los usuarios con rol GESTOR de una sucursal específica.
     *
     * @param sucursalId ID de la sucursal.
     * @return Lista de [UserResponse] de gestores activos de la sucursal.
     */
    @GetMapping("/gestores/sucursal/{sucursalId}")
    fun getGestoresBySucursal(@PathVariable sucursalId: Long): ResponseEntity<List<UserResponse>> {
        val gestores = userRepository.findAll()
            .filter { it.sucursal?.id == sucursalId && it.role == Role.GESTOR && it.isActive }
            .map { UserResponse(it) }
        return ResponseEntity.ok(gestores)
    }

    // --------------------------------------
    // 4. Listar conductores por sucursal
    // --------------------------------------

    /**
     * Obtiene todos los usuarios con rol CONDUCTOR de una sucursal específica.
     *
     * @param sucursalId ID de la sucursal.
     * @return Lista de [UserResponse] de conductores activos de la sucursal.
     */
    @GetMapping("/conductores/sucursal/{sucursalId}")
    fun getConductoresBySucursal(@PathVariable sucursalId: Long): ResponseEntity<List<UserResponse>> {
        val conductores = userRepository.findAll()
            .filter { it.sucursal?.id == sucursalId && it.role == Role.CONDUCTOR && it.isActive }
            .map { UserResponse(it) }
        return ResponseEntity.ok(conductores)
    }

    // --------------------------------------
    // 5. Eliminar usuario
    // --------------------------------------

    /**
     * Elimina permanentemente un usuario.
     *
     * @param userId ID del usuario a eliminar.
     * @return 204 No Content si se elimina, 404 Not Found si no existe.
     */
    @DeleteMapping("/{userId}")
    fun eliminarUsuario(@PathVariable userId: Long): ResponseEntity<Void> {
        val user = userRepository.findById(userId).orElse(null) ?: return ResponseEntity.notFound().build()
        userRepository.delete(user)
        return ResponseEntity.noContent().build()
    }

    // --------------------------------------
    // 6. Desactivar usuario
    // --------------------------------------

    /**
     * Desactiva un usuario, marcando su campo [User.isActive] como false.
     *
     * @param userId ID del usuario a desactivar.
     * @return Usuario desactivado como [UserResponse], o 404 si no se encuentra.
     */
    @PutMapping("/{userId}/desactivar")
    fun desactivarUsuario(@PathVariable userId: Long): ResponseEntity<UserResponse> {
        val user = userRepository.findById(userId).orElse(null) ?: return ResponseEntity.notFound().build()
        val actualizado = user.copy(isActive = false)
        return ResponseEntity.ok(UserResponse(userRepository.save(actualizado)))
    }

    /**
     * Busca el primer conductor disponible asignado a una sucursal específica.
     * Mapea a: GET /api/v1/users/drivers/available?sucursalId=X
     */
    @GetMapping("/drivers/available") // ⬅️ SIN {ID} EN LA RUTA
    fun getAvailableDriverBySucursal(
        @RequestParam sucursalId: Long // ⬅️ USAMOS @RequestParam para el query parameter
    ): ResponseEntity<UserResponse> {
        // Asumiendo que existe un método en UserService
        val conductor = userService.findAvailableDriverBySucursal(sucursalId)
            ?: throw ResourceNotFoundException("No se encontró un conductor disponible para la sucursal ID $sucursalId")

        // Retornar el DTO de respuesta del User/Conductor
        return ResponseEntity.ok(UserResponse(conductor))
    }
}
