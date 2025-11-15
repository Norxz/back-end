package co.edu.unipiloto.backend.model

import jakarta.persistence.*

@Entity
@Table(name = "paquetes")
data class Paquete(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val peso: Double = 0.0,
    val alto: Double? = null,
    val ancho: Double? = null,
    val largo: Double? = null,
    val contenido: String? = null
) {
    constructor() : this(
        peso = 0.0,
        alto = null,
        ancho = null,
        largo = null,
        contenido = null
    )
}
