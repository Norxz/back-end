package co.edu.unipiloto.backend.service

import co.edu.unipiloto.backend.exception.ResourceNotFoundException
import co.edu.unipiloto.backend.model.Solicitud
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
    private val userRepository: UserRepository, // Inyección del repositorio de Usuarios (para buscar gestores/conductores)
    private val solicitudRepository: SolicitudRepository // Inyección del repositorio de Solicitudes
) {

    /**
     * 👤 Asigna un Gestor específico a una Solicitud de envío.
     *
     * 1. Busca la [Solicitud] y el [Gestor] por sus respectivos IDs. Si alguno no existe,
     * lanza una excepción [ResourceNotFoundException].
     * 2. Establece la relación, asignando el objeto Gestor a la propiedad 'gestor' de la Solicitud.
     * 3. Guarda la Solicitud actualizada en la base de datos dentro de una transacción.
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

        // Asigna la relación
        solicitud.gestor = gestor

        return solicitudRepository.save(solicitud)
    }

    /**
     * 🚚 Asigna un Conductor y simultáneamente registra al Gestor que realiza dicha asignación
     * a una Solicitud específica.
     *
     * 1. Busca la [Solicitud], el [Gestor] y el [Conductor] por sus respectivos IDs.
     * 2. Asigna las entidades Conductor y Gestor a las propiedades correspondientes de la Solicitud.
     * 3. Persiste los cambios de la Solicitud en la base de datos.
     *
     * @param solicitudId El ID de la Solicitud.
     * @param gestorId El ID del Gestor que realiza la acción de asignación.
     * @param conductorId El ID del Conductor que transportará la solicitud.
     * @return La entidad [Solicitud] actualizada con el conductor y gestor asignados.
     * @throws ResourceNotFoundException Si alguna de las entidades (Solicitud, Gestor o Conductor) no es encontrada.
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

        return solicitudRepository.save(solicitud)
    }
}