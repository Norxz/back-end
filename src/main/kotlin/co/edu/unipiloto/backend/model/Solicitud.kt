package co.edu.unipiloto.backend.model

import co.edu.unipiloto.backend.model.enums.EstadoSolicitud
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import jakarta.persistence.*
import java.time.Instant

/**
 * 📨 Entidad JPA que representa una **Solicitud de Envío**.
 */
@Entity
@Table(name = "solicitudes")
data class Solicitud(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    // --- RELACIONES OBLIGATORIAS ---

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    val client: User,

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "remitente_id", nullable = false)
    val remitente: Cliente,

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "receptor_id", nullable = false)
    val receptor: Cliente,

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sucursal_id", nullable = false)
    @JsonIgnoreProperties("solicitudes")
    val sucursal: Sucursal,

    // 👇 CORRECCIÓN IMPORTANTE: Cambiado de OneToOne a ManyToOne 👇
    // Esto permite reutilizar la dirección ID 5 en múltiples solicitudes.
    @ManyToOne(cascade = [CascadeType.MERGE, CascadeType.PERSIST], fetch = FetchType.EAGER)
    @JoinColumn(name = "direccion_recoleccion_id", nullable = true)
    val direccionRecoleccion: Direccion? = null,

    // 👇 CORRECCIÓN IMPORTANTE: Cambiado de OneToOne a ManyToOne 👇
    @ManyToOne(cascade = [CascadeType.MERGE, CascadeType.PERSIST], fetch = FetchType.EAGER)
    @JoinColumn(name = "direccion_entrega_id", nullable = false)
    val direccionEntrega: Direccion,

    // El Paquete SI debe ser único por solicitud (OneToOne está bien aquí)
    @OneToOne(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    @JoinColumn(name = "paquete_id", nullable = false)
    val paquete: Paquete,

    // La Guía SI debe ser única por solicitud (OneToOne está bien aquí)
    @OneToOne(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    @JoinColumn(name = "guia_id", nullable = false)
    val guia: Guia,

    // ----------------------------
    // Asignaciones de personal
    // ----------------------------

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "conductor_id")
    @JsonIgnoreProperties("solicitudes")
    var conductor: User? = null,

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "gestor_id")
    @JsonIgnoreProperties("solicitudes")
    var gestor: User? = null,

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "funcionario_id")
    @JsonIgnoreProperties("solicitudes")
    val funcionario: User? = null,

    // ----------------------------
    // Fechas y Estados
    // ----------------------------

    @Column(name = "fecha_asignacion_conductor")
    val fechaAsignacionConductor: Instant? = null,

    @Column(name = "fecha_recoleccion_real")
    val fechaRecoleccionReal: Instant? = null,

    @Column(name = "fecha_entrega_real")
    val fechaEntregaReal: Instant? = null,

    @Column(nullable = false)
    val fechaRecoleccion: String,

    @Column(nullable = false)
    val franjaHoraria: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var estado: EstadoSolicitud = EstadoSolicitud.PENDIENTE,

    @Column(nullable = false)
    val createdAt: Instant = Instant.now(),

    val motivoCancelacion: String? = null

) {
    constructor() : this(
        client = User(),
        remitente = Cliente(nombre = "", numeroId = ""),
        receptor = Cliente(nombre = "", numeroId = ""),
        sucursal = Sucursal(),
        direccionRecoleccion = null,
        direccionEntrega = Direccion(),
        paquete = Paquete(),
        guia = Guia(),
        fechaRecoleccion = "",
        franjaHoraria = "",
        estado = EstadoSolicitud.PENDIENTE
    )
}