package co.edu.unipiloto.backend.repository

import co.edu.unipiloto.backend.model.User
import co.edu.unipiloto.backend.model.enums.Role
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * 👤 Repositorio de Spring Data JPA para la entidad [User] (Usuario del Sistema).
 *
 * Extiende [JpaRepository] para proporcionar métodos CRUD básicos.
 * Define métodos de consulta derivados cruciales para la **autenticación**, **autorización**
 * (roles) y la gestión de la **asociación a sucursales**.
 */
@Repository
interface UserRepository : JpaRepository<User, Long> {

    // --- Consultas de Autenticación y Búsqueda por Credenciales ---

    /**
     * 📧 Busca un usuario por su **dirección de correo electrónico única**.
     *
     * Utilizado comúnmente durante el proceso de inicio de sesión, recuperación de contraseña o validación de existencia.
     *
     * @param email La dirección de correo electrónico del usuario.
     * @return La entidad [User] si se encuentra, o `null`.
     */
    fun findByEmail(email: String): User?

    /**
     * 🔑 Busca un usuario para propósitos de autenticación, coincidiendo tanto el **email** como el **hash de la contraseña**.
     *
     * Nota: En un sistema real, la verificación del hash se hace generalmente en la capa de servicio
     * por razones de seguridad, pero este método ilustra la capacidad de JPA de consultar por múltiples campos.
     *
     * @param email La dirección de correo electrónico del usuario.
     * @param passwordHash El hash de la contraseña almacenada.
     * @return La entidad [User] si las credenciales coinciden, o `null`.
     */
    fun findByEmailAndPasswordHash(email: String, passwordHash: String): User?

    // --- Consultas por Rol y Estado ---

    /**
     * 👑 Busca y recupera todos los usuarios que tienen un **rol específico** (ej. [Role.CONDUCTOR]).
     *
     * @param role El rol por el cual filtrar (se espera el nombre del enum, Ej: "ADMIN").
     * @return Una lista de entidades [User] con el rol especificado.
     */
    fun findByRole(role: String): List<User>

    /**
     * ✅ Busca y recupera todos los usuarios que tienen un **rol específico** y se encuentran **activos**.
     *
     * @param role El rol por el cual filtrar.
     * @param isActive Indica si el usuario debe estar activo (`true` por defecto).
     * @return Una lista de entidades [User] activas con el rol especificado.
     */
    fun findByRoleAndIsActive(role: String, isActive: Boolean = true): List<User>

    /**
     * ❌ Busca todos los usuarios **activos** cuyos **roles NO están incluidos** en la lista proporcionada.
     *
     * Útil para obtener personal operativo excluyendo, por ejemplo, roles de administrador o cliente.
     *
     * @param roles Una lista de roles a **excluir**.
     * @param isActive Indica si el usuario debe estar activo (`true` por defecto).
     * @return Una lista de entidades [User] activas con roles que no están en la lista de exclusión.
     */
    fun findByRoleNotInAndIsActive(roles: List<String>, isActive: Boolean = true): List<User>

    /**
     * 🏢 Busca el **primer** usuario que cumpla con los criterios de **Sucursal**, **Rol** y **Estado de Actividad**.
     *
     * Útil para encontrar, por ejemplo, al Gestor principal de una sucursal específica.
     *
     * @param sucursalId El ID de la sucursal a la que pertenece el usuario.
     * @param role El rol específico del usuario.
     * @param isActive Indica si el usuario debe estar activo.
     * @return La primera entidad [User] que cumple las condiciones, o `null`.
     */
    fun findFirstBySucursalIdAndRoleAndIsActive(
        sucursalId: Long,
        role: String,
        isActive: Boolean
    ): User?

    /**
     * 🆔 Verifica eficientemente si ya existe un usuario registrado con la **dirección de correo electrónico** proporcionada.
     *
     * Crucial en el proceso de registro para asegurar la unicidad del email.
     *
     * @param email La dirección de correo electrónico a verificar.
     * @return `true` si un usuario con ese email ya existe, `false` en caso contrario.
     */
    fun existsByEmail(email: String): Boolean
}