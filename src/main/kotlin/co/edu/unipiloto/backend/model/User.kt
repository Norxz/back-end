package co.edu.unipiloto.backend.model

import co.edu.unipiloto.backend.model.enums.Role
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import jakarta.persistence.*
import java.time.Instant

/**
 * Entidad que representa a un usuario del sistema.
 * Puede ser un cliente, gestor, conductor, administrador u otro rol definido en [Role].
 */
@JsonIgnoreProperties(value = ["hibernateLazyInitializer", "handler"])
@Entity
@Table(name = "users")
data class User(
    /** Identificador único del usuario */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    /** Documento de identidad del usuario (opcional) */
    @Column(name = "documento")
    val documento: String? = null,

    /** Nombre completo del usuario */
    @Column(name = "name")
    val fullName: String,

    /** Correo electrónico único del usuario, obligatorio */
    @Column(name = "email", unique = true, nullable = false)
    val email: String,

    /** Hash de la contraseña del usuario, obligatorio */
    @Column(name = "password_hash", nullable = false)
    val passwordHash: String,

    /** Número de teléfono del usuario, opcional */
    @Column(name = "phone_number")
    val phoneNumber: String?,

    /** Rol del usuario dentro del sistema */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val role: Role,

    /** Sucursal asignada al usuario (opcional) */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sucursal_id")
    val sucursal: Sucursal?,

    /** Fecha de creación del usuario, por defecto el momento de inserción */
    @Column(name = "fecha_creacion")
    val fechaCreacion: Instant = Instant.now(),

    /** Última fecha de login del usuario, opcional */
    @Column(name = "ultimo_login")
    val ultimoLogin: Instant? = null,

    /** Indica si el usuario está activo; por defecto true */
    @Column(name = "is_active", columnDefinition = "tinyint(1) default 1")
    val isActive: Boolean = true
) {
    /**
     * Constructor vacío requerido por JPA.
     * Inicializa valores por defecto para campos no nulos.
     */
    constructor() : this(
        fullName = "",
        email = "",
        passwordHash = "",
        phoneNumber = null,
        role = Role.CLIENTE,
        sucursal = null
    )
}
