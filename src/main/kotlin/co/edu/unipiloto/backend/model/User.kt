package co.edu.unipiloto.backend.model

import jakarta.persistence.*

@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "name")
    val fullName: String,

    @Column(name = "email", unique = true, nullable = false)
    val email: String,

    @Column(name = "password_hash", nullable = false)
    val passwordHash: String,

    @Column(name = "phone_number")
    val phoneNumber: String?,

    @Column(name = "role", nullable = false)
    val role: String,

    @Column(name = "sucursal")
    val sucursal: String?,

    @Column(name = "is_active", columnDefinition = "tinyint(1) default 1")
    val isActive: Boolean = true
) {
    // Constructor vacío requerido por JPA
    constructor() : this(
        fullName = "", email = "", passwordHash = "",
        phoneNumber = null, role = "", sucursal = null
    )
}