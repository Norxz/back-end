package co.edu.unipiloto.backend.repository

import co.edu.unipiloto.backend.model.Contacto
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface ContactoRepository : JpaRepository<Contacto, Long> {
    fun findByNumeroIdentificacion(numero: String): Contacto?
}
