package co.edu.unipiloto.backend.model

import jakarta.persistence.*

@Entity
@Table(name = "clientes")
data class Cliente(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    val nombre: String,

    @Column(name = "tipo_id")
    val tipoId: String? = null,

    @Column(name = "numero_id")
    val numeroId: String? = null,

    val telefono: String? = null,
    val codigoPais: String? = null
) {
    constructor() : this(nombre = "")
}
