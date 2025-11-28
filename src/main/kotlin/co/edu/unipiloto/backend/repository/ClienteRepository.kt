package co.edu.unipiloto.backend.repository

import co.edu.unipiloto.backend.model.Cliente
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface ClienteRepository : JpaRepository<Cliente, Long> {

    fun findByNombreContainingIgnoreCase(nombre: String): List<Cliente>

    fun findByNumeroId(numeroId: String): Cliente?

    fun existsByNumeroId(numeroId: String): Boolean

    fun findByTipoIdAndNumeroId(tipoId: String, numeroId: String): Cliente?

    // ---------------------------------------
    // 🔥 MÉTODOS RECOMENDADOS NUEVOS
    // ---------------------------------------

    // Buscar por nombre o identificación (para buscador)
    @Query(
        """
        SELECT c FROM Cliente c
        WHERE LOWER(c.nombre) LIKE LOWER(CONCAT('%', :filtro, '%'))
           OR LOWER(c.numeroId) LIKE LOWER(CONCAT('%', :filtro, '%'))
    """
    )
    fun buscarClientes(filtro: String): List<Cliente>

    // Cantidad de solicitudes donde es remitente
    @Query(
        """
        SELECT COUNT(s) FROM Solicitud s
        WHERE s.remitente.id = :clienteId
    """
    )
    fun countSolicitudesComoRemitente(clienteId: Long): Long

    // Cantidad de solicitudes donde es receptor
    @Query(
        """
        SELECT COUNT(s) FROM Solicitud s
        WHERE s.receptor.id = :clienteId
    """
    )
    fun countSolicitudesComoReceptor(clienteId: Long): Long
}
