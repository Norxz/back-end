package co.edu.unipiloto.backend.repository

import co.edu.unipiloto.backend.model.Sucursal
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * Repositorio para la entidad [Sucursal] (Oficina o Punto de Servicio).
 * Proporciona métodos para operaciones CRUD básicas y consultas específicas
 * para la gestión de sucursales en el sistema.
 */
@Repository
interface SucursalRepository : JpaRepository<Sucursal, Long> {

    /**
     * Busca una sucursal específica por su nombre exacto.
     * Dado que el nombre debe ser único, devuelve una sola entidad.
     *
     * @param nombre El nombre exacto de la sucursal a buscar.
     * @return La entidad [Sucursal] correspondiente, o null si no se encuentra.
     */
    fun findByNombre(nombre: String): Sucursal?

    /**
     * Busca y recupera todas las sucursales ubicadas en una ciudad específica.
     *
     * @param ciudad El nombre de la ciudad por la cual se desea filtrar.
     * @return Una lista de entidades [Sucursal] localizadas en la ciudad dada.
     */
    fun findAllByDireccion_Ciudad(ciudad: String): List<Sucursal>

    /**
     * Verifica la existencia de una sucursal utilizando su nombre como criterio.
     *
     * @param nombre El nombre de la sucursal a verificar.
     * @return true si ya existe una sucursal con ese nombre, false en caso contrario.
     */
    fun existsByNombre(nombre: String): Boolean
}