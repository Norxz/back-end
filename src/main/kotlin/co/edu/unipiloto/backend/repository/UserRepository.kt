package co.edu.unipiloto.backend.repository

import co.edu.unipiloto.backend.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * Repositorio para la entidad [User] (Usuario del Sistema).
 * Proporciona métodos para operaciones CRUD y consultas específicas relacionadas
 * con la autenticación, roles y estado de los usuarios.
 */
@Repository
interface UserRepository : JpaRepository<User, Long> {

    // --- Consultas de Autenticación y Búsqueda por Credenciales ---

    /**
     * Busca un usuario por su dirección de correo electrónico única.
     * Utilizado comúnmente durante el proceso de inicio de sesión o validación de existencia.
     *
     * @param email La dirección de correo electrónico del usuario.
     * @return La entidad [User] si se encuentra, o null.
     */
    fun findByEmail(email: String): User?

    /**
     * Busca un usuario para propósitos de autenticación, coincidiendo tanto el email como el hash de la contraseña.
     *
     * @param email La dirección de correo electrónico del usuario.
     * @param passwordHash El hash de la contraseña almacenada.
     * @return La entidad [User] si las credenciales coinciden, o null.
     */
    fun findByEmailAndPasswordHash(email: String, passwordHash: String): User?

    // --- Consultas por Rol y Estado ---

    /**
     * Busca y recupera todos los usuarios que tienen un rol específico.
     *
     * @param role El rol por el cual filtrar (ej: "ADMIN", "CONDUCTOR", "CLIENTE").
     * @return Una lista de entidades [User] con el rol especificado.
     */
    fun findByRole(role: String): List<User>

    /**
     * Busca y recupera todos los usuarios que tienen un rol específico y se encuentran activos.
     *
     * @param role El rol por el cual filtrar.
     * @param isActive Indica si el usuario debe estar activo (por defecto, true).
     * @return Una lista de entidades [User] activas con el rol especificado.
     */
    fun findByRoleAndIsActive(role: String, isActive: Boolean = true): List<User>

    /**
     * Busca todos los usuarios activos cuyos roles **no** están incluidos en la lista proporcionada.
     * Utilizado para obtener personal logístico excluyendo, por ejemplo, roles de administrador o cliente.
     *
     * @param roles Una lista de roles a excluir.
     * @param isActive Indica si el usuario debe estar activo (por defecto, true).
     * @return Una lista de entidades [User] activas con roles que no están en la lista de exclusión.
     */
    fun findByRoleNotInAndIsActive(roles: List<String>, isActive: Boolean = true): List<User>

    /**
     * Busca todos los usuarios activos asociados a una sucursal específica y con un rol determinado.
     *
     * @param sucursalId El ID de la sucursal a la que pertenece el usuario.
     * @param role El rol específico del usuario dentro de la sucursal.
     * @param isActive Indica si el usuario debe estar activo (por defecto, true).
     * @return Una lista de entidades [User] que cumplen con todos los criterios.
     */
    fun findFirstBySucursalIdAndRoleAndIsActive(
        sucursalId: Long,
        role: String,
        isActive: Boolean
    ): User?

    /**
     * Verifica si ya existe un usuario registrado con la dirección de correo electrónico proporcionada.
     *
     * @param email La dirección de correo electrónico a verificar.
     * @return true si un usuario con ese email ya existe, false en caso contrario.
     */
    fun existsByEmail(email: String): Boolean
}