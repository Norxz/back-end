package co.edu.unipiloto.backend.controller

import co.edu.unipiloto.backend.dto.SucursalRequest
import co.edu.unipiloto.backend.dto.SucursalResponse
import co.edu.unipiloto.backend.service.SucursalService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 * Controlador para manejar todas las operaciones relacionadas con las sucursales.
 *
 * Permite listar, crear, obtener, actualizar y eliminar sucursales.
 */
@RestController
@RequestMapping("/api/v1/sucursales")
class SucursalController(
    private val sucursalService: SucursalService
) {

    /**
     * Lista todas las sucursales.
     *
     * Principalmente usado para llenar un Spinner en Android.
     *
     * @return Lista de [SucursalResponse]
     */
    @GetMapping
    fun listarSucursales(): ResponseEntity<List<SucursalResponse>> {
        val sucursales = sucursalService.listarTodas()
        val response = sucursales.map { SucursalResponse(it) }
        return ResponseEntity.ok(response)
    }

    /**
     * Crea una nueva sucursal.
     *
     * @param request DTO con los datos de la sucursal
     * @return [SucursalResponse] de la sucursal creada
     */
    @PostMapping
    fun crearSucursal(@RequestBody request: SucursalRequest): ResponseEntity<SucursalResponse> {
        val nuevaSucursal = sucursalService.crearSucursal(request)
        return ResponseEntity.ok(SucursalResponse(nuevaSucursal))
    }

    /**
     * Obtiene una sucursal por su ID.
     *
     * @param id ID de la sucursal
     * @return [SucursalResponse] o 404 si no existe
     */
    @GetMapping("/{id}")
    fun obtenerSucursal(@PathVariable id: Long): ResponseEntity<SucursalResponse> {
        val sucursal = sucursalService.obtenerPorId(id)
        return if (sucursal != null) {
            ResponseEntity.ok(SucursalResponse(sucursal))
        } else {
            ResponseEntity.notFound().build()
        }
    }

    /**
     * Actualiza una sucursal existente.
     *
     * @param id ID de la sucursal a actualizar
     * @param request DTO con los datos actualizados
     * @return [SucursalResponse] de la sucursal actualizada o 404 si no existe
     */
    @PutMapping("/{id}")
    fun actualizarSucursal(
        @PathVariable id: Long,
        @RequestBody request: SucursalRequest
    ): ResponseEntity<SucursalResponse> {
        val sucursalActualizada = sucursalService.actualizarSucursal(id, request)
        return if (sucursalActualizada != null) {
            ResponseEntity.ok(SucursalResponse(sucursalActualizada))
        } else {
            ResponseEntity.notFound().build()
        }
    }

    /**
     * Elimina una sucursal por su ID.
     *
     * @param id ID de la sucursal
     * @return 204 si se eliminó, 404 si no existe
     */
    @DeleteMapping("/{id}")
    fun eliminarSucursal(@PathVariable id: Long): ResponseEntity<Void> {
        val fueEliminada = sucursalService.eliminarSucursal(id)
        return if (fueEliminada) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }
}
