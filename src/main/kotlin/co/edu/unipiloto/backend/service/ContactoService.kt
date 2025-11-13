package co.edu.unipiloto.backend.service

import co.edu.unipiloto.backend.model.Contacto
import co.edu.unipiloto.backend.repository.ContactoRepository
import org.springframework.stereotype.Service
import java.util.Optional

/**
 * Servicio de negocio para gestionar las operaciones relacionadas con los Contactos
 * (pueden ser remitentes o destinatarios).
 */
@Service
class ContactoService(
    private val contactoRepository: ContactoRepository
) {

    /**
     * Busca un contacto por su número de identificación.
     */
    fun buscarPorNumeroIdentificacion(numeroIdentificacion: String): Optional<Contacto> {
        val contacto: Contacto? = contactoRepository.findByNumeroIdentificacion(numeroIdentificacion)
        return Optional.ofNullable(contacto)
    }

    /**
     * Guarda un nuevo contacto o actualiza uno existente.
     */
    fun guardar(contacto: Contacto): Contacto {
        return contactoRepository.save(contacto)
    }
}
