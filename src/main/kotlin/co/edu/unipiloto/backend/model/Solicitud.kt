package co.edu.unipiloto.backend.model

import co.edu.unipiloto.backend.model.enums.EstadoSolicitud
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import jakarta.persistence.*
import java.time.Instant

/**
 * Representa una solicitud de envío en el sistema logístico.
 * Incluye información sobre el cliente, remitente, receptor, sucursal,
 * dirección, paquete, guía, asignaciones de personal y estado de la solicitud.
 */
@Entity
@Table(name = "solicitudes")
data class Solicitud(

    /** Identificador único de la solicitud */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    /** Cliente que creó la solicitud */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    val client: User,

    /** Información del remitente */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "remitente_id", nullable = false)
    val remitente: Cliente,

    /** Información del receptor */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "receptor_id", nullable = false)
    val receptor: Cliente,

    /** Sucursal asociada a la solicitud */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sucursal_id", nullable = false)
    @JsonIgnoreProperties("solicitudes")
    val sucursal: Sucursal,

    /** Dirección de entrega */
    @OneToOne(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    @JoinColumn(name = "direccion_id", nullable = false)
    val direccion: Direccion,

    /** Paquete asociado a la solicitud */
    @OneToOne(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    @JoinColumn(name = "paquete_id", nullable = false)
    val paquete: Paquete,

    /** Guía asociada a la solicitud */
    @OneToOne(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    @JoinColumn(name = "guia_id", nullable = false)
    val guia: Guia,

    // ----------------------------
    // Asignaciones de personal
    // ----------------------------

    /** Conductor asignado para recoger o entregar el paquete */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "conductor_id")
    @JsonIgnoreProperties("solicitudes")
    var conductor: User? = null,

    /** Gestor asignado para supervisar la solicitud */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "gestor_id")
    @JsonIgnoreProperties("solicitudes")
    var gestor: User? = null,

    /** Funcionario asignado en la sucursal */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "funcionario_id")
    @JsonIgnoreProperties("solicitudes")
    val funcionario: User? = null,

    /** Fecha en que se asignó el conductor */
    @Column(name = "fecha_asignacion_conductor")
    val fechaAsignacionConductor: Instant? = null,

    /** Fecha real de recolección */
    @Column(name = "fecha_recoleccion_real")
    val fechaRecoleccionReal: Instant? = null,

    /** Fecha real de entrega */
    @Column(name = "fecha_entrega_real")
    val fechaEntregaReal: Instant? = null,

    /** Fecha programada de recolección */
    @Column(nullable = false)
    val fechaRecoleccion: String,

    /** Franja horaria programada para la recolección */
    @Column(nullable = false)
    val franjaHoraria: String,

    /** Estado actual de la solicitud */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var estado: EstadoSolicitud = EstadoSolicitud.PENDIENTE,

    /** Fecha de creación de la solicitud */
    @Column(nullable = false)
    val createdAt: Instant = Instant.now(),

    /** Motivo de cancelación (si aplica) */
    val motivoCancelacion: String? = null

) {
    /**
     * Constructor vacío requerido por JPA.
     * Inicializa los campos obligatorios y opcionales con valores por defecto.
     */
    constructor() : this(
        client = User(),
        remitente = Cliente(nombre = "", numeroId = ""),
        receptor = Cliente(nombre = "", numeroId = ""),
        sucursal = Sucursal(),
        direccion = Direccion(),
        paquete = Paquete(),
        guia = Guia(),
        conductor = null,
        gestor = null,
        funcionario = null,
        fechaAsignacionConductor = null,
        fechaRecoleccionReal = null,
        fechaEntregaReal = null,
        fechaRecoleccion = "",
        franjaHoraria = "",
        estado = EstadoSolicitud.PENDIENTE,
        createdAt = Instant.now(),
        motivoCancelacion = null
    )
}
