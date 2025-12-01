package co.edu.unipiloto.backend.repository

import co.edu.unipiloto.backend.model.Sucursal
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * 🏢 Repositorio de Spring Data JPA para la entidad [Sucursal] (Oficina o Punto de Servicio).
 *
 * Extiende [JpaRepository] para proporcionar métodos CRUD básicos.
 * Define métodos de consulta derivados necesarios para la búsqueda, filtrado geográfico
 * y verificación de unicidad de las sucursales.
 */
@Repository
interface SucursalRepository : JpaRepository<Sucursal, Long> {

    // --- Consultas por Nombre (Unicidad) ---

    /**
     * 🔍 Busca una sucursal específica por su **nombre exacto**.
     *
     * Dado que el nombre de una sucursal suele ser único, se espera que devuelva una sola entidad.
     *
     * @param nombre El nombre exacto de la sucursal a buscar (Ej: "Sucursal Centro").
     * @return La entidad [Sucursal] correspondiente, o `null` si no se encuentra.
     */
    fun findByNombre(nombre: String): Sucursal?

    // --- Consultas por Relación Anidada (Dirección) ---

    /**
     * 📍 Busca y recupera todas las sucursales ubicadas en una **ciudad específica**.
     *
     * Utiliza la navegación de propiedades de Spring Data JPA para acceder al campo
     * `ciudad` dentro de la entidad anidada `direccion` (`findAllByDireccion_Ciudad`).
     *
     * @param ciudad El nombre de la ciudad por la cual se desea filtrar.
     * @return Una lista de entidades [Sucursal] localizadas en la ciudad dada.
     */
    fun findAllByDireccion_Ciudad(ciudad: String): List<Sucursal>

    // --- Consultas de Existencia ---

    /**
     * 🆔 Verifica eficientemente la existencia de una sucursal utilizando su **nombre** como criterio.
     *
     * @param nombre El nombre de la sucursal a verificar.
     * @return `true` si ya existe una sucursal con ese nombre, `false` en caso contrario.
     */
    fun existsByNombre(nombre: String): Boolean
}