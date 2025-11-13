package co.edu.unipiloto.backend.repository

import co.edu.unipiloto.backend.model.Direccion
import org.springframework.data.jpa.repository.JpaRepository

/**
 * Repositorio para la entidad Direccion.
 * Permite realizar operaciones CRUD sobre la tabla 'direcciones'.
 */
interface DireccionRepository : JpaRepository<Direccion, Long> {


}