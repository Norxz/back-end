package co.edu.unipiloto.backend.model

import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "solicitudes")
data class Solicitud(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    // Relación con el Cliente (el que hizo la solicitud)
    @ManyToOne(fetch = FetchType.LAZY) // Muchas solicitudes pueden ser hechas por un solo User
    @JoinColumn(name = "client_id", nullable = false)
    val client: User, // Asumimos que la entidad User ya existe

    // Relación con el Recolector (LogisticUser) - Opcional al crear
    // Nota: Deberías crear una entidad LogisticUser o usar la entidad User con role='CONDUCTOR'
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recolector_id")
    val recolector: User? = null,

    // Relación con la Dirección (Los datos de la dirección deben ser persistidos)
    // CascadeType.ALL asegura que si se borra la solicitud, se borra la dirección
    @ManyToOne(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    @JoinColumn(name = "direccion_id", nullable = false)
    val direccion: Direccion,

    // Relación con la Guía (El número de Guía se genera al crear la solicitud, como en la imagen)
    @OneToOne(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    @JoinColumn(name = "guia_id", nullable = false)
    val guia: Guia,

    @Column(name = "fecha_recoleccion", nullable = false)
    val fechaRecoleccion: String, // Usar LocalDate es mejor, pero String por simplicidad inicial

    @Column(name = "franja_horaria", nullable = false)
    val franjaHoraria: String,

    // Campo que refleja el estado PENDIENTE, CANCELADA, etc. (como en la imagen de Android)
    @Column(name = "estado", nullable = false)
    var estado: String,

    @Column(name = "peso_kg")
    val pesoKg: Double,

    @Column(name = "precio")
    val precio: Double,

    @Column(name = "created_at", nullable = false)
    val createdAt: Instant = Instant.now()
) {
    // Constructor vacío requerido por JPA
    constructor() : this(
        client = User(),
        direccion = Direccion(),
        guia = Guia(),
        fechaRecoleccion = "",
        franjaHoraria = "",
        estado = "PENDIENTE",
        pesoKg = 0.0,
        precio = 0.0
    )
}