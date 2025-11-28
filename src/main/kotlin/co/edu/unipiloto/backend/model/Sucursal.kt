package co.edu.unipiloto.backend.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import jakarta.persistence.*

/**
 * Representa una sucursal de la empresa de logística.
 * Contiene información de su nombre, dirección y solicitudes asociadas.
 */
@JsonIgnoreProperties(value = ["hibernateLazyInitializer", "handler"])
@Entity
@Table(name = "sucursales")
data class Sucursal(

    /** Identificador único de la sucursal */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    /** Nombre de la sucursal */
    @Column(nullable = false)
    val nombre: String,

    /** Dirección física de la sucursal */
    @OneToOne(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    @JoinColumn(name = "direction_id", nullable = false)
    val direccion: Direccion,

    /** Lista de solicitudes asociadas a esta sucursal */
    @OneToMany(mappedBy = "sucursal", fetch = FetchType.LAZY)
    @JsonIgnoreProperties("sucursal")
    val solicitudes: List<Solicitud> = emptyList()

) {
    /**
     * Constructor vacío requerido por JPA.
     * Inicializa el nombre y la dirección con valores por defecto.
     */
    constructor() : this(
        nombre = "",
        direccion = Direccion()
    )
}
