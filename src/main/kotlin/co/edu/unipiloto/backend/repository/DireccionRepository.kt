package co.edu.unipiloto.backend.repository

import co.edu.unipiloto.backend.model.Direccion
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * Repositorio para la entidad [Direccion].
 * Proporciona métodos para CRUD y consultas personalizadas sobre direcciones.
 */
@Repository
interface DireccionRepository : JpaRepository<Direccion, Long> {

    /**
     * Busca una dirección específica por su dirección completa y ciudad.
     * Retorna null si no existe.
     */
    fun findByDireccionCompletaAndCiudad(
        direccionCompleta: String,
        ciudad: String
    ): Direccion?

    /**
     * Obtiene todas las direcciones que pertenecen a una ciudad específica.
     * La búsqueda ignora mayúsculas/minúsculas.
     */
    fun findByCiudadIgnoreCase(ciudad: String): List<Direccion>

    /**
     * Verifica si existe una dirección con la combinación de dirección completa y ciudad.
     */
    fun existsByDireccionCompletaAndCiudad(direccionCompleta: String, ciudad: String): Boolean

    /**
     * Busca direcciones cuyo barrio contenga el texto dado, ignorando mayúsculas/minúsculas.
     */
    fun findByBarrioContainingIgnoreCase(barrio: String): List<Direccion>
}
