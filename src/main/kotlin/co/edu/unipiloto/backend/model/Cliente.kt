package co.edu.unipiloto.backend.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import jakarta.persistence.*
import java.time.Instant

/**
 * 🧑‍🤝‍🧑 Entidad JPA que representa a un **Cliente** dentro del sistema logístico.
 *
 * Un cliente es una persona o entidad que puede iniciar (remitente) o recibir (receptor)
 * solicitudes de envío. Mapea a la tabla `clientes` en la base de datos.
 */
@Entity
@Table(name = "clientes")
data class Cliente(

    /** 🔑 Identificador único (Primary Key) del cliente en la base de datos. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    /** 🏷️ Nombre completo o razón social del cliente. No puede ser nulo. */
    @Column(nullable = false)
    val nombre: String,

    /** Tipo de identificación (ej: C.C., NIT, C.E.). */
    @Column(name = "tipo_id")
    val tipoId: String? = null,

    /** 🆔 Número de identificación único del cliente. No puede ser nulo. */
    @Column(name = "numero_id", nullable = false)
    val numeroId: String,

    /** 📞 Número de teléfono de contacto. */
    val telefono: String? = null,

    /** Código de país asociado al teléfono (ej: "+57"). */
    val codigoPais: String? = null,

    /** Clasificación del cliente para uso interno (ej: "regular", "VIP", "corporativo"). */
    @Column(name = "tipo_cliente")
    val tipoCliente: String? = null,

    /** 🕰️ Marca de tiempo de la creación del registro del cliente. Se inicializa automáticamente. */
    @Column(name = "fecha_creacion", nullable = false)
    val fechaCreacion: Instant = Instant.now(),

    // --- RELACIONES JPA ---

    /**
     * 📨 **Relación Uno a Muchos** con [Solicitud].
     * Lista de todas las solicitudes donde este cliente figura como **remitente**.
     * - `mappedBy = "remitente"`: Indica que la relación es bidireccional y el campo de mapeo está en la entidad [Solicitud].
     * - `FetchType.LAZY`: Los datos de la lista solo se cargan cuando se acceden explícitamente.
     * - `@JsonIgnoreProperties`: Previene bucles infinitos durante la serialización JSON.
     */
    @OneToMany(mappedBy = "remitente", fetch = FetchType.LAZY)
    @JsonIgnoreProperties("remitente", "receptor")
    val solicitudesComoRemitente: List<Solicitud> = emptyList(),

    /**
     * 📥 **Relación Uno a Muchos** con [Solicitud].
     * Lista de todas las solicitudes donde este cliente figura como **receptor**.
     * - `mappedBy = "receptor"`: Indica que el mapeo está en la entidad [Solicitud].
     * - `@JsonIgnoreProperties`: Previene bucles infinitos de serialización.
     */
    @OneToMany(mappedBy = "receptor", fetch = FetchType.LAZY)
    @JsonIgnoreProperties("receptor", "remitente")
    val solicitudesComoReceptor: List<Solicitud> = emptyList()
)