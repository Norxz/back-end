// Archivo: co.edu.unipiloto.backend.controller.SucursalController.kt

package co.edu.unipiloto.backend.controller

import co.edu.unipiloto.backend.dto.NearestBranchResponse
import co.edu.unipiloto.backend.dto.SucursalRequest
import co.edu.unipiloto.backend.dto.SucursalResponse
import co.edu.unipiloto.backend.service.SucursalService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException // Importar ResponseStatusException

/**
 * Controlador REST para manejar todas las operaciones relacionadas con las sucursales.
 */
@RestController
@RequestMapping("/api/v1/sucursales")
class SucursalController(
    private val sucursalService: SucursalService
) {

    // --- 1. Listar todas ---
    @GetMapping
    fun listarSucursales(): ResponseEntity<List<SucursalResponse>> =
        sucursalService.listarTodas()
            .map { SucursalResponse(it) }
            .let { ResponseEntity.ok(it) }

    // --- 2. Crear nueva ---
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun crearSucursal(@RequestBody request: SucursalRequest): SucursalResponse {
        val nuevaSucursal = sucursalService.crearSucursal(request)
        return SucursalResponse(nuevaSucursal)
    }

    // -------------------------------------------------------------------
    // ✅ 3. OBTENER SUCURSAL MÁS CERCANA (NUEVO ENDPOINT)
    // -------------------------------------------------------------------
    /**
     * Busca el ID de la sucursal más cercana a las coordenadas dadas.
     * La ruta es /api/v1/sucursales/cercana?lat=X&lon=Y.
     */
    @GetMapping("/cercana")
    fun findNearestBranchId(
        @RequestParam lat: Double,
        @RequestParam lon: Double
    ): ResponseEntity<NearestBranchResponse> {

        val nearestId = sucursalService.findNearestBranchId(lat, lon)

        return nearestId?.let {
            ResponseEntity.ok(NearestBranchResponse(id = it))
        } ?: ResponseEntity.notFound().build()
    }

    // -------------------------------------------------------------------
    // ✅ 4. OBTENER POR ID (ENDPOINT ORIGINAL)
    // -------------------------------------------------------------------
    /**
     * Obtiene una sucursal por su ID.
     */
    @GetMapping("/{id}")
    fun obtenerSucursal(@PathVariable id: Long): ResponseEntity<SucursalResponse> {
        return sucursalService.obtenerPorId(id)
            ?.let { ResponseEntity.ok(SucursalResponse(it)) }
            ?: ResponseEntity.notFound().build()
    }

    // --- 5. Actualizar existente ---
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
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun eliminarSucursal(@PathVariable id: Long) {
        val fueEliminada = sucursalService.eliminarSucursal(id)
        if (!fueEliminada) {
            throw ResponseStatusException( // Usamos la clase importada
                HttpStatus.NOT_FOUND, "Sucursal no encontrada"
            )
        }
    }
}