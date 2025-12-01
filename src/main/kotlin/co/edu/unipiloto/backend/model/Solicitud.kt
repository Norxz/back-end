package co.edu.unipiloto.backend.model

import co.edu.unipiloto.backend.model.enums.EstadoSolicitud
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import jakarta.persistence.*
import java.time.Instant

/**
 * 📨 Entidad JPA que representa una **Solicitud de Envío** (o de Recolección) en el sistema logístico.
 *
 * Esta es la entidad central del negocio, agrupando todas las partes de la transacción:
 * Quién envía, quién recibe, qué se envía, dónde se recoge/entrega, quién lo gestiona y cuál es su estado actual.
 * Mapea a la tabla `solicitudes` en la base de datos.
 */
@Entity
@Table(name = "solicitudes")
data class Solicitud(

    /** 🔑 Identificador único (Primary Key) de la solicitud. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    // --- RELACIONES OBLIGATORIAS ---

    /** 👤 **Relación ManyToOne:** El usuario ([User]) que originó o creó la solicitud. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    val client: User,

    /** 👥 **Relación ManyToOne:** El cliente ([Cliente]) que actúa como remitente del paquete. */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "remitente_id", nullable = false)
    val remitente: Cliente,

    /** 👥 **Relación ManyToOne:** El cliente ([Cliente]) que actúa como receptor del paquete. */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "receptor_id", nullable = false)
    val receptor: Cliente,

    /** 🏢 **Relación ManyToOne:** La sucursal ([Sucursal]) de origen asignada para gestionar la solicitud. */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sucursal_id", nullable = false)
    @JsonIgnoreProperties("solicitudes")
    val sucursal: Sucursal,

    /** 🗺️ **Relación OneToOne:** La dirección física donde se debe recoger el paquete (origen). **Ahora es opcional (nullable = true)**. */
    @OneToOne(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    @JoinColumn(name = "direccion_recoleccion_id", nullable = true)
    val direccionRecoleccion: Direccion? = null,

    /** 🗺️ **Relación OneToOne:** La dirección física de entrega (destino). **Obligatoria**. */
    @OneToOne(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    @JoinColumn(name = "direccion_entrega_id", nullable = false)
    val direccionEntrega: Direccion,

    /** 📦 **Relación OneToOne:** El paquete ([Paquete]) con sus dimensiones y contenido. **Obligatoria**. */
    @OneToOne(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    @JoinColumn(name = "paquete_id", nullable = false)
    val paquete: Paquete,

    /** 🏷️ **Relación OneToOne:** La guía ([Guia]) de rastreo asociada a la solicitud. **Obligatoria**. */
    @OneToOne(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    @JoinColumn(name = "guia_id", nullable = false)
    val guia: Guia,

    // ----------------------------
    // Asignaciones de personal
    // ----------------------------

    /** 🚚 **Relación ManyToOne:** El conductor ([User]) asignado (recolector/repartidor). Es variable (`var`) para permitir reasignación. */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "conductor_id")
    @JsonIgnoreProperties("solicitudes")
    var conductor: User? = null,

    /** 🧑‍💼 **Relación ManyToOne:** El gestor ([User]) asignado para la supervisión logística. Es variable (`var`) para permitir reasignación. */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "gestor_id")
    @JsonIgnoreProperties("solicitudes")
    var gestor: User? = null,

    /** Funcionario ([User]) que procesó la solicitud (ej. recepcionista de sucursal). */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "funcionario_id")
    @JsonIgnoreProperties("solicitudes")
    val funcionario: User? = null,

    // ----------------------------
    // Fechas y Estados
    // ----------------------------

    /** 🕰️ Fecha en que se asignó un conductor. Nullable hasta la asignación. */
    @Column(name = "fecha_asignacion_conductor")
    val fechaAsignacionConductor: Instant? = null,

    /** 🕰️ Fecha y hora real en que se recogió el paquete. Nullable hasta la recolección. */
    @Column(name = "fecha_recoleccion_real")
    val fechaRecoleccionReal: Instant? = null,

    /** 🕰️ Fecha y hora real en que se entregó el paquete. Nullable hasta la entrega. */
    @Column(name = "fecha_entrega_real")
    val fechaEntregaReal: Instant? = null,

    /** Fecha programada por el cliente para la recolección (String). **Obligatoria**. */
    @Column(nullable = false)
    val fechaRecoleccion: String,

    /** Franja horaria programada para la recolección (String). **Obligatoria**. */
    @Column(nullable = false)
    val franjaHoraria: String,

    /** 🚦 Estado actual de la solicitud ([EstadoSolicitud]). Es variable (`var`) para permitir cambios de estado. */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var estado: EstadoSolicitud = EstadoSolicitud.PENDIENTE,

    /** 🕰️ Marca de tiempo de la creación inicial de la solicitud. Se inicializa automáticamente. */
    @Column(nullable = false)
    val createdAt: Instant = Instant.now(),

    /** Razón por la que la solicitud fue cancelada. Nullable. */
    val motivoCancelacion: String? = null

) {
    /**
     * 🏗️ Constructor vacío requerido por JPA (Hibernate).
     * Proporciona inicialización segura de los campos, respetando las relaciones
     * obligatorias con objetos por defecto o nulos.
     */
    constructor() : this(
        // Inicialización de relaciones obligatorias con objetos dummy o por defecto
        client = User(),
        remitente = Cliente(nombre = "", numeroId = ""),
        receptor = Cliente(nombre = "", numeroId = ""),
        sucursal = Sucursal(),
        direccionRecoleccion = null,
        direccionEntrega = Direccion(),
        paquete = Paquete(),
        guia = Guia(),

        // Inicialización de relaciones opcionales
        conductor = null,
        gestor = null,
        funcionario = null,
        fechaAsignacionConductor = null,
        fechaRecoleccionReal = null,
        fechaEntregaReal = null,

        // Inicialización de campos de datos
        fechaRecoleccion = "",
        franjaHoraria = "",
        estado = EstadoSolicitud.PENDIENTE,
        createdAt = Instant.now(),
        motivoCancelacion = null
    )
}