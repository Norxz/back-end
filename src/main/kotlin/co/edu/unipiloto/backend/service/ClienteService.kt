package co.edu.unipiloto.backend.service

import co.edu.unipiloto.backend.exception.ResourceAlreadyExistsException
import co.edu.unipiloto.backend.exception.ResourceNotFoundException
import co.edu.unipiloto.backend.model.Cliente
import co.edu.unipiloto.backend.model.Solicitud
import co.edu.unipiloto.backend.repository.ClienteRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * 👨‍💼 Servicio de Spring (`@Service`) encargado de la lógica de negocio para la gestión de la entidad [Cliente].
 *
 * Proporciona métodos transaccionales y de consulta para **crear**, **buscar** y **listar clientes**,
 * además de gestionar las colecciones de solicitudes asociadas a cada uno.
 */
@Service
class ClienteService(
    // Inyección de dependencia del repositorio de clientes
    private val clienteRepository: ClienteRepository
) {

    // -------------------------------------------------------------------------
    // ## Operaciones Transaccionales (Escritura)
    // -------------------------------------------------------------------------

    /**
     * ➕ Crea un nuevo cliente en la base de datos.
     *
     * Implementa una validación crucial para asegurar que no exista otro cliente con el mismo
     * **número de documento**, previniendo duplicidad en el registro.
     *
     * @param cliente El objeto [Cliente] a ser creado.
     * @return El objeto [Cliente] persistido.
     * @throws ResourceAlreadyExistsException si ya existe un cliente con el mismo número de documento.
     */
    @Transactional
    fun crearCliente(cliente: Cliente): Cliente {

        // 1. Validar duplicados por número de documento usando el repositorio
        if (clienteRepository.existsByNumeroId(cliente.numeroId)) {
            throw ResourceAlreadyExistsException(
                "Ya existe un cliente con número de documento ${cliente.numeroId}"
            )
        }

        // 2. Guardar y retornar
        return clienteRepository.save(cliente)
    }

    // -------------------------------------------------------------------------
    // ## Operaciones de Consulta (Lectura)
    // -------------------------------------------------------------------------

    /**
     * 🔎 Busca un cliente por su **ID único**.
     *
     * @param id El ID del cliente a buscar.
     * @return La entidad [Cliente] si es encontrada.
     * @throws ResourceNotFoundException si el cliente con el ID especificado no existe.
     */
    fun buscarPorId(id: Long): Cliente {
        // Utiliza orElseThrow para lanzar ResourceNotFoundException si no se encuentra
        return clienteRepository.findById(id).orElseThrow {
            ResourceNotFoundException("El cliente con ID $id no existe.")
        }
    }

    /**
     * 💳 Busca un cliente utilizando la combinación de su **tipo y número de documento**.
     *
     * @param tipo El tipo de documento (ej: "CC", "NIT").
     * @param numero El número de documento.
     * @return La entidad [Cliente] si es encontrada, o `null` si no existe.
     */
    fun buscarPorDocumento(tipo: String, numero: String): Cliente? {
        return clienteRepository.findByTipoIdAndNumeroId(tipo, numero)
    }

    /**
     * 📝 Busca clientes cuyos **nombres** contengan el texto dado, ignorando mayúsculas/minúsculas.
     *
     * Útil para funcionalidades de autocompletado en interfaces de usuario.
     *
     * @param nombre El texto a buscar dentro del nombre del cliente.
     * @return Una lista de entidades [Cliente] que coinciden parcialmente con el nombre.
     */
    fun buscarPorNombre(nombre: String): List<Cliente> {
        return clienteRepository.findByNombreContainingIgnoreCase(nombre)
    }

    /**
     * 📋 Recupera la lista completa de **todos los clientes** registrados en el sistema.
     *
     * @return Una lista de todas las entidades [Cliente].
     */
    fun listarTodos(): List<Cliente> {
        return clienteRepository.findAll()
    }

    /**
     * 🔄 Obtiene todas las solicitudes ([Solicitud]) en las que el cliente participa,
     * ya sea como **remitente** (quien envía) o como **receptor** (quien recibe).
     *
     * @param id El ID del cliente.
     * @return Una lista combinada de las solicitudes donde el cliente es remitente o receptor.
     * @throws ResourceNotFoundException si el cliente no es encontrado.
     */
    fun obtenerSolicitudesDelCliente(id: Long): List<Solicitud> {
        // 1. Se busca el cliente para asegurar su existencia y cargar sus relaciones LAZY.
        val cliente = buscarPorId(id)

        // 2. Se combinan las dos colecciones de solicitudes.
        // Nota: Las colecciones 'solicitudesComoRemitente' y 'solicitudesComoReceptor' se cargan
        // aquí debido al acceso, si estaban configuradas como FetchType.LAZY.
        return cliente.solicitudesComoRemitente + cliente.solicitudesComoReceptor
    }
}