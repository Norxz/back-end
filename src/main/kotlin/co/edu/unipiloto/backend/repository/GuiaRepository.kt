package co.edu.unipiloto.backend.repository

import co.edu.unipiloto.backend.model.Guia
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * Repositorio para la entidad [Guia] (Guía de Envío).
 * Proporciona métodos para CRUD y consultas personalizadas sobre guías.
 */
@Repository
interface GuiaRepository : JpaRepository<Guia, Long> {

    /**
     * Busca una guía específica por su número de guía único.
     * Retorna [Guia] si es encontrada, o null si no existe.
     *
     * @param numeroGuia El número de guía a buscar.
     * @return La entidad [Guia] correspondiente, o null.
     */
    fun findByNumeroGuia(numeroGuia: String): Guia?

    /**
     * Busca una guía específica por su número de seguimiento (tracking number) único.
     * Retorna [Guia] si es encontrada, o null si no existe.
     *
     * @param trackingNumber El número de seguimiento a buscar.
     * @return La entidad [Guia] correspondiente, o null.
     */
    fun findByTrackingNumber(trackingNumber: String): Guia?

    /**
     * Verifica si ya existe una guía con el número de guía proporcionado.
     *
     * @param numeroGuia El número de guía a verificar.
     * @return true si una guía con ese número ya existe, false en caso contrario.
     */
    fun existsByNumeroGuia(numeroGuia: String): Boolean
}