package co.edu.unipiloto.backend.repository

import co.edu.unipiloto.backend.model.Paquete
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * 📦 Repositorio de Spring Data JPA para la entidad [Paquete] (Paquete o Envío).
 *
 * Extiende [JpaRepository] para proporcionar métodos CRUD básicos.
 * Define métodos de consulta derivados cruciales para la clasificación y el análisis de la carga.
 */
@Repository
interface PaqueteRepository : JpaRepository<Paquete, Long> {

    // --- Métodos de Clasificación ---

    /**
     * 🏷️ Busca y recupera todos los paquetes que pertenecen a una **categoría** específica.
     *
     * @param categoria La categoría del paquete (ej: "Frágil", "Electrónica").
     * @return Una lista de entidades [Paquete] que coinciden con la categoría dada.
     */
    fun findByCategoria(categoria: String): List<Paquete>

    // --- Métodos de Consulta por Peso ---

    /**
     * 📈 Busca y recupera todos los paquetes cuyo **peso** sea **estrictamente mayor**
     * que el peso especificado (`GreaterThan`).
     *
     * Útil para filtrar cargas pesadas.
     *
     * @param peso El peso mínimo (no inclusivo) para la búsqueda.
     * @return Una lista de entidades [Paquete] con peso superior al valor dado.
     */
    fun findByPesoGreaterThan(peso: Double): List<Paquete>

    /**
     * ⚖️ Busca y recupera todos los paquetes cuyo **peso** se encuentre **dentro del rango**
     * especificado (ambos valores inclusivos).
     *
     * Útil para tarificación por rangos de peso.
     *
     * @param min El peso mínimo (inclusivo) del rango.
     * @param max El peso máximo (inclusivo) del rango.
     * @return Una lista de entidades [Paquete] cuyo peso esté entre 'min' y 'max'.
     */
    fun findByPesoBetween(min: Double, max: Double): List<Paquete>
}