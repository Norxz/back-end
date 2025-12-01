package co.edu.unipiloto.backend.service

import co.edu.unipiloto.backend.dto.SucursalRequest
import co.edu.unipiloto.backend.model.Direccion
import co.edu.unipiloto.backend.model.Sucursal
import co.edu.unipiloto.backend.repository.SucursalRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * 🏢 Servicio de Spring (`@Service`) encargado de la lógica de negocio para la gestión de la entidad [Sucursal] (Oficina o Punto de Servicio).
 *
 * Proporciona funcionalidades completas para **CRUD** (Crear, Leer, Actualizar, Eliminar) de sucursales,
 * además de utilidades geográficas como la búsqueda de la sucursal más cercana.
 */
@Service
class SucursalService(
    private val sucursalRepository: SucursalRepository
) {
    /** Radio de la Tierra en kilómetros, utilizado para la fórmula de Haversine. */
    private val EARTH_RADIUS_KM = 6371.0

    // -------------------------------------------------------------------------
    // ## Operaciones CRUD
    // -------------------------------------------------------------------------

    /**
     * 📋 Recupera una lista de **todas las sucursales** registradas en el sistema.
     *
     * @return Una [List] de todas las entidades [Sucursal].
     */
    fun listarTodas(): List<Sucursal> = sucursalRepository.findAll()

    /**
     * ➕ Crea una nueva sucursal a partir de los datos de la solicitud [SucursalRequest].
     *
     * La dirección se crea anidada dentro de la operación, asegurando que la sucursal tenga
     * una [Direccion] asociada desde su creación.
     *
     * @param request El DTO con los datos de la nueva sucursal.
     * @return La entidad [Sucursal] recién creada y persistida.
     */
    @Transactional
    fun crearSucursal(request: SucursalRequest): Sucursal {
        // Mapeo del DTO de Dirección a la entidad Direccion
        val direccion = Direccion(
            direccionCompleta = request.direccion.direccionCompleta,
            ciudad = request.direccion.ciudad,
            latitud = request.direccion.latitud,
            longitud = request.direccion.longitud,
            pisoApto = request.direccion.pisoApto,
            notasEntrega = request.direccion.notasEntrega,
            barrio = request.direccion.barrio,
            codigoPostal = request.direccion.codigoPostal,
            tipoDireccion = request.direccion.tipoDireccion
        )

        // Creación y asignación de la entidad Sucursal
        val nuevaSucursal = Sucursal(
            nombre = request.nombre,
            direccion = direccion // Asignación de la dirección
        )

        return sucursalRepository.save(nuevaSucursal)
    }

    /**
     * 🔎 Busca una sucursal por su ID único.
     *
     * @param id El ID de la sucursal a buscar.
     * @return La entidad [Sucursal] si es encontrada, o `null`.
     */
    fun obtenerPorId(id: Long): Sucursal? =
        sucursalRepository.findById(id).orElse(null)

    /**
     * ✏️ Actualiza la información de una sucursal existente (nombre y datos de dirección).
     *
     * Utiliza la función `copy()` de Kotlin para crear una nueva instancia de la [Sucursal]
     * con los valores actualizados, garantizando la inmutabilidad si la entidad fuera un `data class`.
     *
     * @param id El ID de la sucursal a actualizar.
     * @param request El DTO con los nuevos datos.
     * @return La entidad [Sucursal] actualizada, o `null` si la sucursal no existe.
     */
    @Transactional
    fun actualizarSucursal(id: Long, request: SucursalRequest): Sucursal? {
        val sucursalExistente = sucursalRepository.findById(id).orElse(null)

        return if (sucursalExistente != null) {
            // Se crea una nueva instancia de Direccion (o se actualiza la existente si JPA lo permite)
            val direccionActualizada = Direccion(
                direccionCompleta = request.direccion.direccionCompleta,
                ciudad = request.direccion.ciudad,
                latitud = request.direccion.latitud,
                longitud = request.direccion.longitud,
                pisoApto = request.direccion.pisoApto,
                notasEntrega = request.direccion.notasEntrega,
                barrio = request.direccion.barrio,
                codigoPostal = request.direccion.codigoPostal,
                tipoDireccion = request.direccion.tipoDireccion
            )

            // Se actualiza la Sucursal existente usando la función copy, preservando el ID.
            val sucursalActualizada = sucursalExistente.copy(
                nombre = request.nombre,
                direccion = direccionActualizada
            )

            sucursalRepository.save(sucursalActualizada)
        } else null
    }

    /**
     * 🗑️ Elimina una sucursal por su ID.
     *
     * @param id El ID de la sucursal a eliminar.
     * @return `true` si la sucursal fue eliminada exitosamente, `false` si no fue encontrada.
     */
    @Transactional
    fun eliminarSucursal(id: Long): Boolean {
        val existe = sucursalRepository.existsById(id)
        if (existe) {
            sucursalRepository.deleteById(id)
            return true
        }
        return false
    }

    // -------------------------------------------------------------------------
    // ## Funciones Geográficas
    // -------------------------------------------------------------------------

    /**
     * 🌐 Calcula la distancia de gran círculo (en km) entre dos pares de coordenadas
     * (latitud/longitud) utilizando la **fórmula de Haversine**.
     *
     * La fórmula de Haversine es:
     * $$ a = \sin^2(\frac{\Delta\phi}{2}) + \cos\phi_1 \cdot \cos\phi_2 \cdot \sin^2(\frac{\Delta\lambda}{2}) $$
     * $$ c = 2 \cdot \operatorname{atan2}(\sqrt{a}, \sqrt{1-a}) $$
     * $$ d = R \cdot c $$
     * Donde $R$ es el radio de la Tierra, $\phi$ es latitud, $\lambda$ es longitud, y $\Delta$ indica la diferencia.
     *
     * @param lat1 Latitud del punto 1.
     * @param lon1 Longitud del punto 1.
     * @param lat2 Latitud del punto 2.
     * @param lon2 Longitud del punto 2.
     * @return La distancia entre los dos puntos en kilómetros.
     */
    private fun calculateHaversineDistance(
        lat1: Double, lon1: Double,
        lat2: Double, lon2: Double
    ): Double {
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val lat1Rad = Math.toRadians(lat1)
        val lat2Rad = Math.toRadians(lat2)

        // Componente 'a' de Haversine
        val a = sin(dLat / 2) * sin(dLat / 2) +
                sin(dLon / 2) * sin(dLon / 2) * cos(lat1Rad) * cos(lat2Rad)

        // Componente 'c' (distancia angular)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        // Distancia total
        return EARTH_RADIUS_KM * c
    }

    /**
     * 📍 Busca el ID de la **sucursal más cercana** a un punto de recolección dado.
     *
     * Este método es esencial para la lógica de asignación inicial de solicitudes
     * al centro de operaciones geográficamente más conveniente.
     *
     * 1. Recupera todas las sucursales.
     * 2. Calcula la distancia a cada sucursal usando [calculateHaversineDistance].
     * 3. Retorna el ID de la sucursal con la distancia mínima.
     *
     * @param lat Latitud del punto de recolección.
     * @param lon Longitud del punto de recolección.
     * @return El [Long] ID de la sucursal más cercana, o `null` si no hay sucursales.
     */
    fun findNearestBranchId(lat: Double, lon: Double): Long? {
        val todasSucursales = sucursalRepository.findAll()

        // Si no hay sucursales, no podemos buscar la más cercana
        if (todasSucursales.isEmpty()) {
            return null
        }

        // Encuentra la sucursal que minimiza la distancia.
        val nearestSucursal = todasSucursales.minByOrNull { sucursal ->
            // Se usa Double.MAX_VALUE como valor predeterminado si lat/lon son nulos, para que se considere la más lejana.
            calculateHaversineDistance(
                lat, lon,
                sucursal.direccion?.latitud ?: Double.MAX_VALUE,
                sucursal.direccion?.longitud ?: Double.MAX_VALUE
            )
        }

        // Retorna el ID de la sucursal más cercana
        return nearestSucursal?.id
    }
}