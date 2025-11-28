package co.edu.unipiloto.backend.model

import jakarta.persistence.*

/**
 * Representa un paquete que será enviado a través del sistema logístico.
 * Contiene dimensiones, peso, contenido y categoría del paquete.
 */
@Entity
@Table(name = "paquetes")
data class Paquete(

    /** Identificador único del paquete en la base de datos */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    /** Peso del paquete en kilogramos */
    val peso: Double = 0.0,

    /** Altura del paquete (opcional) */
    val alto: Double? = null,

    /** Ancho del paquete (opcional) */
    val ancho: Double? = null,

    /** Largo del paquete (opcional) */
    val largo: Double? = null,

    /** Descripción del contenido del paquete */
    val contenido: String? = null,

    /** Categoría del paquete (ej: frágil, electrónico, ropa, etc.) */
    @Column(name = "categoria")
    val categoria: String? = null,
) {
    /**
     * Constructor vacío requerido por JPA.
     * Inicializa los campos con valores por defecto o null según corresponda.
     */
    constructor() : this(
        peso = 0.0,
        alto = null,
        ancho = null,
        largo = null,
        contenido = null
    )
}
