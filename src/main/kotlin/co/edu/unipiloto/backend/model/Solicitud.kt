package co.edu.unipiloto.backend.model

import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "solicitudes")
data class Solicitud(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

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
    val sucursal: Sucursal,

    @ManyToOne(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    @JoinColumn(name = "direccion_id", nullable = false)
    val direccion: Direccion,

    @OneToOne(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    @JoinColumn(name = "paquete_id", nullable = false)
    val paquete: Paquete,

    @OneToOne(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    @JoinColumn(name = "guia_id", nullable = false)
    val guia: Guia,

    @Column(nullable = false)
    val fechaRecoleccion: String,

    @Column(nullable = false)
    val franjaHoraria: String,

    @Column(nullable = false)
    var estado: String = "PENDIENTE",

    @Column(nullable = false)
    val createdAt: Instant = Instant.now()
) {
    constructor() : this(
        client = User(),
        remitente = Cliente(nombre = ""),
        receptor = Cliente(nombre = ""),
        sucursal = Sucursal(),
        direccion = Direccion(),
        paquete = Paquete(),
        guia = Guia(),
        fechaRecoleccion = "",
        franjaHoraria = "",
        estado = "PENDIENTE"
    )
}
