package co.edu.unipiloto.backend.repository

import co.edu.unipiloto.backend.model.Solicitud
import co.edu.unipiloto.backend.model.Sucursal
import org.springframework.data.jpa.repository.JpaRepository

interface SucursalRepository : JpaRepository<Sucursal, Long>