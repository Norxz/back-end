package co.edu.unipiloto.backend.model

import jakarta.persistence.*
import java.time.Instant

/**
 * Entidad unificada para almacenar datos de contacto (Personas o Empresas)
 * que actúan como Remitentes o Destinatarios en una Solicitud.
 */
@Entity
@Table(name = "contactos")
data class Contacto(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "nombre_completo", nullable = false)
    val nombreCompleto: String,

    @Column(name = "tipo_identificacion", nullable = false)
    val tipoIdentificacion: String, // Ej: CC, NIT, Cédula Extranjera

    @Column(name = "numero_identificacion", unique = true, nullable = false)
    val numeroIdentificacion: String,

    @Column(name = "telefono", nullable = false)
    val telefono: String,

    @Column(name = "created_at", nullable = false)
    val createdAt: Instant = Instant.now()
) {
    // Constructor vacío requerido por JPA
    constructor() : this(
        nombreCompleto = "",
        tipoIdentificacion = "",
        numeroIdentificacion = "",
        telefono = "",
    )
}