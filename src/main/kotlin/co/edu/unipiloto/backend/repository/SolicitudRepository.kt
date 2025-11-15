package co.edu.unipiloto.backend.repository

import co.edu.unipiloto.backend.model.Solicitud
import org.springframework.data.jpa.repository.JpaRepository
import java.time.Instant

interface SolicitudRepository : JpaRepository<Solicitud, Long> {

    // Solicitudes creadas por el usuario (cliente del sistema)
    fun findAllByClientId(clientId: Long): List<Solicitud>

    // Filtrar por estado
    fun findAllByEstado(estado: String): List<Solicitud>

    // Buscar por remitente (Cliente)
    fun findAllByRemitenteId(remitenteId: Long): List<Solicitud>

    // Buscar por receptor (Cliente)
    fun findAllByReceptorId(receptorId: Long): List<Solicitud>

    // Buscar solicitud por id de la guía
    fun findByGuiaId(guiaId: Long): Solicitud?

    // Buscar por número de guía (muy útil para tracking)
    fun findByGuia_NumeroGuia(numeroGuia: String): Solicitud?

    // Buscar por tracking number interno
    fun findByGuia_TrackingNumber(trackingNumber: String): Solicitud?

    // Reportes: buscar entre fechas
    fun findAllByCreatedAtBetween(start: Instant, end: Instant): List<Solicitud>
}
