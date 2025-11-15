package co.edu.unipiloto.backend.repository

import co.edu.unipiloto.backend.model.Cliente
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ClienteRepository : JpaRepository<Cliente, Long> {

    fun findByNombreContainingIgnoreCase(nombre: String): List<Cliente>

    fun findByNumeroId(numeroId: String): Cliente?

    fun existsByNumeroId(numeroId: String): Boolean

    fun findByTipoIdAndNumeroId(tipoId: String, numeroId: String): Cliente?
}
