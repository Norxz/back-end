package co.edu.unipiloto.backend.service

import co.edu.unipiloto.backend.dto.ContactoDTO
import co.edu.unipiloto.backend.dto.DireccionDTO
import co.edu.unipiloto.backend.dto.SolicitudRequest
import co.edu.unipiloto.backend.model.*
import co.edu.unipiloto.backend.repository.*
import org.springframework.stereotype.Service
import java.time.LocalDate
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sqrt

@Service
class SolicitudService(
    private val solicitudRepository: SolicitudRepository,
    private val userRepository: UserRepository,
    private val direccionRepository: DireccionRepository,
    private val guiaRepository: GuiaRepository,
    private val contactoService: ContactoService
) {

    /**
     * Crea una nueva solicitud a partir de un DTO.
     */
    fun crearSolicitud(request: SolicitudRequest): Solicitud {
        // 🔹 Buscar cliente
        val cliente = userRepository.findById(request.clienteId)
            .orElseThrow { IllegalArgumentException("Cliente con ID ${request.clienteId} no encontrado") }

        // 🔹 Guardar remitente evitando duplicados
        val remitente = dtoToContacto(request.remitente)
        val remitenteGuardado = contactoService.buscarPorNumeroIdentificacion(remitente.numeroIdentificacion)
            .orElseGet { contactoService.guardar(remitente) }

        // 🔹 Guardar destinatario evitando duplicados
        val destinatario = dtoToContacto(request.destinatario)
        val destinatarioGuardado = contactoService.buscarPorNumeroIdentificacion(destinatario.numeroIdentificacion)
            .orElseGet { contactoService.guardar(destinatario) }

        // 🔹 Guardar direcciones
        val origen = dtoToDireccion(request.origenDireccion)
        val destino = dtoToDireccion(request.destinoDireccion)
        val origenGuardado = direccionRepository.save(origen)
        val destinoGuardado = direccionRepository.save(destino)

        val guia = Guia(
            numeroGuia = generarNumeroGuia(),
            trackingNumber = generarTrackingNumber(),
            remitente = remitenteGuardado,
            destinatario = destinatarioGuardado,
            pesoKg = request.peso ?: 1.0,
            altoCm = request.alto ?: 10.0,
            anchoCm = request.ancho ?: 10.0,
            largoCm = request.largo ?: 10.0,
            contenidoDescripcion = request.contenido ?: "Sin descripción",
            latitudDestino = destino.latitud,
            longitudDestino = destino.longitud
        )


        val guiaGuardada = guiaRepository.save(guia)

        // 🔹 Crear solicitud
        val solicitud = Solicitud(
            cliente = cliente,
            origenDireccion = origenGuardado,
            destinoDireccion = destinoGuardado,
            guia = guiaGuardada,
            fechaRecoleccion = LocalDate.parse(request.fechaRecoleccion),
            franjaHoraria = request.franjaHoraria,
            tipoServicio = "NORMAL",
            observaciones = null // o agregar desde request si quieres
        )

        // 🔹 Calcular precio estimado (solo para logging)
        val precioEstimado = calcularPrecio(origenGuardado, destinoGuardado, solicitud.tipoServicio)
        println("💰 Precio estimado: $precioEstimado")

        return solicitudRepository.save(solicitud)
    }

    // ------------------------ UTILIDADES ------------------------

    private fun dtoToContacto(dto: ContactoDTO) = Contacto(
        nombreCompleto = dto.nombreCompleto,
        tipoIdentificacion = dto.tipoIdentificacion,
        numeroIdentificacion = dto.numeroIdentificacion,
        telefono = dto.telefono
    )

    private fun dtoToDireccion(dto: DireccionDTO) = Direccion(
        direccionCompleta = dto.direccionCompleta,
        ciudad = dto.ciudad,
        latitud = dto.latitud,
        longitud = dto.longitud,
        pisoApto = dto.pisoApto,
        notasEntrega = dto.notasEntrega
    )

    private fun calcularPrecio(origen: Direccion, destino: Direccion, tipoServicio: String): Double {
        val distancia = sqrt(
            ((destino.latitud ?: 0.0) - (origen.latitud ?: 0.0)).pow(2) +
                    ((destino.longitud ?: 0.0) - (origen.longitud ?: 0.0)).pow(2)
        ) * 111.0

        var precioBase = 5000.00 + distancia * 300
        if (tipoServicio.uppercase() == "EXPRESS") precioBase *= 1.3

        return (precioBase * 100).roundToInt() / 100.0
    }

    private fun generarNumeroGuia(): String = "G-${System.currentTimeMillis()}"
    private fun generarTrackingNumber(): String = "TRK-${System.nanoTime()}"

    fun obtenerTodas(): List<Solicitud> = solicitudRepository.findAll()
    fun obtenerPorId(id: Long): Solicitud? = solicitudRepository.findById(id).orElse(null)
    fun eliminarSolicitud(id: Long) {
        if (!solicitudRepository.existsById(id)) throw IllegalArgumentException("La solicitud con ID $id no existe")
        solicitudRepository.deleteById(id)
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
