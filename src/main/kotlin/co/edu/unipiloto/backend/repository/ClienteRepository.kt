package co.edu.unipiloto.backend.repository

import co.edu.unipiloto.backend.model.Cliente
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

/**
 * Repositorio para la entidad [Cliente].
 * Proporciona métodos para CRUD y consultas personalizadas sobre clientes.
 */
@Repository
interface ClienteRepository : JpaRepository<Cliente, Long> {

    /**
     * Busca clientes cuyo nombre contenga el texto especificado, ignorando mayúsculas/minúsculas.
     */
    fun findByNombreContainingIgnoreCase(nombre: String): List<Cliente>

    /**
     * Busca un cliente por su número de identificación.
     */
    fun findByNumeroId(numeroId: String): Cliente?

    /**
     * Verifica si existe un cliente con el número de identificación dado.
     */
    fun existsByNumeroId(numeroId: String): Boolean

    /**
     * Busca un cliente por tipo de identificación y número de identificación.
     */
    fun findByTipoIdAndNumeroId(tipoId: String, numeroId: String): Cliente?

    // ---------------------------------------
    // 🔥 MÉTODOS RECOMENDADOS NUEVOS
    // ---------------------------------------

    /**
     * Busca clientes cuyo nombre o número de identificación contenga el filtro dado.
     * Útil para buscadores en UI.
     */
    @Query(
        """
        SELECT c FROM Cliente c
        WHERE LOWER(c.nombre) LIKE LOWER(CONCAT('%', :filtro, '%'))
           OR LOWER(c.numeroId) LIKE LOWER(CONCAT('%', :filtro, '%'))
    """
    )
    fun buscarClientes(filtro: String): List<Cliente>

    /**
     * Cuenta la cantidad de solicitudes en las que el cliente es remitente.
     */
    @Query(
        """
        SELECT COUNT(s) FROM Solicitud s
        WHERE s.remitente.id = :clienteId
    """
    )
    fun countSolicitudesComoRemitente(clienteId: Long): Long

    /**
     * Cuenta la cantidad de solicitudes en las que el cliente es receptor.
     */
    @Query(
        """
        SELECT COUNT(s) FROM Solicitud s
        WHERE s.receptor.id = :clienteId
    """
    )
    fun countSolicitudesComoReceptor(clienteId: Long): Long
}
