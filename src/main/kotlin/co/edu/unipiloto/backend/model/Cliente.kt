package co.edu.unipiloto.backend.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import jakarta.persistence.*
import java.time.Instant

/**
 * Representa un cliente dentro del sistema.
 * Un cliente puede actuar como remitente o receptor de solicitudes de envío.
 */
@Entity
@Table(name = "clientes")
data class Cliente(

    /** Identificador único del cliente en la base de datos */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    /** Nombre completo del cliente */
    @Column(nullable = false)
    val nombre: String,

    /** Tipo de identificación (ej: C.C., NIT, etc.) */
    @Column(name = "tipo_id")
    val tipoId: String? = null,

    /** Número de identificación del cliente */
    @Column(name = "numero_id", nullable = false)
    val numeroId: String,

    /** Número de teléfono del cliente */
    val telefono: String? = null,

    /** Código de país asociado al teléfono del cliente */
    val codigoPais: String? = null,

    /** Tipo de cliente (ej: regular, VIP, corporativo) */
    @Column(name = "tipo_cliente")
    val tipoCliente: String? = null,

    /** Fecha de creación del registro del cliente */
    @Column(name = "fecha_creacion", nullable = false)
    val fechaCreacion: Instant = Instant.now(),

    /** Lista de solicitudes donde el cliente es remitente */
    @OneToMany(mappedBy = "remitente", fetch = FetchType.LAZY)
    @JsonIgnoreProperties("remitente", "receptor")
    val solicitudesComoRemitente: List<Solicitud> = emptyList(),

    /** Lista de solicitudes donde el cliente es receptor */
    @OneToMany(mappedBy = "receptor", fetch = FetchType.LAZY)
    @JsonIgnoreProperties("receptor", "remitente")
    val solicitudesComoReceptor: List<Solicitud> = emptyList()
)
