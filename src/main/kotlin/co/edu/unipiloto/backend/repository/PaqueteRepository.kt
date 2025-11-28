package co.edu.unipiloto.backend.repository

import co.edu.unipiloto.backend.model.Paquete
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PaqueteRepository : JpaRepository<Paquete, Long> {

    fun findByCategoria(categoria: String): List<Paquete>

    fun findByPesoGreaterThan(peso: Double): List<Paquete>

    fun findByPesoBetween(min: Double, max: Double): List<Paquete>
}
