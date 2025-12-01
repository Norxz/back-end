// Archivo: co.edu.unipiloto.backend.controller.SucursalController.kt

package co.edu.unipiloto.backend.controller

import co.edu.unipiloto.backend.dto.NearestBranchResponse
import co.edu.unipiloto.backend.dto.SucursalRequest
import co.edu.unipiloto.backend.dto.SucursalResponse
import co.edu.unipiloto.backend.service.SucursalService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

/**
 * 🏢 Controlador REST para manejar todas las operaciones CRUD y consultas
 * de geolocalización relacionadas con las **Sucursales** de la compañía.
 *
 * Mapea a la ruta base: `/api/v1/sucursales`
 *
 * @property sucursalService El servicio que contiene la lógica de negocio para las sucursales.
 */
@RestController
@RequestMapping("/api/v1/sucursales")
class SucursalController(
    private val sucursalService: SucursalService
) {

    // --- 1. Listar todas ---
    /**
     * 📋 Obtiene la lista de todas las sucursales registradas.
     *
     * Mapea a: `GET /api/v1/sucursales`
     *
     * @return [ResponseEntity] con la lista de [SucursalResponse] y HTTP 200 OK.
     */
    @GetMapping
    fun listarSucursales(): ResponseEntity<List<SucursalResponse>> =
        sucursalService.listarTodas()
            .map { SucursalResponse(it) } // Convierte cada entidad a su DTO de respuesta
            .let { ResponseEntity.ok(it) }

    // --- 2. Crear nueva ---
    /**
     * ➕ Crea una nueva sucursal en el sistema.
     *
     * Mapea a: `POST /api/v1/sucursales`
     *
     * @param request DTO ([SucursalRequest]) con los detalles de la nueva sucursal.
     * @return La [SucursalResponse] creada y HTTP **201 CREATED**.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // Indica el código de estado de éxito
    fun crearSucursal(@RequestBody request: SucursalRequest): SucursalResponse {
        val nuevaSucursal = sucursalService.crearSucursal(request)
        return SucursalResponse(nuevaSucursal)
    }

    // -------------------------------------------------------------------
    // ✅ 3. OBTENER SUCURSAL MÁS CERCANA (GEOLOCALIZACIÓN)
    // -------------------------------------------------------------------
    /**
     * 📍 Busca la sucursal activa más cercana a las coordenadas geográficas dadas
     * (latitud y longitud). Este endpoint es crucial para asignar una solicitud
     * a la sucursal de origen más conveniente.
     *
     * Mapea a: `GET /api/v1/sucursales/cercana?lat=X&lon=Y`
     *
     * @param lat Latitud (coordenada Y) del punto de referencia.
     * @param lon Longitud (coordenada X) del punto de referencia.
     * @return [ResponseEntity] con el ID de la sucursal más cercana ([NearestBranchResponse]) y HTTP 200 OK,
     * o HTTP **404 NOT FOUND** si no se encuentra ninguna sucursal activa.
     */
    @GetMapping("/cercana")
    fun findNearestBranchId(
        @RequestParam lat: Double,
        @RequestParam lon: Double
    ): ResponseEntity<NearestBranchResponse> {

        // Llama al servicio para calcular la distancia y encontrar el ID.
        val nearestId = sucursalService.findNearestBranchId(lat, lon)

        // Si se encuentra el ID, retorna 200 OK con el ID.
        return nearestId?.let {
            ResponseEntity.ok(NearestBranchResponse(id = it))
        } ?: ResponseEntity.notFound().build() // Si es null, retorna 404 Not Found.
    }

    // -------------------------------------------------------------------
    // ✅ 4. OBTENER POR ID
    // -------------------------------------------------------------------
    /**
     * 🔎 Obtiene los detalles de una sucursal específica por su identificador.
     *
     * Mapea a: `GET /api/v1/sucursales/{id}`
     *
     * @param id ID de la sucursal.
     * @return [ResponseEntity] con la [SucursalResponse] y HTTP 200 OK si se encuentra,
     * o HTTP **404 NOT FOUND** si no existe.
     */
    @GetMapping("/{id}")
    fun obtenerSucursal(@PathVariable id: Long): ResponseEntity<SucursalResponse> {
        return sucursalService.obtenerPorId(id)
            // Si la sucursal existe (no es null), mapea a DTO y retorna 200 OK.
            ?.let { ResponseEntity.ok(SucursalResponse(it)) }
        // Si es null, retorna 404 Not Found.
            ?: ResponseEntity.notFound().build()
    }

    // --- 5. Actualizar existente ---
    /**
     * 🔄 Actualiza la información de una sucursal existente.
     *
     * Mapea a: `PUT /api/v1/sucursales/{id}`
     *
     * @param id ID de la sucursal a actualizar.
     * @param request DTO ([SucursalRequest]) con los nuevos datos.
     * @return [ResponseEntity] con la [SucursalResponse] actualizada y HTTP 200 OK,
     * o HTTP **404 NOT FOUND** si la sucursal no existe.
     */
    @PutMapping("/{id}")
    fun actualizarSucursal(
        @PathVariable id: Long,
        @RequestBody request: SucursalRequest
    ): ResponseEntity<SucursalResponse> {
        return sucursalService.actualizarSucursal(id, request)
            ?.let { ResponseEntity.ok(SucursalResponse(it)) }
            ?: ResponseEntity.notFound().build()
    }

    // --- 6. Eliminar por ID ---
    /**
     * 🗑️ Elimina permanentemente una sucursal del sistema por su ID.
     *
     * Mapea a: `DELETE /api/v1/sucursales/{id}`
     *
     * @param id ID de la sucursal a eliminar.
     * @return HTTP **204 NO CONTENT** si la eliminación es exitosa.
     * @throws ResponseStatusException (HTTP **404 NOT FOUND**) si la sucursal no existe.
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun eliminarSucursal(@PathVariable id: Long) {
        val fueEliminada = sucursalService.eliminarSucursal(id)
        if (!fueEliminada) {
            // Lanza una excepción que Spring Boot mapea automáticamente a una respuesta 404
            throw ResponseStatusException(
                HttpStatus.NOT_FOUND, "Sucursal no encontrada"
            )
        }
    }
}