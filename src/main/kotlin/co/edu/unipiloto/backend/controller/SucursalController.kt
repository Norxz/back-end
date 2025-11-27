package co.edu.unipiloto.backend.controller

import co.edu.unipiloto.backend.dto.SucursalRequest
import co.edu.unipiloto.backend.dto.SucursalResponse
import co.edu.unipiloto.backend.service.SucursalService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/sucursales")
class SucursalController(
    private val sucursalService: SucursalService
) {

    // 1. Listar todas (Para llenar el Spinner en Android)
    @GetMapping
    fun listarSucursales(): ResponseEntity<List<SucursalResponse>> {
        val sucursales = sucursalService.listarTodas()
        // Convertimos Entidades a DTOs de respuesta
        val response = sucursales.map { SucursalResponse(it) }
        return ResponseEntity.ok(response)
    }

    // 2. Crear una nueva sucursal
    @PostMapping
    fun crearSucursal(@RequestBody request: SucursalRequest): ResponseEntity<SucursalResponse> {
        val nuevaSucursal = sucursalService.crearSucursal(request)
        return ResponseEntity.ok(SucursalResponse(nuevaSucursal))
    }

    // 3. Obtener una por ID
    @GetMapping("/{id}")
    fun obtenerSucursal(@PathVariable id: Long): ResponseEntity<SucursalResponse> {
        val sucursal = sucursalService.obtenerPorId(id)
        return if (sucursal != null) {
            ResponseEntity.ok(SucursalResponse(sucursal))
        } else {
            ResponseEntity.notFound().build()
        }
    }

    // 4. Actualizar una sucursal
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

    // 5. Eliminar una sucursal
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