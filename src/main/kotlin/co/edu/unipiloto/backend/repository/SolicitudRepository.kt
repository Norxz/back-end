package co.edu.unipiloto.backend.repository

import co.edu.unipiloto.backend.model.Solicitud
import co.edu.unipiloto.backend.model.enums.EstadoSolicitud
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.Instant

@Repository
interface SolicitudRepository : JpaRepository<Solicitud, Long> {

    // Solicitudes creadas por el usuario (cliente del sistema)
    fun findAllByClientId(clientId: Long): List<Solicitud>

    // Filtrar por estado (CORRECTO)
    fun findAllByEstado(estado: EstadoSolicitud): List<Solicitud>

    // Buscar por remitente
    fun findAllByRemitenteId(remitenteId: Long): List<Solicitud>

    // Buscar por receptor
    fun findAllByReceptorId(receptorId: Long): List<Solicitud>

    // Buscar por guía
    fun findByGuiaId(guiaId: Long): Solicitud?

    // Buscar por número de guía
    fun findByGuia_NumeroGuia(numeroGuia: String): Solicitud?

    // Buscar por tracking interno
    fun findByGuia_TrackingNumber(trackingNumber: String): Solicitud?

    // Reportes por rango de fechas
    fun findAllByCreatedAtBetween(start: Instant, end: Instant): List<Solicitud>

    fun findAllBySucursalId(sucursalId: Long): List<Solicitud>

    fun findAllByConductorId(conductorId: Long): List<Solicitud>

    fun findAllByGestorId(gestorId: Long): List<Solicitud>

    fun findAllByFuncionarioId(funcionarioId: Long): List<Solicitud>
}
