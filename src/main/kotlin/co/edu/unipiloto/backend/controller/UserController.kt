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
 * 👥 Controlador REST para la gestión integral de usuarios del sistema.
 *
 * Mapea a la ruta base: `/api/v1/users`
 *
 * Expone endpoints para:
 * - Asignación de sucursales.
 * - Listado de usuarios por rol (Gestores, Conductores) y estado activo.
 * - Gestión del ciclo de vida (activación, desactivación, eliminación).
 * - Búsqueda de recursos logísticos disponibles (Conductores).
 *
 * @property userRepository Repositorio para operaciones directas en la entidad [User].
 * @property sucursalRepository Repositorio para buscar entidades [Sucursal] para asignación.
 * @property userService Servicio con la lógica de negocio para la gestión avanzada de usuarios.
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
     * 🏢 Asigna una sucursal existente a un usuario logístico (Gestor, Conductor, Funcionario).
     *
     * Mapea a: `PUT /api/v1/users/{userId}/sucursal/{sucursalId}`
     *
     * @param userId ID del usuario a actualizar.
     * @param sucursalId ID de la sucursal a asignar.
     * @return [ResponseEntity] con el usuario actualizado ([UserResponse]) y HTTP 200 OK,
     * o HTTP **404 Not Found** si no se encuentra el usuario o la sucursal.
     */
    @PutMapping("/{userId}/sucursal/{sucursalId}")
    fun asignarSucursal(
        @PathVariable userId: Long,
        @PathVariable sucursalId: Long
    ): ResponseEntity<UserResponse> {
        // 1. Busca el usuario. Si no existe, retorna 404.
        val user = userRepository.findById(userId).orElse(null) ?: return ResponseEntity.notFound().build()
        // 2. Busca la sucursal. Si no existe, retorna 404.
        val sucursal = sucursalRepository.findById(sucursalId).orElse(null) ?: return ResponseEntity.notFound().build()

        // 3. Crea una copia inmutable del usuario con la sucursal asignada.
        val actualizado = user.copy(sucursal = sucursal)

        // 4. Guarda y retorna el DTO de respuesta.
        return ResponseEntity.ok(UserResponse(userRepository.save(actualizado)))
    }

    // --------------------------------------
    // 2. Listar usuarios logísticos activos
    // --------------------------------------

    /**
     * ⚙️ Obtiene la lista de todos los usuarios activos que participan en la operación logística
     * (excluye [Role.ADMIN] y [Role.CLIENTE]).
     *
     * Mapea a: `GET /api/v1/users/logistic`
     *
     * @return Lista de [UserResponse] de usuarios logísticos activos.
     */
    @GetMapping("/logistic")
    fun getLogisticUsers(): ResponseEntity<List<UserResponse>> {
        // Filtra todos los usuarios por roles logísticos y estado activo.
        val logistic = userRepository.findAll()
            .filter { it.role != Role.ADMIN && it.role != Role.CLIENTE && it.isActive }
            .map { UserResponse(it) }
        return ResponseEntity.ok(logistic)
    }

    // --------------------------------------
    // 3. Listar gestores por sucursal
    // --------------------------------------

    /**
     * 🧑‍💼 Obtiene todos los usuarios con rol [Role.GESTOR] activos de una sucursal específica.
     *
     * Mapea a: `GET /api/v1/users/gestores/sucursal/{sucursalId}`
     *
     * @param sucursalId ID de la sucursal.
     * @return Lista de [UserResponse] de gestores activos de la sucursal.
     */
    @GetMapping("/gestores/sucursal/{sucursalId}")
    fun getGestoresBySucursal(@PathVariable sucursalId: Long): ResponseEntity<List<UserResponse>> {
        // Filtra por sucursal, rol GESTOR y estado activo.
        val gestores = userRepository.findAll()
            .filter { it.sucursal?.id == sucursalId && it.role == Role.GESTOR && it.isActive }
            .map { UserResponse(it) }
        return ResponseEntity.ok(gestores)
    }

    // --------------------------------------
    // 4. Listar conductores por sucursal
    // --------------------------------------

    /**
     * 🚛 Obtiene todos los usuarios con rol [Role.CONDUCTOR] activos de una sucursal específica.
     *
     * Mapea a: `GET /api/v1/users/conductores/sucursal/{sucursalId}`
     *
     * @param sucursalId ID de la sucursal.
     * @return Lista de [UserResponse] de conductores activos de la sucursal.
     */
    @GetMapping("/conductores/sucursal/{sucursalId}")
    fun getConductoresBySucursal(@PathVariable sucursalId: Long): ResponseEntity<List<UserResponse>> {
        // Filtra por sucursal, rol CONDUCTOR y estado activo.
        val conductores = userRepository.findAll()
            .filter { it.sucursal?.id == sucursalId && it.role == Role.CONDUCTOR && it.isActive }
            .map { UserResponse(it) }
        return ResponseEntity.ok(conductores)
    }

    // --------------------------------------
    // 5. Eliminar usuario
    // --------------------------------------

    /**
     * ⚠️ Elimina permanentemente un usuario del sistema (Hard Delete).
     *
     * Mapea a: `DELETE /api/v1/users/{userId}`
     *
     * @param userId ID del usuario a eliminar.
     * @return HTTP **204 No Content** si se elimina, o HTTP **404 Not Found** si no existe.
     */
    @DeleteMapping("/{userId}")
    fun eliminarUsuario(@PathVariable userId: Long): ResponseEntity<Void> {
        // 1. Busca el usuario. Si no existe, retorna 404.
        val user = userRepository.findById(userId).orElse(null) ?: return ResponseEntity.notFound().build()
        // 2. Elimina la entidad.
        userRepository.delete(user)
        // 3. Retorna 204 No Content para indicar una eliminación exitosa sin cuerpo de respuesta.
        return ResponseEntity.noContent().build()
    }

    // --------------------------------------
    // 6. Desactivar usuario
    // --------------------------------------

    /**
     * ⛔ Desactiva un usuario, marcando su campo [User.isActive] como `false` (Soft Delete).
     *
     * Mapea a: `PUT /api/v1/users/{userId}/desactivar`
     *
     * @param userId ID del usuario a desactivar.
     * @return Usuario desactivado como [UserResponse] y HTTP 200 OK, o HTTP **404 Not Found** si no se encuentra.
     */
    @PutMapping("/{userId}/desactivar")
    fun desactivarUsuario(@PathVariable userId: Long): ResponseEntity<UserResponse> {
        // 1. Busca el usuario. Si no existe, retorna 404.
        val user = userRepository.findById(userId).orElse(null) ?: return ResponseEntity.notFound().build()

        // 2. Crea una copia inmutable y establece isActive a false.
        val actualizado = user.copy(isActive = false)

        // 3. Guarda y retorna el DTO de respuesta.
        return ResponseEntity.ok(UserResponse(userRepository.save(actualizado)))
    }

    /**
     * 🟢 Busca el primer conductor **disponible** asignado a una sucursal específica.
     *
     * Mapea a: `GET /api/v1/users/drivers/available?sucursalId=X`
     *
     * @param sucursalId ID de la sucursal, pasado como parámetro de consulta.
     * @return [ResponseEntity] con el [UserResponse] del conductor disponible y HTTP 200 OK.
     * @throws ResourceNotFoundException (mapeada a HTTP 404) si no se encuentra ningún conductor disponible.
     */
    @GetMapping("/drivers/available")
    fun getAvailableDriverBySucursal(
        @RequestParam sucursalId: Long // Usa @RequestParam para leer el parámetro de consulta.
    ): ResponseEntity<UserResponse> {
        // Llama al servicio, que contiene la lógica para determinar la "disponibilidad".
        val conductor = userService.findAvailableDriverBySucursal(sucursalId)
            ?: throw ResourceNotFoundException("No se encontró un conductor disponible para la sucursal ID $sucursalId")

        // Retorna el DTO de respuesta del Conductor.
        return ResponseEntity.ok(UserResponse(conductor))
    }
}