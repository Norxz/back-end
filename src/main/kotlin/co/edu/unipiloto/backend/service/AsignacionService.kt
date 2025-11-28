package co.edu.unipiloto.backend.service

import co.edu.unipiloto.backend.exception.ResourceNotFoundException
import co.edu.unipiloto.backend.model.Solicitud
import co.edu.unipiloto.backend.repository.SolicitudRepository
import co.edu.unipiloto.backend.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AsignacionService(
    private val userRepository: UserRepository,
    private val solicitudRepository: SolicitudRepository // inyectar repositorio de solicitudes
) {

    @Transactional
    fun asignarGestorASolicitud(solicitudId: Long, gestorId: Long): Solicitud {
        val solicitud = solicitudRepository.findById(solicitudId)
            .orElseThrow { ResourceNotFoundException("Solicitud con ID $solicitudId no encontrada") }

        val gestor = userRepository.findById(gestorId)
            .orElseThrow { ResourceNotFoundException("Gestor con ID $gestorId no encontrado") }

        solicitud.gestor = gestor

        return solicitudRepository.save(solicitud)
    }

    /**
     * Asigna un conductor a una solicitud y registra el gestor que lo asigna
     */
    @Transactional
    fun asignarConductorASolicitud(
        solicitudId: Long,
        gestorId: Long,
        conductorId: Long
    ): Solicitud {
        val solicitud = solicitudRepository.findById(solicitudId)
            .orElseThrow { ResourceNotFoundException("Solicitud con ID $solicitudId no encontrada") }

        val gestor = userRepository.findById(gestorId)
            .orElseThrow { ResourceNotFoundException("Gestor con ID $gestorId no encontrado") }

        val conductor = userRepository.findById(conductorId)
            .orElseThrow { ResourceNotFoundException("Conductor con ID $conductorId no encontrado") }

        // Asigna el conductor y el gestor
        solicitud.conductor = conductor
        solicitud.gestor = gestor

        return solicitudRepository.save(solicitud)
    }
}
