package co.edu.unipiloto.backend.model

import jakarta.persistence.*

@Entity
@Table(name = "direcciones")
data class Direccion(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "direccion_completa", nullable = false)
    val direccionCompleta: String, // Cra 68C #22b-71, Bogotá, Bogotá

    @Column(name = "ciudad", nullable = false)
    val ciudad: String,

    @Column(name = "latitud")
    val latitud: Double?, // Opcional, pero muy recomendado para la logística

    @Column(name = "longitud")
    val longitud: Double?,

    @Column(name = "piso_apto")
    val pisoApto: String?, // Información adicional como piso o número de apartamento

    @Column(name = "notas_entrega")
    val notasEntrega: String? // Notas del cliente (ej. "Tocar 2 veces")

) {
    // Constructor vacío requerido por JPA
    constructor() : this(
        direccionCompleta = "",
        ciudad = "",
        latitud = null,
        longitud = null,
        pisoApto = null,
        notasEntrega = null
    )
}