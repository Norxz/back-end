package co.edu.unipiloto.backend.service

import co.edu.unipiloto.backend.model.Guia
import co.edu.unipiloto.backend.repository.GuiaRepository
import org.springframework.stereotype.Service
import java.util.*

/**
 * 🏷️ Servicio encargado de la lógica de negocio para la gestión de la entidad [Guia] (Guía de Envío).
 * Proporciona funcionalidades para la creación y búsqueda de guías.
 */
@Service
class GuiaService(
    private val guiaRepository: GuiaRepository // Inyección del repositorio de Guías
) {

    /**
     * Genera y crea una nueva entidad [Guia] con identificadores únicos.
     *
     * 1. Genera un identificador corto (`numeroGuia`) a partir de un UUID.
     * 2. Genera un número de seguimiento (`trackingNumber`) prefijado y también basado en un UUID.
     * 3. Persiste la nueva guía en la base de datos.
     *
     * @return La entidad [Guia] recién creada y persistida.
     */
    fun crearGuia(): Guia {
        // Genera un número de guía único (los primeros 10 caracteres del UUID)
        val numeroGuia = UUID.randomUUID().toString().substring(0, 10)
        // Genera un número de seguimiento con un prefijo y una parte del UUID
        val tracking = "TRK-${UUID.randomUUID().toString().take(12)}"

        val guia = Guia(
            numeroGuia = numeroGuia,
            trackingNumber = tracking
        )

        return guiaRepository.save(guia)
    }

    /**
     * Guarda o actualiza una entidad [Guia] existente en la base de datos.
     * Utilizado para actualizar la guía con la información del paquete y la solicitud.
     *
     * @param guia El objeto [Guia] a guardar/actualizar.
     * @return La entidad [Guia] persistida.
     */
    fun guardar(guia: Guia): Guia {
        return guiaRepository.save(guia)
    }

    /**
     * Busca una guía por su ID de clave primaria.
     *
     * @param id El ID interno de la guía.
     * @return La entidad [Guia] si es encontrada, o null.
     */
    fun buscarPorId(id: Long): Guia? {
        return guiaRepository.findById(id).orElse(null)
    }

    /**
     * Busca una guía por su número de guía único (`numeroGuia`).
     *
     * @param numero El número de guía a buscar.
     * @return La entidad [Guia] si es encontrada, o null.
     */
    fun buscarPorNumero(numero: String): Guia? {
        return guiaRepository.findByNumeroGuia(numero)
    }

    /**
     * Busca una guía por su número de seguimiento único (`trackingNumber`).
     *
     * @param tracking El número de seguimiento a buscar.
     * @return La entidad [Guia] si es encontrada, o null.
     */
    fun buscarPorTracking(tracking: String): Guia? {
        return guiaRepository.findByTrackingNumber(tracking)
    }
}