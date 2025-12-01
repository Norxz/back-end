package co.edu.unipiloto.backend.repository

import co.edu.unipiloto.backend.model.Solicitud
import co.edu.unipiloto.backend.model.enums.EstadoSolicitud
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.Instant
import java.util.Optional

/**
 * Repositorio para la entidad [Solicitud] (Solicitud de Servicio de Envío).
 * Proporciona métodos para CRUD y consultas específicas relacionadas con la gestión de solicitudes.
 */
@Repository
interface SolicitudRepository : JpaRepository<Solicitud, Long> {

    // --- Consultas por Rol / Usuario ---

    /**
     * Busca y recupera todas las solicitudes creadas por un cliente específico.
     *
     * @param clientId El ID del cliente (usuario) que creó la solicitud.
     * @return Una lista de entidades [Solicitud] asociadas a ese cliente.
     */
    fun findAllByClientId(clientId: Long): List<Solicitud>

    /**
     * Busca y recupera todas las solicitudes asociadas a una sucursal específica (donde se gestiona la solicitud).
     *
     * @param sucursalId El ID de la sucursal.
     * @return Una lista de entidades [Solicitud].
     */
    fun findAllBySucursalId(sucursalId: Long): List<Solicitud>

    /**
     * Busca y recupera todas las solicitudes asignadas a un conductor específico para su transporte.
     *
     * @param conductorId El ID del conductor.
     * @return Una lista de entidades [Solicitud].
     */
    fun findAllByConductorId(conductorId: Long): List<Solicitud>

    /**
     * Busca y recupera todas las solicitudes gestionadas por un Gestor específico.
     *
     * @param gestorId El ID del Gestor.
     * @return Una lista de entidades [Solicitud].
     */
    fun findAllByGestorId(gestorId: Long): List<Solicitud>

    /**
     * Busca y recupera todas las solicitudes gestionadas por un Funcionario específico.
     *
     * @param funcionarioId El ID del Funcionario.
     * @return Una lista de entidades [Solicitud].
     */
    fun findAllByFuncionarioId(funcionarioId: Long): List<Solicitud>

    // --- Consultas por Estado y Fechas ---

    /**
     * Busca y recupera todas las solicitudes que se encuentran en un estado determinado.
     *
     * @param estado El [EstadoSolicitud] por el que se desea filtrar (ej: PENDIENTE, EN_TRANSITO, ENTREGADO).
     * @return Una lista de entidades [Solicitud] que coinciden con el estado.
     */
    fun findAllByEstado(estado: EstadoSolicitud): List<Solicitud>

    /**
     * Busca y recupera todas las solicitudes creadas dentro de un rango de tiempo específico.
     *
     * @param start El instante de tiempo inicial (inclusivo).
     * @param end El instante de tiempo final (inclusivo).
     * @return Una lista de entidades [Solicitud] creadas entre las fechas dadas.
     */
    fun findAllByCreatedAtBetween(start: Instant, end: Instant): List<Solicitud>

    // --- Consultas por Relaciones de Entidad ---

    /**
     * Busca y recupera todas las solicitudes cuyo remitente coincide con el ID especificado.
     *
     * @param remitenteId El ID de la entidad Remitente.
     * @return Una lista de entidades [Solicitud] con el remitente especificado.
     */
    fun findAllByRemitenteId(remitenteId: Long): List<Solicitud>

    /**
     * Busca y recupera todas las solicitudes cuyo receptor coincide con el ID especificado.
     *
     * @param receptorId El ID de la entidad Receptor.
     * @return Una lista de entidades [Solicitud] con el receptor especificado.
     */
    fun findAllByReceptorId(receptorId: Long): List<Solicitud>

    /**
     * Busca una solicitud específica por el ID de la [Guia] asociada.
     *
     * @param guiaId El ID de la entidad Guia.
     * @return Un [Optional] que contiene la entidad [Solicitud] asociada a la guía, o vacío si no existe.
     */
    fun findByGuiaId(guiaId: Long): Optional<Solicitud>

    /**
     * Busca una solicitud específica a través del campo 'numeroGuia' de su entidad [Guia] relacionada.
     * Utiliza la propiedad de navegación de JPA: Solicitud -> Guia -> numeroGuia.
     *
     * @param numeroGuia El número de guía único.
     * @return Un [Optional] que contiene la entidad [Solicitud] encontrada, o vacío.
     */
    fun findByGuia_NumeroGuia(numeroGuia: String): Optional<Solicitud>

    /**
     * Busca una solicitud específica a través del campo 'trackingNumber' de su entidad [Guia] relacionada.
     * Utiliza la propiedad de navegación de JPA: Solicitud -> Guia -> trackingNumber.
     *
     * @param trackingNumber El número de seguimiento único.
     * @return Un [Optional] que contiene la entidad [Solicitud] encontrada, o vacío.
     */
    fun findByGuia_TrackingNumber(trackingNumber: String): Optional<Solicitud>

    /**
     * Busca solicitudes por el ID de la sucursal asociada y un estado específico.
     * @param sucursalId El ID de la sucursal (propiedad en la entidad Solicitud o sucursal.id).
     * @param estado El estado de la solicitud (String o Enum.name).
     * @return Lista de Solicitudes.
     */
    fun findBySucursalIdAndEstado(sucursalId: Long, estado: EstadoSolicitud): List<Solicitud>

    fun findByConductor_Id(conductorId: Long): List<Solicitud>
}