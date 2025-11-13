package co.edu.unipiloto.backend.repository

import co.edu.unipiloto.backend.model.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {

    fun findByEmail(email: String): User?

    fun findByEmailAndPasswordHash(email: String, passwordHash: String): User?
}