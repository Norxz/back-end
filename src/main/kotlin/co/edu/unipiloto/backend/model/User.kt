package co.edu.unipiloto.backend.model

import co.edu.unipiloto.backend.model.enums.Role
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import jakarta.persistence.*
import java.time.Instant

@JsonIgnoreProperties(value = ["hibernateLazyInitializer", "handler"])
@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "documento")
    val documento: String? = null,

    @Column(name = "name")
    val fullName: String,

    @Column(name = "email", unique = true, nullable = false)
    val email: String,

    @Column(name = "password_hash", nullable = false)
    val passwordHash: String,

    @Column(name = "phone_number")
    val phoneNumber: String?,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val role: Role,

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sucursal_id")
    val sucursal: Sucursal?,

    @Column(name = "fecha_creacion")
    val fechaCreacion: Instant = Instant.now(),

    @Column(name = "ultimo_login")
    val ultimoLogin: Instant? = null,

    @Column(name = "is_active", columnDefinition = "tinyint(1) default 1")
    val isActive: Boolean = true
) {
    constructor() : this(
        fullName = "",
        email = "",
        passwordHash = "",
        phoneNumber = null,
        role = Role.USUARIO,
        sucursal = null
    )
}