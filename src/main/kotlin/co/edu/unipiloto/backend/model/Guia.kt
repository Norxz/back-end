package co.edu.unipiloto.backend.model

import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "guias")
data class Guia(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(unique = true, nullable = false)
    val numeroGuia: String,

    @Column(unique = true, nullable = false)
    val trackingNumber: String,

    @Column(nullable = false)
    val fechaCreacion: Instant = Instant.now(),

    @Column(name = "costo_envio")
    val costoEnvio: Double? = null,

    @Column(name = "estado_guia")
    val estadoGuia: String = "CREADA",

    @Column(name = "ultima_actualizacion")
    val ultimaActualizacion: Instant = Instant.now()

) {
    constructor() : this(
        id = null,
        numeroGuia = "",
        trackingNumber = "",
        fechaCreacion = Instant.now()
    )
}
