package co.edu.unipiloto.backend.service

import co.edu.unipiloto.backend.exception.ResourceAlreadyExistsException
import co.edu.unipiloto.backend.exception.ResourceNotFoundException
import co.edu.unipiloto.backend.model.Cliente
import co.edu.unipiloto.backend.model.Solicitud
import co.edu.unipiloto.backend.repository.ClienteRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ClienteService(
    private val clienteRepository: ClienteRepository
) {

    /**
     * Crear un cliente, validando si ya existe por tipo y número de documento.
     */
    @Transactional
    fun crearCliente(cliente: Cliente): Cliente {

        if (clienteRepository.existsByNumeroId(cliente.numeroId)) {
            throw ResourceAlreadyExistsException(
                "Ya existe un cliente con número de documento ${cliente.numeroId}"
            )
        }

        return clienteRepository.save(cliente)
    }

    /**
     * Buscar un cliente por ID. Lanza excepción si no existe.
     */
    fun buscarPorId(id: Long): Cliente {
        return clienteRepository.findById(id).orElseThrow {
            ResourceNotFoundException("El cliente con ID $id no existe.")
        }
    }

    /**
     * Buscar cliente por documento.
     */
    fun buscarPorDocumento(tipo: String, numero: String): Cliente? {
        return clienteRepository.findByTipoIdAndNumeroId(tipo, numero)
    }

    /**
     * Buscar cliente por coincidencia en el nombre (útil para autocompletar).
     */
    fun buscarPorNombre(nombre: String): List<Cliente> {
        return clienteRepository.findByNombreContainingIgnoreCase(nombre)
    }

    /**
     * Listar todos los clientes.
     */
    fun listarTodos(): List<Cliente> {
        return clienteRepository.findAll()
    }

    fun obtenerSolicitudesDelCliente(id: Long): List<Solicitud> {
        val cliente = buscarPorId(id)
        return cliente.solicitudesComoRemitente + cliente.solicitudesComoReceptor
    }
}
