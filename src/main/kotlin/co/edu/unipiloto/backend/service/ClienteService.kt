package co.edu.unipiloto.backend.service

import co.edu.unipiloto.backend.model.Cliente
import co.edu.unipiloto.backend.repository.ClienteRepository
import org.springframework.stereotype.Service

@Service
class ClienteService(
    private val clienteRepository: ClienteRepository
) {

    fun crearCliente(cliente: Cliente): Cliente {
        return clienteRepository.save(cliente)
    }

    fun buscarPorId(id: Long): Cliente? {
        return clienteRepository.findById(id).orElse(null)
    }

    fun buscarPorDocumento(tipo: String, numero: String): Cliente? {
        return clienteRepository.findByTipoIdAndNumeroId(tipo, numero)
    }

    fun buscarPorNombre(nombre: String): List<Cliente> {
        return clienteRepository.findByNombreContainingIgnoreCase(nombre)
    }

    fun listarTodos(): List<Cliente> {
        return clienteRepository.findAll()
    }
}
