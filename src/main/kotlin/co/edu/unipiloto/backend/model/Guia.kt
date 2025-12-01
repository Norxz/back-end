package co.edu.unipiloto.backend.model

import jakarta.persistence.*
import java.time.Instant

/**
 * 🏷️ Entidad JPA que representa la **Guía de Envío** o documento de transporte.
 *
 * Es el registro fundamental para el seguimiento (`tracking`) de un paquete o solicitud.
 * Mapea a la tabla `guias` en la base de datos.
 */
@Entity
@Table(name = "guias")
data class Guia(

    /** 🔑 Identificador único (Primary Key) de la guía en la base de datos. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    /**
     * 🆔 Número interno o correlativo de la guía. **Debe ser único** a nivel de base de datos.
     * Es el identificador usado internamente en la operación.
     */
    @Column(unique = true, nullable = false)
    val numeroGuia: String,

    /**
     * 🌐 Número de seguimiento único (tracking number). **Debe ser único** a nivel de base de datos.
     * Es el código proporcionado al cliente para el rastreo.
     */
    @Column(unique = true, nullable = false)
    val trackingNumber: String,

    /** 🕰️ Marca de tiempo de la creación del registro de la guía. Se inicializa automáticamente. */
    @Column(nullable = false)
    val fechaCreacion: Instant = Instant.now(),

    /** 💵 Costo total del envío asociado a esta guía. Opcional si el cálculo es posterior. */
    @Column(name = "costo_envio")
    val costoEnvio: Double? = null,

    /** Estado actual de la guía (Ej: "CREADA", "EN_TRANSITO"). Usado para el seguimiento. */
    @Column(name = "estado_guia")
    val estadoGuia: String = "CREADA",

    /** 🔄 Marca de tiempo que registra la última modificación o actualización de estado de la guía. */
    @Column(name = "ultima_actualizacion")
    val ultimaActualizacion: Instant = Instant.now()

) {
    /**
     * 🏗️ Constructor vacío requerido por JPA (Hibernate).
     * Proporciona valores por defecto para permitir la instanciación por reflexión.
     */
    constructor() : this(
        id = null,
        numeroGuia = "",
        trackingNumber = "",
        fechaCreacion = Instant.now() // Los valores por defecto se redefinen para consistencia.
    )
}