package co.edu.unipiloto.backend.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import jakarta.persistence.*

/**
 * 🏢 Entidad JPA que representa una **Sucursal** (centro operativo o bodega)
 * de la empresa de logística.
 *
 * Es un punto geográfico clave en el sistema, asociado a una dirección y responsable
 * de la gestión de un conjunto de solicitudes de envío. Mapea a la tabla `sucursales`.
 *
 * La anotación `@JsonIgnoreProperties` evita problemas de inicialización perezosa (Lazy Initialization)
 * y bucles de serialización al tratar con proxies de Hibernate.
 */
@JsonIgnoreProperties(value = ["hibernateLazyInitializer", "handler"])
@Entity
@Table(name = "sucursales")
data class Sucursal(

    /** 🔑 Identificador único (Primary Key) de la sucursal en la base de datos. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    /** 🏷️ Nombre descriptivo de la sucursal (Ej: "Centro", "Norte B"). **No nulo**. */
    @Column(nullable = false)
    val nombre: String,

    /**
     * 🗺️ **Relación OneToOne:** La dirección física ([Direccion]) de la sucursal.
     * - `cascade = [CascadeType.ALL]`: Las operaciones (guardar, actualizar, eliminar) se propagan a la entidad [Direccion].
     * - `fetch = FetchType.EAGER`: La dirección se carga inmediatamente al cargar la sucursal.
     * - `@JoinColumn`: Especifica la clave foránea (`direction_id`).
     */
    @OneToOne(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    @JoinColumn(name = "direction_id", nullable = false)
    val direccion: Direccion,

    /**
     * 📨 **Relación Uno a Muchos** con [Solicitud].
     * Lista de solicitudes gestionadas por o asignadas a esta sucursal.
     * - `mappedBy = "sucursal"`: Indica que la relación es bidireccional y el campo de mapeo está en la entidad [Solicitud].
     * - `fetch = FetchType.LAZY`: Las solicitudes solo se cargan si se acceden explícitamente.
     * - `@JsonIgnoreProperties`: Previene bucles infinitos durante la serialización JSON.
     */
    @OneToMany(mappedBy = "sucursal", fetch = FetchType.LAZY)
    @JsonIgnoreProperties("sucursal")
    val solicitudes: List<Solicitud> = emptyList()

) {
    /**
     * 🏗️ Constructor vacío requerido por JPA (Hibernate).
     * Proporciona inicialización segura de los campos obligatorios.
     */
    constructor() : this(
        nombre = "",
        direccion = Direccion()
    )
}