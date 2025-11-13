package co.edu.unipiloto.backend.repository

import co.edu.unipiloto.backend.model.Guia
import org.springframework.data.jpa.repository.JpaRepository

/**
 * Repositorio para la entidad Guia. Permite realizar operaciones CRUD
 * y búsquedas personalizadas sobre la tabla 'guias'.
 */
interface GuiaRepository : JpaRepository<Guia, Long> {

    // Si quisieras buscar una guía por su número de seguimiento:
    fun findByTrackingNumber(trackingNumber: String): Guia?
}