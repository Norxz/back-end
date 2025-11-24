package co.edu.unipiloto.backend.model

import jakarta.persistence.*

@Entity
@Table(name = "sucursales")
data class Sucursal(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    val nombre: String,

    @OneToOne(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    @JoinColumn(name = "direction_id", nullable = false)
    val direccion: Direccion,

    @OneToMany(mappedBy = "sucursal", fetch = FetchType.LAZY)
    val solicitudes: List<Solicitud> = emptyList()

) {
    constructor() : this(nombre = "", direccion = Direccion())
}
