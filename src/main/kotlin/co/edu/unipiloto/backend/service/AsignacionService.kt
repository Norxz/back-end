package co.edu.unipiloto.backend.service

import co.edu.unipiloto.backend.exception.ResourceNotFoundException
import co.edu.unipiloto.backend.model.Solicitud
import co.edu.unipiloto.backend.model.enums.EstadoSolicitud
import co.edu.unipiloto.backend.repository.SolicitudRepository
import co.edu.unipiloto.backend.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

/**
 * 🧑‍🤝‍ Servicio encargado de la lógica de negocio para la **asignación de personal**
 * operativo (Gestores y Conductores) a las solicitudes de envío ([Solicitud]).
 *
 * Utiliza transacciones (`@Transactional`) para asegurar la **integridad de los datos**
 * durante la modificación de las asignaciones y el estado de la solicitud.
 */
@Service
class AsignacionService(
    private val userRepository: UserRepository,
    private val solicitudRepository: SolicitudRepository
) {

    /**
     * 👤 Asigna un **Gestor** específico a una Solicitud de envío.
     *
     * Este método se utiliza típicamente en la primera fase de gestión de una solicitud PENDIENTE.
     *
     * 1. 🔍 Busca y valida la existencia de la [Solicitud] y el [Gestor].
     * 2. 🚦 Cambia el estado a [EstadoSolicitud.ASIGNADA].
     * 3. 💾 Persiste la [Solicitud] actualizada.
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

        // Establece el Gestor
        solicitud.gestor = gestor

        // Cambia el estado para indicar que la gestión ha sido iniciada por un responsable.
        solicitud.estado = EstadoSolicitud.ASIGNADA

        return solicitudRepository.save(solicitud)
    }

    /**
     * 🚚 Asigna un **Conductor** y registra al **Gestor** que realiza dicha asignación
     * a una Solicitud específica.
     *
     * Este método se utiliza cuando el gestor asigna la solicitud a un conductor para la ruta de recolección.
     *
     * 1. 🔍 Busca y valida la existencia de la [Solicitud], el [Gestor] y el [Conductor].
     * 2. 🚦 Cambia el estado a [EstadoSolicitud.ASIGNADA] (o se podría cambiar a EN_RUTA_RECOLECCION, dependiendo del flujo exacto).
     * 3. 💾 Persiste los cambios de la Solicitud.
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

        // Asigna el conductor y registra al gestor que hizo la asignación
        solicitud.conductor = conductor
        solicitud.gestor = gestor

        // Actualiza el estado y la fecha de asignación del conductor
        solicitud.estado = EstadoSolicitud.ASIGNADA // El estado de ruta lo cambiará el conductor
        // solicitud.fechaAsignacionConductor = Instant.now() // Si se necesitara actualizar el Instant

        return solicitudRepository.save(solicitud)
    }

    /**
     * 🚛 Asigna (o reasigna) un **Recolector/Conductor** a una Solicitud,
     * basado únicamente en el ID de la solicitud y el ID del recolector.
     *
     * Este es un método simplificado, a menudo utilizado por la aplicación móvil del conductor o un sistema automatizado.
     *
     * 1. 🔍 Busca y valida la [Solicitud] y el [Recolector] (Usuario con rol [Role.CONDUCTOR]).
     * 2. 🛑 Establece el Recolector (campo `conductor`) y cambia el estado a [EstadoSolicitud.ASIGNADA].
     * 3. 💾 Guarda la Solicitud actualizada.
     *
     * @param solicitudId El ID de la Solicitud.
     * @param recolectorId El ID del usuario que actuará como recolector/conductor.
     * @return La entidad [Solicitud] actualizada.
     * @throws ResourceNotFoundException Si la Solicitud o el Recolector no son encontrados.
     */
    @Transactional
    fun asignarRecolectorASolicitud(solicitudId: Long, recolectorId: Long): Solicitud {
        val solicitud = solicitudRepository.findById(solicitudId)
            .orElseThrow { ResourceNotFoundException("Solicitud con ID $solicitudId no encontrada") }

        val recolector = userRepository.findById(recolectorId)
            .orElseThrow { ResourceNotFoundException("Recolector/Conductor con ID $recolectorId no encontrado") }

        // Asigna el conductor (recolector)
        solicitud.conductor = recolector
        solicitud.estado = EstadoSolicitud.ASIGNADA // Se marca como asignada

        return solicitudRepository.save(solicitud)
    }
}