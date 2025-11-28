package co.edu.unipiloto.backend.model

import jakarta.persistence.*
import java.time.Instant

/**
 * Representa una guía de envío asociada a un paquete o solicitud.
 * Contiene información de seguimiento, estado y costos de envío.
 */
@Entity
@Table(name = "guias")
data class Guia(

    /** Identificador único de la guía en la base de datos */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    /** Número único de la guía (ej: "GUIA-000123") */
    @Column(unique = true, nullable = false)
    val numeroGuia: String,

    /** Número de seguimiento único asignado para rastreo */
    @Column(unique = true, nullable = false)
    val trackingNumber: String,

    /** Fecha de creación de la guía */
    @Column(nullable = false)
    val fechaCreacion: Instant = Instant.now(),

    /** Costo del envío asociado a la guía */
    @Column(name = "costo_envio")
    val costoEnvio: Double? = null,

    /** Estado actual de la guía (ej: CREADA, EN_TRANSITO, ENTREGADA) */
    @Column(name = "estado_guia")
    val estadoGuia: String = "CREADA",

    /** Última fecha en que se actualizó la guía */
    @Column(name = "ultima_actualizacion")
    val ultimaActualizacion: Instant = Instant.now()

) {
    /**
     * Constructor vacío requerido por JPA.
     * Inicializa los campos obligatorios con valores por defecto.
     */
    constructor() : this(
        id = null,
        numeroGuia = "",
        trackingNumber = "",
        fechaCreacion = Instant.now()
    )
}
