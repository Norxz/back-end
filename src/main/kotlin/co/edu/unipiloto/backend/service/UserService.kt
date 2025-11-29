// co.edu.unipiloto.backend.service/UserService.kt

package co.edu.unipiloto.backend.service

import co.edu.unipiloto.backend.model.User
import co.edu.unipiloto.backend.model.enums.Role
import co.edu.unipiloto.backend.repository.UserRepository
import co.edu.unipiloto.backend.repository.SucursalRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val userRepository: UserRepository,
    private val sucursalRepository: SucursalRepository
) {

    // --- Lógica Transaccional (Modificación de datos) ---

    /**
     * Asigna una sucursal existente a un usuario y persiste el cambio.
     * @param userId ID del usuario.
     * @param sucursalId ID de la sucursal.
     * @return El objeto User actualizado, o null si no se encuentran las entidades.
     */
    @Transactional // Asegura que la operación de mutación y guardado sea atómica
    fun asignarSucursal(userId: Long, sucursalId: Long): User? {
        var user = userRepository.findById(userId).orElse(null) ?: return null
        var sucursal = sucursalRepository.findById(sucursalId).orElse(null) ?: return null

        // Mutación directa de la entidad rastreada por Hibernate
        user.sucursal = sucursal

        // El save es opcional aquí si @Transactional está presente,
        // pero se incluye por claridad y seguridad.
        return userRepository.save(user)
    }

    /**
     * Desactiva un usuario, marcando su campo [User.isActive] como false.
     * @param userId ID del usuario.
     * @return El objeto User actualizado, o null si no se encuentra el usuario.
     */
    @Transactional
    fun desactivarUsuario(userId: Long): User? {
        val user = userRepository.findById(userId).orElse(null) ?: return null

        // Mutación directa de la entidad
        user.isActive = false

        return userRepository.save(user)
    }

    // --- Lógica de Consulta (Lectura de datos) ---

    fun getLogisticUsers(): List<User> {
        return userRepository.findAll()
            .filter { it.role != Role.ADMIN && it.role != Role.CLIENTE && it.isActive }
    }

    fun getGestoresBySucursal(sucursalId: Long): List<User> {
        return userRepository.findAll()
            .filter { it.sucursal?.id == sucursalId && it.role == Role.GESTOR && it.isActive }
    }

    fun getConductoresBySucursal(sucursalId: Long): List<User> {
        return userRepository.findAll()
            .filter { it.sucursal?.id == sucursalId && it.role == Role.CONDUCTOR && it.isActive }
    }

    fun findById(userId: Long): User? {
        return userRepository.findById(userId).orElse(null)
    }

    // El método de eliminación es una operación directa del Repository
    fun deleteUser(userId: Long) {
        userRepository.deleteById(userId)
    }

}