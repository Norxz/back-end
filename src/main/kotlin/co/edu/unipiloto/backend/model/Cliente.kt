package co.edu.unipiloto.backend.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "clientes")
data class Cliente(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    val nombre: String,

    @Column(name = "tipo_id")
    val tipoId: String? = null,

    @Column(name = "numero_id", nullable = false)
    val numeroId: String,

    val telefono: String? = null,

    val codigoPais: String? = null,

    @Column(name = "tipo_cliente")
    val tipoCliente: String? = null,

    @Column(name = "fecha_creacion", nullable = false)
    val fechaCreacion: Instant = Instant.now(),

    @OneToMany(mappedBy = "remitente", fetch = FetchType.LAZY)
    @JsonIgnoreProperties("remitente", "receptor")
    val solicitudesComoRemitente: List<Solicitud> = emptyList(),

    @OneToMany(mappedBy = "receptor", fetch = FetchType.LAZY)
    @JsonIgnoreProperties("receptor", "remitente")
    val solicitudesComoReceptor: List<Solicitud> = emptyList()

)