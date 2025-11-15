package co.edu.unipiloto.backend.service

import co.edu.unipiloto.backend.model.Guia
import co.edu.unipiloto.backend.repository.GuiaRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class GuiaService(
    private val guiaRepository: GuiaRepository
) {

    fun crearGuia(): Guia {
        val numeroGuia = UUID.randomUUID().toString().substring(0, 10)
        val tracking = "TRK-${UUID.randomUUID().toString().take(12)}"

        val guia = Guia(
            numeroGuia = numeroGuia,
            trackingNumber = tracking
        )

        return guiaRepository.save(guia)
    }

    fun guardar(guia: Guia): Guia {
        return guiaRepository.save(guia)
    }

    fun buscarPorId(id: Long): Guia? {
        return guiaRepository.findById(id).orElse(null)
    }

    fun buscarPorNumero(numero: String): Guia? {
        return guiaRepository.findByNumeroGuia(numero)
    }

    fun buscarPorTracking(tracking: String): Guia? {
        return guiaRepository.findByTrackingNumber(tracking)
    }
}
