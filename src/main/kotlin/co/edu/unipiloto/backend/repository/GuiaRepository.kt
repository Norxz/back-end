package co.edu.unipiloto.backend.repository

import co.edu.unipiloto.backend.model.Guia
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface GuiaRepository : JpaRepository<Guia, Long> {

    fun findByNumeroGuia(numeroGuia: String): Guia?

    fun findByTrackingNumber(trackingNumber: String): Guia?

    fun existsByNumeroGuia(numeroGuia: String): Boolean
}
