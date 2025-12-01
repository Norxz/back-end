package co.edu.unipiloto.backend.model

import jakarta.persistence.*

/**
 * 🗺️ Entidad JPA que representa una **Dirección** física o punto de ubicación.
 *
 * Esta entidad es reutilizable y se asocia a:
 * - Solicitudes (para recolección y entrega).
 * - Sucursales.
 * - (Potencialmente) Usuarios.
 *
 * Mapea a la tabla `direcciones` en la base de datos.
 */
@Entity
@Table(name = "direcciones")
data class Direccion(

    /** 🔑 Identificador único (Primary Key) de la dirección en la base de datos. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    /** 🏷️ Dirección completa y estructurada en formato texto (Ej: Calle 10 # 5-45). **No nulo**. */
    @Column(name = "direccion_completa", nullable = false)
    val direccionCompleta: String,

    /** Ciudad o municipio donde se encuentra la dirección. **No nulo**. */
    @Column(name = "ciudad", nullable = false)
    val ciudad: String,

    /** Coordenada Y: Latitud geográfica. Opcional, pero vital para el ruteo logístico. */
    @Column(name = "latitud")
    val latitud: Double?,

    /** Coordenada X: Longitud geográfica. Opcional, pero vital para el ruteo logístico. */
    @Column(name = "longitud")
    val longitud: Double?,

    /** Información adicional como número de piso o apartamento. */
    @Column(name = "piso_apto")
    val pisoApto: String?,

    /** Instrucciones adicionales o puntos de referencia para el conductor/repartidor. */
    @Column(name = "notas_entrega")
    val notasEntrega: String?,

    /** Nombre del barrio o sector. Opcional. */
    @Column(name = "barrio")
    val barrio: String? = null,

    /** Código postal de la zona. Opcional. */
    @Column(name = "codigo_postal")
    val codigoPostal: String? = null,

    /** Clasificación de la ubicación (ej: "residencial", "comercial"). Opcional. */
    @Column(name = "tipo_direccion")
    val tipoDireccion: String? = null

) {
    /**
     * 🏗️ Constructor vacío requerido por JPA (Hibernate).
     * Proporciona valores por defecto para permitir la instanciación por reflexión.
     */
    constructor() : this(
        direccionCompleta = "",
        ciudad = "",
        latitud = null,
        longitud = null,
        pisoApto = null,
        notasEntrega = null,
        barrio = null,
        codigoPostal = null,
        tipoDireccion = null
    )
}