package co.edu.unipiloto.backend.model

import jakarta.persistence.*
import java.time.Instant
import java.time.LocalDate

@Entity
@Table(name = "solicitudes")
data class Solicitud(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    // 🧍 Cliente que crea la solicitud
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    val cliente: User,

    // 🚚 Recolector asignado (opcional)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recolector_id")
    val recolector: User? = null,

    // 📍 Dirección de origen
    @ManyToOne(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    @JoinColumn(name = "origen_direccion_id", nullable = false)
    val origenDireccion: Direccion,

    // 🎯 Dirección de destino
    @ManyToOne(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    @JoinColumn(name = "destino_direccion_id", nullable = false)
    val destinoDireccion: Direccion,

    // 📦 Guía asociada
    @OneToOne(cascade = [CascadeType.ALL], fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "guia_id", nullable = false)
    val guia: Guia,

    // 📅 Fecha y franja horaria de recolección
    @Column(nullable = false)
    val fechaRecoleccion: LocalDate,

    @Column(nullable = false)
    val franjaHoraria: String,

    // 🚀 Tipo de servicio (normal, express, etc.)
    @Column(nullable = false)
    val tipoServicio: String = "NORMAL",

    // 🗒️ Observaciones del cliente
    @Column(columnDefinition = "TEXT")
    val observaciones: String? = null,

    // 📦 Estado actual de la solicitud
    @Column(nullable = false)
    val estado: String = "PENDIENTE",

    // 🕒 Fecha de creación y actualización
    @Column(nullable = false)
    val createdAt: Instant = Instant.now(),

    @Column
    val updatedAt: Instant? = null,

    // 📆 Fecha estimada de entrega (opcional)
    @Column
    val fechaEntregaEstimada: LocalDate? = null
) {
    constructor() : this(
        cliente = User(),
        origenDireccion = Direccion(),
        destinoDireccion = Direccion(),
        guia = Guia(),
        fechaRecoleccion = LocalDate.now(),
        franjaHoraria = "",
        tipoServicio = "NORMAL",
        estado = "PENDIENTE"
    )

    // 💰 Cálculo dinámico del precio según la guía y tipo de servicio
    val precioEstimado: Double
        get() {
            var base = guia.precioEstimado
            if (tipoServicio == "EXPRESS") base *= 1.3
            return base
        }
}
