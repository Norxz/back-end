package co.edu.unipiloto.backend.repository

import co.edu.unipiloto.backend.model.Direccion
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * 🗺️ Repositorio de Spring Data JPA para la entidad [Direccion].
 *
 * Extiende [JpaRepository] para proporcionar métodos CRUD básicos.
 * Define métodos de consulta derivados para la búsqueda y verificación de ubicaciones.
 */
@Repository
interface DireccionRepository : JpaRepository<Direccion, Long> {

    // --- Métodos de Consulta Derivados ---

    /**
     * 🔍 Busca una dirección específica utilizando la combinación de su **dirección completa**
     * y su **ciudad**.
     *
     * Útil para verificar si una dirección ya existe antes de crear un nuevo registro.
     *
     * @param direccionCompleta La dirección física completa (Ej: Cra 68C #22b-71).
     * @param ciudad Ciudad donde se encuentra la dirección.
     * @return La [Direccion] encontrada o `null` si no existe.
     */
    fun findByDireccionCompletaAndCiudad(
        direccionCompleta: String,
        ciudad: String
    ): Direccion?

    /**
     * Obtiene todas las direcciones que pertenecen a una **ciudad** específica.
     * La búsqueda es **insensible a mayúsculas/minúsculas** (`IgnoreCase`).
     *
     * @param ciudad Ciudad para filtrar.
     * @return Lista de [Direccion] que coinciden con la ciudad.
     */
    fun findByCiudadIgnoreCase(ciudad: String): List<Direccion>

    /**
     * Verifica eficientemente si existe una dirección con la combinación exacta de
     * **dirección completa** y **ciudad**.
     *
     * @param direccionCompleta La dirección física completa.
     * @param ciudad Ciudad.
     * @return `true` si existe, `false` en caso contrario.
     */
    fun existsByDireccionCompletaAndCiudad(direccionCompleta: String, ciudad: String): Boolean

    /**
     * Busca direcciones cuyo **barrio** contenga el texto dado, **ignorando mayúsculas/minúsculas**.
     *
     * @param barrio Fragmento del nombre del barrio.
     * @return Lista de [Direccion] que contienen el fragmento de barrio.
     */
    fun findByBarrioContainingIgnoreCase(barrio: String): List<Direccion>
}