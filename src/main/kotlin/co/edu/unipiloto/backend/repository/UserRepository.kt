package co.edu.unipiloto.backend.repository

import co.edu.unipiloto.backend.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Long> {

    // Autenticación
    fun findByEmail(email: String): User?

    fun findByEmailAndPasswordHash(email: String, passwordHash: String): User?

    // Búsqueda por rol (ADMIN, CLIENTE, RECOLECTOR, LOGISTICO, etc.)
    fun findByRole(role: String): List<User>

    // Búsqueda por sucursal (especial para listar recolectores/logísticos de una sucursal)
    fun findAllBySucursal(nombreSucursal: String): List<User>

    // Validación de duplicados
    fun existsByEmail(email: String): Boolean
}
