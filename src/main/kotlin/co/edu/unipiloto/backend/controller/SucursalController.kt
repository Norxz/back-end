package co.edu.unipiloto.backend.controller

import co.edu.unipiloto.backend.model.Sucursal
import co.edu.unipiloto.backend.repository.SucursalRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/sucursales")
class SucursalController(
    private val sucursalRepository: SucursalRepository
) {

    @PostMapping
    fun crearSucursal(@RequestBody request: Sucursal): ResponseEntity<Sucursal> {
        val nuevaSucursal = sucursalRepository.save(request)
        return ResponseEntity.ok(nuevaSucursal)
    }

    @GetMapping
    fun listarSucursales(): List<Sucursal> = sucursalRepository.findAll()

    @GetMapping("/{id}")
    fun obtenerSucursal(@PathVariable id: Long): ResponseEntity<Sucursal> {
        val sucursal = sucursalRepository.findById(id)
        return if (sucursal.isPresent) ResponseEntity.ok(sucursal.get())
        else ResponseEntity.notFound().build()
    }

    @PutMapping("/{id}")
    fun actualizarSucursal(
        @PathVariable id: Long,
        @RequestBody request: Sucursal
    ): ResponseEntity<Sucursal> {
        val existente = sucursalRepository.findById(id)
        if (!existente.isPresent) return ResponseEntity.notFound().build()

        val actualizada = existente.get().copy(
            nombre = request.nombre,
            direccion = request.direccion
        )

        return ResponseEntity.ok(sucursalRepository.save(actualizada))
    }

    @DeleteMapping("/{id}")
    fun eliminarSucursal(@PathVariable id: Long): ResponseEntity<Void> {
        return if (sucursalRepository.existsById(id)) {
            sucursalRepository.deleteById(id)
            ResponseEntity.noContent().build()
        } else ResponseEntity.notFound().build()
    }
}
