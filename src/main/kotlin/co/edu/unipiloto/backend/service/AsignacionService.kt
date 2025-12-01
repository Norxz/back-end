package co.edu.unipiloto.backend.service

import co.edu.unipiloto.backend.exception.ResourceNotFoundException
import co.edu.unipiloto.backend.model.Solicitud
import co.edu.unipiloto.backend.model.enums.EstadoSolicitud
import co.edu.unipiloto.backend.repository.SolicitudRepository
import co.edu.unipiloto.backend.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * 📦 Servicio encargado de la lógica de negocio para la asignación de personal
 * operativo (Gestores y Conductores) a las solicitudes de envío ([Solicitud]).
 *
 * Este servicio utiliza transacciones para asegurar la integridad de los datos
 * durante el proceso de asignación.
 */
@Service
class AsignacionService(
    private val userRepository: UserRepository,
    private val solicitudRepository: SolicitudRepository
) {

    /**
     * 👤 Asigna un Gestor específico a una Solicitud de envío.
     *
     * 1. Busca la [Solicitud] y el [Gestor] por sus respectivos IDs.
     * 2. 🛑 Establece el Gestor y cambia el estado a "ASIGNADA".
     * 3. Guarda la Solicitud actualizada.
     *
     * @param solicitudId El ID de la Solicitud que necesita ser asignada.
     * @param gestorId El ID del Gestor que será responsable de la solicitud.
     * @return La entidad [Solicitud] actualizada y persistida.
     * @throws ResourceNotFoundException Si la Solicitud o el Gestor no son encontrados.
     */
    @Transactional
    fun asignarGestorASolicitud(solicitudId: Long, gestorId: Long): Solicitud {
        val solicitud = solicitudRepository.findById(solicitudId)
            .orElseThrow { ResourceNotFoundException("Solicitud con ID $solicitudId no encontrada") }

        val gestor = userRepository.findById(gestorId)
            .orElseThrow { ResourceNotFoundException("Gestor con ID $gestorId no encontrado") }

        solicitud.estado = EstadoSolicitud.ASIGNADA

        // Asigna la relación
        solicitud.gestor = gestor

        return solicitudRepository.save(solicitud)
    }

    /**
     * 🚚 Asigna un Conductor y simultáneamente registra al Gestor que realiza dicha asignación
     * a una Solicitud específica.
     *
     * 1. Busca la [Solicitud], el [Gestor] y el [Conductor].
     * 2. 🛑 Establece las relaciones y cambia el estado a "ASIGNADA".
     * 3. Persiste los cambios de la Solicitud.
     *
     * @param solicitudId El ID de la Solicitud.
     * @param gestorId El ID del Gestor que realiza la acción de asignación.
     * @param conductorId El ID del Conductor que transportará la solicitud.
     * @return La entidad [Solicitud] actualizada con el conductor y gestor asignados.
     * @throws ResourceNotFoundException Si alguna de las entidades no es encontrada.
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

        solicitud.estado = EstadoSolicitud.ASIGNADA

        // Asigna el conductor y registra al gestor que hizo la asignación
        solicitud.conductor = conductor
        solicitud.gestor = gestor

        return solicitudRepository.save(solicitud)
    }
}