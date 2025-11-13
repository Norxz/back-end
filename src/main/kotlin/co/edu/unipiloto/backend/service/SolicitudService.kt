package co.edu.unipiloto.backend.service

import co.edu.unipiloto.backend.dto.SolicitudRequest
import co.edu.unipiloto.backend.exception.ResourceNotFoundException
import co.edu.unipiloto.backend.model.Direccion
import co.edu.unipiloto.backend.model.Guia
import co.edu.unipiloto.backend.model.Solicitud
import co.edu.unipiloto.backend.model.User
import co.edu.unipiloto.backend.repository.DireccionRepository
import co.edu.unipiloto.backend.repository.GuiaRepository
import co.edu.unipiloto.backend.repository.SolicitudRepository
import co.edu.unipiloto.backend.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class SolicitudService(
    private val solicitudRepository: SolicitudRepository,
    private val userRepository: UserRepository,
    private val guiaRepository: GuiaRepository
) {

    /**
     * Crea una nueva solicitud, incluyendo la generación de la Guía y la Dirección asociada.
     * Esta operación es transaccional.
     */
    @Transactional
    fun crearSolicitud(request: SolicitudRequest): Solicitud {

        // 1. Verificar si el cliente existe (Seguridad/Integridad)
        val client: User = userRepository.findById(request.clientId)
            .orElseThrow { ResourceNotFoundException("Cliente con ID ${request.clientId} no encontrado.") }

        // 2. Crear la Entidad Dirección
        val nuevaDireccion = Direccion(
            direccionCompleta = request.direccionCompleta,
            ciudad = request.ciudad,
            latitud = request.latitud,
            longitud = request.longitud,
            pisoApto = request.pisoApto,
            notasEntrega = request.notasEntrega
        )
        // No necesitamos guardar la dirección aquí explícitamente si usamos Cascade.ALL en Solicitud.

        // 3. Crear la Entidad Guía (Generando el tracking number)
        val trackingCode = UUID.randomUUID().toString().substring(0, 10).uppercase()
        val nuevaGuia = Guia(
            numeroGuia = trackingCode.substring(0, 8), // Código corto para UI
            trackingNumber = trackingCode,
            volumenM3 = null // Asumimos que el volumen es null por ahora
        )
        // nuevaGuia = guiaRepository.save(nuevaGuia) // Se guarda por Cascade.ALL

        // 4. Crear la Entidad Solicitud, vinculando las anteriores
        val nuevaSolicitud = Solicitud(
            client = client,
            direccion = nuevaDireccion,
            guia = nuevaGuia,
            fechaRecoleccion = request.fechaRecoleccion,
            franjaHoraria = request.franjaHoraria,
            estado = "PENDIENTE",
            pesoKg = request.pesoKg,
            precio = request.precio
        )

        // 5. Guardar la Solicitud (guarda Guía y Dirección por Cascade.ALL)
        return solicitudRepository.save(nuevaSolicitud)
    }

    fun getSolicitudesByClientId(clientId: Long): List<Solicitud> {
        // Llama al método de Spring Data JPA que definiste en SolicitudRepository
        return solicitudRepository.findAllByClientId(clientId)
    }


    /**
     * Actualiza el estado de una solicitud en la base de datos.
     */
    @Transactional
    fun updateEstado(solicitudId: Long, newState: String): Solicitud {
        val solicitud = solicitudRepository.findById(solicitudId)
            .orElseThrow { ResourceNotFoundException("Solicitud con ID $solicitudId no encontrada.") }

        // Cambia el estado (Ahora es posible porque 'estado' es 'var')
        solicitud.estado = newState

        // Guarda el cambio en la base de datos
        return solicitudRepository.save(solicitud)
    }
}
