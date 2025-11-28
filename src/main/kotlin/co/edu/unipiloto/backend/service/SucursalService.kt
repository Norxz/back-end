package co.edu.unipiloto.backend.service

import co.edu.unipiloto.backend.dto.SucursalRequest
import co.edu.unipiloto.backend.model.Direccion
import co.edu.unipiloto.backend.model.Sucursal
import co.edu.unipiloto.backend.repository.SucursalRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * 🏢 Servicio encargado de la lógica de negocio para la gestión de la entidad [Sucursal] (Oficina o Punto de Servicio).
 * Proporciona funcionalidades completas para CRUD (Crear, Leer, Actualizar, Eliminar) de sucursales.
 */
@Service
class SucursalService(
    private val sucursalRepository: SucursalRepository // Inyección del repositorio de Sucursales
) {

    /**
     * 📋 Recupera una lista de todas las sucursales registradas en el sistema.
     *
     * @return Una [List] de todas las entidades [Sucursal].
     */
    fun listarTodas(): List<Sucursal> = sucursalRepository.findAll()

    /**
     * ➕ Crea una nueva sucursal a partir de los datos de la solicitud [SucursalRequest].
     *
     * 1. Crea una nueva entidad [Direccion] a partir del DTO anidado.
     * 2. Crea la entidad [Sucursal] enlazándola con la [Direccion] recién creada.
     * 3. Persiste la [Sucursal] en la base de datos.
     *
     * @param request El DTO con los datos de la nueva sucursal.
     * @return La entidad [Sucursal] recién creada y persistida.
     */
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

        // Creación de la entidad Sucursal
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
     * @return La entidad [Sucursal] si es encontrada, o null.
     */
    fun obtenerPorId(id: Long): Sucursal? =
        sucursalRepository.findById(id).orElse(null)

    /**
     * ✏️ Actualiza la información de una sucursal existente (nombre y datos de dirección).
     *
     * 1. Busca la sucursal existente por ID.
     * 2. Si existe, crea una nueva entidad [Direccion] con los datos actualizados del DTO.
     * 3. Utiliza la función `copy()` de Kotlin (en el `data class` Sucursal) para crear una instancia
     * actualizada con los nuevos valores de `nombre` y `direccion`.
     * 4. Persiste la sucursal actualizada.
     *
     * @param id El ID de la sucursal a actualizar.
     * @param request El DTO con los nuevos datos.
     * @return La entidad [Sucursal] actualizada, o null si la sucursal no existe.
     */
    @Transactional
    fun actualizarSucursal(id: Long, request: SucursalRequest): Sucursal? {
        val sucursalExistente = sucursalRepository.findById(id).orElse(null)

        return if (sucursalExistente != null) {
            // Se crea una nueva entidad Direccion o se actualiza la existente (depende de la configuración JPA)
            // Aquí se crea una nueva instancia de Direccion con los datos del request.
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
     * 1. Verifica si la sucursal existe.
     * 2. Si existe, la elimina y retorna true.
     * 3. Si no existe, retorna false.
     *
     * @param id El ID de la sucursal a eliminar.
     * @return true si la sucursal fue eliminada exitosamente, false si no fue encontrada.
     */
    fun eliminarSucursal(id: Long): Boolean {
        val existe = sucursalRepository.existsById(id)
        if (existe) {
            sucursalRepository.deleteById(id)
            return true
        }
        return false
    }
}