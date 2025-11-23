package co.edu.unipiloto.backend.controller

import co.edu.unipiloto.backend.model.User
import co.edu.unipiloto.backend.repository.UserRepository
import co.edu.unipiloto.backend.repository.SucursalRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/users")
class UserController(
    private val userRepository: UserRepository,
    private val sucursalRepository: SucursalRepository
) {

    @PutMapping("/{userId}/sucursal/{sucursalId}")
    fun asignarSucursal(
        @PathVariable userId: Long,
        @PathVariable sucursalId: Long
    ): ResponseEntity<User> {

        val user = userRepository.findById(userId)
        if (!user.isPresent) return ResponseEntity.notFound().build()

        val sucursal = sucursalRepository.findById(sucursalId)
        if (!sucursal.isPresent) return ResponseEntity.notFound().build()

        val actualizado = user.get().copy(
            sucursal = sucursal.get()
        )

        return ResponseEntity.ok(userRepository.save(actualizado))
    }
}
