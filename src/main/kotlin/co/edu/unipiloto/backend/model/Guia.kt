package co.edu.unipiloto.backend.model

import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "guias")
data class Guia(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    // Número de guía visible al usuario
    @Column(nullable = false, unique = true)
    val numeroGuia: String,

    // Código interno de rastreo
    @Column(nullable = false, unique = true)
    val trackingNumber: String,

    // 📦 Dimensiones físicas
    @Column
    val pesoKg: Double? = null,

    @Column
    val altoCm: Double? = null,

    @Column
    val anchoCm: Double? = null,

    @Column
    val largoCm: Double? = null,

    // 📝 Descripción del contenido
    @Column
    val contenidoDescripcion: String? = null,

    // 📍 Ubicación de destino (mapa)
    @Column
    val latitudDestino: Double? = null,

    @Column
    val longitudDestino: Double? = null,

    // 🕒 Fecha y hora de creación
    @Column(nullable = false)
    val fechaCreacion: Instant = Instant.now(),

    // 🔹 Relación con el remitente
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "remitente_id", nullable = false)
    val remitente: Contacto,

    // 🔹 Relación con el destinatario
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "destinatario_id", nullable = false)
    val destinatario: Contacto
) {
    // 🧮 Campo calculado: volumen (m³)
    val volumenM3: Double
        get() = if (altoCm != null && anchoCm != null && largoCm != null)
            (altoCm!! * anchoCm!! * largoCm!!) / 1_000_000
        else 0.0

    // 💰 Campo calculado: precio estimado (ejemplo simple)
    val precioEstimado: Double
        get() {
            val base = 8000.0
            val peso = (pesoKg ?: 0.0) * 500.0
            val volumen = volumenM3 * 20000.0
            return base + peso + volumen
        }

    constructor() : this(
        numeroGuia = "",
        trackingNumber = "",
        remitente = Contacto(),
        destinatario = Contacto()
    )
}
