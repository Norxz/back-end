package co.edu.unipiloto.backend.repository

import co.edu.unipiloto.backend.model.Cliente
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

/**
 * 🧑‍🤝‍🧑 Repositorio de Spring Data JPA para la entidad [Cliente].
 *
 * Extiende [JpaRepository] para proporcionar métodos CRUD básicos.
 * Además, define métodos de consulta derivados y personalizados para buscar y contar clientes.
 */
@Repository
interface ClienteRepository : JpaRepository<Cliente, Long> {

    // --- Métodos de Consulta Derivados ---

    /**
     * Busca clientes cuyo **nombre** contenga el texto especificado, ignorando mayúsculas/minúsculas.
     * (Ej: `findByNombreContainingIgnoreCase("juan")` encuentra "Juan Perez" y "juAnito").
     */
    fun findByNombreContainingIgnoreCase(nombre: String): List<Cliente>

    /**
     * Busca un cliente por su **número de identificación** exacto.
     * @param numeroId Número de identificación (ej. CC, NIT).
     * @return El cliente encontrado o `null` si no existe.
     */
    fun findByNumeroId(numeroId: String): Cliente?

    /**
     * Verifica eficientemente si existe un cliente con el **número de identificación** dado.
     * @param numeroId Número de identificación a verificar.
     * @return `true` si existe, `false` en caso contrario.
     */
    fun existsByNumeroId(numeroId: String): Boolean

    /**
     * Busca un cliente por una combinación de **tipo de identificación y número de identificación**.
     * @param tipoId Tipo de identificación (ej. "CC").
     * @param numeroId Número de identificación.
     * @return El cliente encontrado o `null`.
     */
    fun findByTipoIdAndNumeroId(tipoId: String, numeroId: String): Cliente?

    // ---------------------------------------
    // 🔥 Métodos Personalizados (JPQL)
    // ---------------------------------------

    /**
     * 🔎 Busca clientes de forma flexible, comparando el filtro tanto con el
     * **nombre** como con el **número de identificación**, ignorando mayúsculas/minúsculas.
     *
     * Útil para implementar un buscador general en interfaces de usuario.
     *
     * @param filtro Texto de búsqueda.
     * @return Lista de clientes que coinciden con el filtro.
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
     * 🔢 Cuenta la cantidad de registros en la entidad [Solicitud] en las que el cliente
     * (identificado por `clienteId`) figura como **remitente**.
     *
     * @param clienteId ID del cliente.
     * @return El número total de solicitudes donde es remitente.
     */
    @Query(
        """
        SELECT COUNT(s) FROM Solicitud s
        WHERE s.remitente.id = :clienteId
    """
    )
    fun countSolicitudesComoRemitente(clienteId: Long): Long

    /**
     * 🔢 Cuenta la cantidad de registros en la entidad [Solicitud] en las que el cliente
     * (identificado por `clienteId`) figura como **receptor**.
     *
     * @param clienteId ID del cliente.
     * @return El número total de solicitudes donde es receptor.
     */
    @Query(
        """
        SELECT COUNT(s) FROM Solicitud s
        WHERE s.receptor.id = :clienteId
    """
    )
    fun countSolicitudesComoReceptor(clienteId: Long): Long
}