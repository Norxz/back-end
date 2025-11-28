package co.edu.unipiloto.backend.repository

import co.edu.unipiloto.backend.model.Sucursal
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SucursalRepository : JpaRepository<Sucursal, Long> {

    // Buscar por nombre exacto
    fun findByNombre(nombre: String): Sucursal?

    // Buscar por ciudad
    fun findAllByCiudad(ciudad: String): List<Sucursal>

    // Validar si ya existe una sucursal con ese nombre
    fun existsByNombre(nombre: String): Boolean
}
