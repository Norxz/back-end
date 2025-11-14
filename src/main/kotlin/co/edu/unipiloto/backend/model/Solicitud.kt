package co.edu.unipiloto.backend.model

import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "solicitudes")
data class Solicitud(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    // Cliente creador
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    val client: User,

    // Datos del remitente
    @Column(name = "remitente_nombre")
    val remitenteNombre: String? = null,

    @Column(name = "remitente_tipo_id")
    val remitenteTipoId: String? = null,

    @Column(name = "remitente_numero_id")
    val remitenteNumeroId: String? = null,

    @Column(name = "remitente_telefono")
    val remitenteTelefono: String? = null,

    @Column(name = "remitente_codigo_pais")
    val remitenteCodigoPais: String? = null,

    // Datos del receptor
    @Column(name = "receptor_nombre")
    val receptorNombre: String? = null,

    @Column(name = "receptor_tipo_id")
    val receptorTipoId: String? = null,

    @Column(name = "receptor_numero_id")
    val receptorNumeroId: String? = null,

    @Column(name = "receptor_telefono")
    val receptorTelefono: String? = null,

    @Column(name = "receptor_codigo_pais")
    val receptorCodigoPais: String? = null,

    // Medidas del paquete
    @Column(name = "alto")
    val alto: Double? = null,

    @Column(name = "ancho")
    val ancho: Double? = null,

    @Column(name = "largo")
    val largo: Double? = null,

    @Column(name = "contenido")
    val contenido: String? = null,

    // Relación Dirección
    @ManyToOne(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    @JoinColumn(name = "direccion_id", nullable = false)
    val direccion: Direccion,

    // Relación Guía
    @OneToOne(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    @JoinColumn(name = "guia_id", nullable = false)
    val guia: Guia,

    @Column(name = "fecha_recoleccion", nullable = false)
    val fechaRecoleccion: String,

    @Column(name = "franja_horaria", nullable = false)
    val franjaHoraria: String,

    @Column(name = "estado", nullable = false)
    var estado: String,

    @Column(name = "peso_kg")
    val pesoKg: Double,

    @Column(name = "precio")
    val precio: Double,

    @Column(name = "created_at", nullable = false)
    val createdAt: Instant = Instant.now()
) {

    constructor() : this(
        client = User(),
        direccion = Direccion(),
        guia = Guia(),
        fechaRecoleccion = "",
        franjaHoraria = "",
        estado = "PENDIENTE",
        pesoKg = 0.0,
        precio = 0.0
    )
}
