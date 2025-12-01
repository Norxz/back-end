package co.edu.unipiloto.backend.model

import jakarta.persistence.*

/**
 * 📦 Entidad JPA que representa la **carga física** (el paquete) que será enviada
 * a través del sistema logístico.
 *
 * Sus atributos son esenciales para determinar el costo del envío (basado en peso/volumen)
 * y las necesidades de manejo logístico. Mapea a la tabla `paquetes` en la base de datos.
 */
@Entity
@Table(name = "paquetes")
data class Paquete(

    /** 🔑 Identificador único (Primary Key) del paquete en la base de datos. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    /** ⚖️ Peso del paquete, generalmente expresado en kilogramos (kg). **Obligatorio** (valor por defecto 0.0). */
    val peso: Double = 0.0,

    /** Altura del paquete, expresada en centímetros (cm). Opcional, usado para calcular el peso volumétrico. */
    val alto: Double? = null,

    /** Ancho del paquete, expresado en centímetros (cm). Opcional, usado para calcular el peso volumétrico. */
    val ancho: Double? = null,

    /** Largo del paquete, expresado en centímetros (cm). Opcional, usado para calcular el peso volumétrico. */
    val largo: Double? = null,

    /** Descripción breve del contenido del paquete (Ej: "Documentos personales"). */
    val contenido: String? = null,

    /** Clasificación logística o de manejo (Ej: "Frágil", "Perecedero", "Ropa"). Opcional. */
    @Column(name = "categoria")
    val categoria: String? = null,
) {
    /**
     * 🏗️ Constructor vacío requerido por JPA (Hibernate).
     * Proporciona valores por defecto para permitir la instanciación por reflexión.
     */
    constructor() : this(
        peso = 0.0,
        alto = null,
        ancho = null,
        largo = null,
        contenido = null,
        categoria = null
    )
}