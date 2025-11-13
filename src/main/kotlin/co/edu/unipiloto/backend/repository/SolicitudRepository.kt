package co.edu.unipiloto.backend.repository

import co.edu.unipiloto.backend.model.Solicitud
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SolicitudRepository : JpaRepository<Solicitud, Long> {

    // ✅ Cambiado: usa la relación 'cliente' y el campo 'id' de esa entidad
    fun findAllByCliente_Id(clienteId: Long): List<Solicitud>

    // Método para obtener solicitudes por estado (Activas/Pendientes)
    fun findAllByEstado(estado: String): List<Solicitud>
}
