package co.edu.unipiloto.backend.model

import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "guias")
data class Guia(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    // El número de guía que se muestra al usuario. Podría ser un UUID o un número secuencial.
    @Column(name = "numero_guia", unique = true, nullable = false)
    val numeroGuia: String,

    @Column(name = "tracking_number", unique = true, nullable = false)
    val trackingNumber: String, // Número de seguimiento interno (si es diferente del número de guía)

    @Column(name = "volumen_m3")
    val volumenM3: Double?,

    @Column(name = "fecha_creacion", nullable = false)
    val fechaCreacion: Instant = Instant.now()

    // Aquí puedes añadir más campos, como el nombre del remitente/destinatario si lo necesitas

) {
    // Constructor vacío requerido por JPA
    constructor() : this(
        numeroGuia = "",
        trackingNumber = "",
        volumenM3 = null
    )
}