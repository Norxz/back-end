package co.edu.unipiloto.backend.service

import co.edu.unipiloto.backend.model.User
import co.edu.unipiloto.backend.model.enums.Role
import co.edu.unipiloto.backend.repository.UserRepository
import co.edu.unipiloto.backend.repository.SucursalRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * 👤 Servicio de Spring (`@Service`) encargado de la lógica de negocio para la gestión de [User]s.
 *
 * Se enfoca principalmente en la gestión del personal logístico (Gestores y Conductores)
 * y su relación con las sucursales, además de las operaciones básicas de lectura y mutación.
 */
@Service
class UserService(
    private val userRepository: UserRepository,
    private val sucursalRepository: SucursalRepository
) {

    // -------------------------------------------------------------------------
    // --- Lógica Transaccional (Modificación de datos) ---
    // -------------------------------------------------------------------------

    /**
     * 🏢 Asigna una sucursal existente a un usuario y persiste el cambio.
     *
     * @param userId ID del usuario.
     * @param sucursalId ID de la sucursal.
     * @return El objeto [User] actualizado, o `null` si no se encuentran las entidades.
     */
    @Transactional // Asegura que la operación de mutación y guardado sea atómica
    fun asignarSucursal(userId: Long, sucursalId: Long): User? {
        val user = userRepository.findById(userId).orElse(null) ?: return null
        val sucursal = sucursalRepository.findById(sucursalId).orElse(null) ?: return null

        // Mutación directa de la entidad rastreada por Hibernate
        user.sucursal = sucursal

        // El save es opcional aquí si @Transactional está presente y es una mutación,
        // pero se incluye por claridad y seguridad de la persistencia inmediata.
        return userRepository.save(user)
    }

    /**
     * ❌ Desactiva un usuario, marcando su campo [User.isActive] como `false`.
     * Esto se utiliza para la baja lógica del personal sin eliminar el registro.
     *
     * @param userId ID del usuario.
     * @return El objeto [User] actualizado, o `null` si no se encuentra el usuario.
     */
    @Transactional
    fun desactivarUsuario(userId: Long): User? {
        val user = userRepository.findById(userId).orElse(null) ?: return null

        // Mutación directa de la entidad
        user.isActive = false

        return userRepository.save(user)
    }

    // -------------------------------------------------------------------------
    // --- Lógica de Consulta (Lectura de datos) ---
    // -------------------------------------------------------------------------

    /**
     * ⚙️ Obtiene todos los usuarios que forman parte del **equipo logístico activo**.
     * Excluye a los administradores (`ADMIN`) y clientes (`CLIENTE`).
     *
     * @return Una lista de entidades [User] con rol logístico y activos.
     */
    fun getLogisticUsers(): List<User> {
        // Nota: Esta implementación trae todos los usuarios y luego filtra en memoria,
        // lo cual puede ser ineficiente para grandes volúmenes. Se recomienda un método de repositorio.
        return userRepository.findAll()
            .filter { it.role != Role.ADMIN && it.role != Role.CLIENTE && it.isActive }
    }

    /**
     * 🧑‍💻 Obtiene todos los usuarios con rol **GESTOR** activos en una sucursal específica.
     *
     * @param sucursalId ID de la sucursal.
     * @return Una lista de [User]s que son Gestores de esa sucursal.
     */
    fun getGestoresBySucursal(sucursalId: Long): List<User> {
        return userRepository.findAll()
            .filter { it.sucursal?.id == sucursalId && it.role == Role.GESTOR && it.isActive }
    }

    /**
     * 🚚 Obtiene todos los usuarios con rol **CONDUCTOR** activos en una sucursal específica.
     *
     * @param sucursalId ID de la sucursal.
     * @return Una lista de [User]s que son Conductores de esa sucursal.
     */
    fun getConductoresBySucursal(sucursalId: Long): List<User> {
        return userRepository.findAll()
            .filter { it.sucursal?.id == sucursalId && it.role == Role.CONDUCTOR && it.isActive }
    }

    /**
     * 🔎 Busca un usuario por su ID.
     *
     * @param userId ID del usuario.
     * @return La entidad [User] si es encontrada, o `null`.
     */
    fun findById(userId: Long): User? {
        return userRepository.findById(userId).orElse(null)
    }

    /**
     * 🗑️ Elimina un usuario por su ID de la base de datos (eliminación física).
     *
     * @param userId ID del usuario a eliminar.
     */
    @Transactional
    fun deleteUser(userId: Long) {
        userRepository.deleteById(userId)
    }

    /**
     * 🚛 Busca el **primer conductor disponible** (activo) para trabajar en una sucursal específica.
     *
     * Este método asume que el repositorio tiene una consulta optimizada para encontrar solo uno,
     * lo cual es útil para la asignación automática de tareas.
     *
     * @param sucursalId ID de la sucursal.
     * @return El [User] encontrado, o `null` si no hay conductores activos en esa sucursal.
     */
    fun findAvailableDriverBySucursal(sucursalId: Long): User? {
        // ✅ Se asume que el método del repositorio hace la búsqueda eficiente.
        // Si el rol es un String en el repositorio, la invocación es correcta.
        return userRepository.findFirstBySucursalIdAndRoleAndIsActive(
            sucursalId,
            "CONDUCTOR", // Se pasa el nombre literal del rol
            true         // Buscamos un conductor que esté Activo/Disponible
        )
    }
}