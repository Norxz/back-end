package co.edu.unipiloto.backend.repository

import co.edu.unipiloto.backend.model.Guia
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * 🏷️ Repositorio de Spring Data JPA para la entidad [Guia] (Guía de Envío).
 *
 * Extiende [JpaRepository] para proporcionar métodos CRUD básicos.
 * Define métodos de consulta derivados necesarios para el rastreo y verificación de unicidad.
 */
@Repository
interface GuiaRepository : JpaRepository<Guia, Long> {

    // --- Métodos de Consulta Derivados por Identificadores Únicos ---

    /**
     * 🔍 Busca una guía específica por su **número de guía** único (identificador interno).
     *
     * @param numeroGuia El número de guía a buscar.
     * @return La entidad [Guia] correspondiente, o `null` si no existe.
     */
    fun findByNumeroGuia(numeroGuia: String): Guia?

    /**
     * 🌐 Busca una guía específica por su **número de seguimiento (tracking number)** único
     * (el código que se le da al cliente).
     *
     * @param trackingNumber El número de seguimiento a buscar.
     * @return La entidad [Guia] correspondiente, o `null`.
     */
    fun findByTrackingNumber(trackingNumber: String): Guia?

    /**
     * 🆔 Verifica eficientemente si ya existe una guía con el **número de guía** proporcionado.
     *
     * Este método es útil en la lógica de negocio para asegurar la unicidad antes de la creación
     * de una nueva guía.
     *
     * @param numeroGuia El número de guía a verificar.
     * @return `true` si una guía con ese número ya existe, `false` en caso contrario.
     */
    fun existsByNumeroGuia(numeroGuia: String): Boolean

    // Nota: Spring Data JPA es inteligente y automáticamente infiere el método `existsByTrackingNumber`
    // si fuera necesario, basándose en la misma lógica.
}