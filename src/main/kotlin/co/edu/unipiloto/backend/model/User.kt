package co.edu.unipiloto.backend.model

import co.edu.unipiloto.backend.model.enums.Role
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import jakarta.persistence.*
import java.time.Instant

/**
 * 👤 Entidad JPA que representa a un **Usuario** del sistema logístico.
 *
 * Esta es la entidad de autenticación y autorización, que incluye a clientes, gestores,
 * conductores, y administradores. Mapea a la tabla `users` en la base de datos.
 *
 * La anotación `@JsonIgnoreProperties` evita problemas de inicialización perezosa (Lazy Initialization)
 * y bucles de serialización al tratar con proxies de Hibernate.
 */
@JsonIgnoreProperties(value = ["hibernateLazyInitializer", "handler"])
@Entity
@Table(name = "users")
data class User(
    /** 🔑 Identificador único (Primary Key) del usuario. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    /** Documento de identidad del usuario (ej. C.C., pasaporte). Opcional. */
    @Column(name = "documento")
    val documento: String? = null,

    /** Nombre completo del usuario. **No nulo**. */
    @Column(name = "name")
    val fullName: String,

    /** 📧 Correo electrónico del usuario. **Debe ser único** y **No nulo**. */
    @Column(name = "email", unique = true, nullable = false)
    val email: String,

    /** 🔒 Hash seguro de la contraseña. **No nulo**. Es vital para la seguridad. */
    @Column(name = "password_hash", nullable = false)
    val passwordHash: String,

    /** Número de teléfono de contacto. Opcional. */
    @Column(name = "phone_number")
    val phoneNumber: String?,

    /** 👑 **Rol** del usuario ([Role]) dentro del sistema. Se almacena como un String en la DB. **No nulo**. */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val role: Role,

    /**
     * 🏢 **Relación ManyToOne:** Sucursal ([Sucursal]) asignada al usuario.
     * - `fetch = FetchType.EAGER`: La sucursal se carga inmediatamente con el usuario.
     * - `var` permite que el gestor/administrador pueda cambiar la sucursal asignada.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sucursal_id")
    var sucursal: Sucursal?,

    /** 🕰️ Fecha y hora de creación del registro del usuario. Se inicializa automáticamente. */
    @Column(name = "fecha_creacion")
    val fechaCreacion: Instant = Instant.now(),

    /** Última marca de tiempo en que el usuario inició sesión. Opcional. */
    @Column(name = "ultimo_login")
    val ultimoLogin: Instant? = null,

    /** 🟢 **Estado de la cuenta:** Indica si el usuario puede iniciar sesión y operar. **Por defecto es `true`**. */
    @Column(name = "is_active", columnDefinition = "tinyint(1) default 1")
    var isActive: Boolean = true
) {
    /**
     * 🏗️ Constructor vacío requerido por JPA (Hibernate).
     * Proporciona inicialización segura de los campos obligatorios.
     */
    constructor() : this(
        fullName = "",
        email = "",
        passwordHash = "",
        phoneNumber = null,
        role = Role.CLIENTE,
        sucursal = null,
        documento = null,
        ultimoLogin = null,
        isActive = true
    )
}