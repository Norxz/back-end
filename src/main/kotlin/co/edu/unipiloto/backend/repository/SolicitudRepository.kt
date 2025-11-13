package co.edu.unipiloto.backend.repository

import co.edu.unipiloto.backend.model.Solicitud
import org.springframework.data.jpa.repository.JpaRepository

interface SolicitudRepository : JpaRepository<Solicitud, Long> {

    // Método para obtener las solicitudes de un cliente específico (como en tu UI de Android)
    fun findAllByClientId(clientId: Long): List<Solicitud>

    // Método para obtener solicitudes por estado (Activas/Pendientes)
    fun findAllByEstado(estado: String): List<Solicitud>
}