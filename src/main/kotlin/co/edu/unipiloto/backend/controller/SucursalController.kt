package co.edu.unipiloto.backend.controller

import co.edu.unipiloto.backend.dto.SucursalRequest
import co.edu.unipiloto.backend.dto.SucursalResponse
import co.edu.unipiloto.backend.service.SucursalService
import org.springframework.http.HttpStatus // Importar HttpStatus para claridad
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 * Controlador REST para manejar todas las operaciones relacionadas con las sucursales.
 * Delega la lógica de negocio al SucursalService.
 */
@RestController
@RequestMapping("/api/v1/sucursales")
class SucursalController(
    private val sucursalService: SucursalService
) {

    // --- 1. Listar todas ---

    /**
     * Lista todas las sucursales (usado para Spinners/selección).
     * @return 200 OK con lista de [SucursalResponse].
     */
    @GetMapping
    fun listarSucursales(): ResponseEntity<List<SucursalResponse>> =
        sucursalService.listarTodas()
            .map { SucursalResponse(it) }
            .let { ResponseEntity.ok(it) } // Uso de let para retornar el ResponseEntity

    // --- 2. Crear nueva ---

    /**
     * Crea una nueva sucursal.
     * @param request DTO con los datos de la sucursal
     * @return 201 Created (usado para POST) con [SucursalResponse].
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // Indica explícitamente el código 201
    fun crearSucursal(@RequestBody request: SucursalRequest): SucursalResponse {
        val nuevaSucursal = sucursalService.crearSucursal(request)
        return SucursalResponse(nuevaSucursal)
        // Spring convierte la respuesta a 201 Created gracias a @ResponseStatus
    }

    // --- 3. Obtener por ID ---

    /**
     * Obtiene una sucursal por su ID.
     * @param id ID de la sucursal.
     * @return 200 OK con [SucursalResponse] o 404 Not Found.
     */
    @GetMapping("/{id}")
    fun obtenerSucursal(@PathVariable id: Long): ResponseEntity<SucursalResponse> {
        return sucursalService.obtenerPorId(id)
            ?.let { ResponseEntity.ok(SucursalResponse(it)) } // Si existe, mapea y retorna 200 OK
            ?: ResponseEntity.notFound().build() // Si es null, retorna 404 Not Found
    }

    // --- 4. Actualizar existente ---

    /**
     * Actualiza una sucursal existente.
     * @param id ID de la sucursal a actualizar.
     * @param request DTO con los datos actualizados.
     * @return 200 OK con [SucursalResponse] de la sucursal actualizada o 404.
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

    // --- 5. Eliminar por ID ---

    /**
     * Elimina una sucursal por su ID.
     * @param id ID de la sucursal.
     * @return 204 No Content si se eliminó, 404 Not Found si no existe.
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // Simplifica la respuesta 204
    fun eliminarSucursal(@PathVariable id: Long) {
        // En lugar de devolver un ResponseEntity<Void>, podemos simplificar la función
        // y lanzar una excepción si no se encuentra (la cual Spring convierte a 404),
        // o usar el enfoque actual para devolver un 404 manual.
        val fueEliminada = sucursalService.eliminarSucursal(id)
        if (!fueEliminada) {
            // Se puede lanzar una excepción personalizada aquí,
            // pero para mantener tu estructura de 404 manual:
            throw org.springframework.web.server.ResponseStatusException(
                HttpStatus.NOT_FOUND, "Sucursal no encontrada"
            )
        }
        // Si no lanza excepción y retorna, Spring usa 204 No Content gracias a @ResponseStatus.
    }
}