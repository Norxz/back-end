package co.edu.unipiloto.backend.service

import co.edu.unipiloto.backend.model.Guia
import co.edu.unipiloto.backend.repository.GuiaRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

/**
 * 🏷️ Servicio de Spring (`@Service`) encargado de la lógica de negocio para la gestión de la entidad [Guia] (Guía de Envío).
 *
 * Proporciona funcionalidades clave para la **creación de identificadores únicos** (número de guía y tracking number)
 * y la búsqueda de guías.
 */
@Service
class GuiaService(
    private val guiaRepository: GuiaRepository // Inyección del repositorio de Guías
) {

    // -------------------------------------------------------------------------
    // ## Operaciones de Creación y Persistencia
    // -------------------------------------------------------------------------

    /**
     * ➕ Genera y crea una nueva entidad [Guia] con identificadores únicos.
     *
     * Utiliza [UUID] para asegurar la unicidad del [numeroGuia] y el [trackingNumber]
     * antes de persistir la entidad con el estado inicial.
     *
     * @return La entidad [Guia] recién creada y persistida.
     */
    @Transactional
    fun crearGuia(): Guia {
        // 1. Genera un número de guía único (se toman los primeros 10 caracteres del UUID para hacerlo más corto)
        val numeroGuia = UUID.randomUUID().toString().substring(0, 10).uppercase(Locale.getDefault())

        // 2. Genera un número de seguimiento con un prefijo ('TRK-') y una parte del UUID (12 caracteres)
        val tracking = "TRK-${UUID.randomUUID().toString().take(12).uppercase(Locale.getDefault())}"

        // 3. Crea la instancia de la Guia (los campos de fecha se inicializan automáticamente)
        val guia = Guia(
            numeroGuia = numeroGuia,
            trackingNumber = tracking
        )

        // 4. Persiste y retorna
        return guiaRepository.save(guia)
    }

    /**
     * 💾 Guarda o actualiza una entidad [Guia] existente en la base de datos.
     *
     * Utilizado para actualizar la guía con información adicional o cambios de estado/costo
     * una vez asociada a una solicitud.
     *
     * @param guia El objeto [Guia] a guardar/actualizar. Si el `id` no es null, se actualiza; si es null, se crea.
     * @return La entidad [Guia] persistida.
     */
    @Transactional
    fun guardar(guia: Guia): Guia {
        return guiaRepository.save(guia)
    }

    // -------------------------------------------------------------------------
    // ## Operaciones de Consulta
    // -------------------------------------------------------------------------

    /**
     * 🆔 Busca una guía por su ID de clave primaria.
     *
     * @param id El ID interno de la guía.
     * @return La entidad [Guia] si es encontrada, o `null`.
     */
    fun buscarPorId(id: Long): Guia? {
        return guiaRepository.findById(id).orElse(null)
    }

    /**
     * 🔍 Busca una guía por su **número de guía único** (`numeroGuia`).
     *
     * @param numero El número de guía a buscar.
     * @return La entidad [Guia] si es encontrada, o `null`.
     */
    fun buscarPorNumero(numero: String): Guia? {
        return guiaRepository.findByNumeroGuia(numero)
    }

    /**
     * 🌐 Busca una guía por su **número de seguimiento único** (`trackingNumber`).
     *
     * Este es el método usado para el rastreo público por parte de los clientes.
     *
     * @param tracking El número de seguimiento a buscar.
     * @return La entidad [Guia] si es encontrada, o `null`.
     */
    fun buscarPorTracking(tracking: String): Guia? {
        return guiaRepository.findByTrackingNumber(tracking)
    }
}