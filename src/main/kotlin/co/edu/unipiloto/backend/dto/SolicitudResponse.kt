package co.edu.unipiloto.backend.dto

import co.edu.unipiloto.backend.model.Solicitud

/**
 * DTO utilizado para enviar una respuesta de Solicitud al frontend.
 * Contiene datos planos y DTOs anidados para simplificar la interfaz.
 */
data class SolicitudResponse(
    val id: Long,
    val estado: String,
    val fechaRecoleccion: String,
    val franjaHoraria: String,
    val tipoServicio: String,
    val observaciones: String?,
    val remitente: UserResponse,
    val origen: DireccionResponse,
    val destino: DireccionResponse,
    val guia: GuiaResponse
)
 {
     constructor(solicitud: Solicitud) : this(
         id = solicitud.id ?: 0,
         estado = solicitud.estado,
         fechaRecoleccion = solicitud.fechaRecoleccion.toString(),
         franjaHoraria = solicitud.franjaHoraria,
         tipoServicio = solicitud.tipoServicio,
         observaciones = solicitud.observaciones,
         remitente = UserResponse(solicitud.cliente),
         origen = DireccionResponse(solicitud.origenDireccion),
         destino = DireccionResponse(solicitud.destinoDireccion),
         guia = GuiaResponse(solicitud.guia)
     )
 }
