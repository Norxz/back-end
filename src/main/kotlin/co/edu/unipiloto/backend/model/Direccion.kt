package co.edu.unipiloto.backend.model

import jakarta.persistence.*

/**
 * Representa una dirección física asociada a un cliente o solicitud.
 * Puede contener coordenadas geográficas y detalles adicionales para la entrega.
 */
@Entity
@Table(name = "direcciones")
data class Direccion(

    /** Identificador único de la dirección en la base de datos */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    /** Dirección completa en formato texto (ej: Cra 68C #22b-71, Bogotá, Bogotá) */
    @Column(name = "direccion_completa", nullable = false)
    val direccionCompleta: String,

    /** Ciudad donde se encuentra la dirección */
    @Column(name = "ciudad", nullable = false)
    val ciudad: String,

    /** Latitud geográfica opcional de la dirección (útil para logística) */
    @Column(name = "latitud")
    val latitud: Double?,

    /** Longitud geográfica opcional de la dirección (útil para logística) */
    @Column(name = "longitud")
    val longitud: Double?,

    /** Información adicional como piso o número de apartamento */
    @Column(name = "piso_apto")
    val pisoApto: String?,

    /** Notas de entrega proporcionadas por el cliente */
    @Column(name = "notas_entrega")
    val notasEntrega: String?,

    /** Nombre del barrio, opcional */
    @Column(name = "barrio")
    val barrio: String? = null,

    /** Código postal de la dirección, opcional */
    @Column(name = "codigo_postal")
    val codigoPostal: String? = null,

    /** Tipo de dirección (ej: casa, oficina, almacén), opcional */
    @Column(name = "tipo_direccion")
    val tipoDireccion: String? = null

) {
    /**
     * Constructor vacío requerido por JPA.
     * Inicializa las propiedades con valores por defecto.
     */
    constructor() : this(
        direccionCompleta = "",
        ciudad = "",
        latitud = null,
        longitud = null,
        pisoApto = null,
        notasEntrega = null
    )
}
