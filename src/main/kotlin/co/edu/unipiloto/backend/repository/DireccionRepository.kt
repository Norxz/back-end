package co.edu.unipiloto.backend.repository

import co.edu.unipiloto.backend.model.Direccion
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface DireccionRepository : JpaRepository<Direccion, Long> {
    fun findByDireccionCompletaAndCiudad(
        direccionCompleta: String,
        ciudad: String
    ): Direccion?
}
