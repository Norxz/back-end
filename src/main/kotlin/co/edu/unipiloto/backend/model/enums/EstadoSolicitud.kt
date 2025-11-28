package co.edu.unipiloto.backend.model.enums

/**
 * Representa los posibles estados de una solicitud de envío en el sistema.
 *
 * - [PENDIENTE]: Solicitud creada pero aún no asignada a un gestor.
 * - [ASIGNADA]: Solicitud asignada a un gestor pero no ha iniciado la recolección.
 * - [EN_RECOLECCION]: El conductor ha iniciado la recolección del paquete.
 * - [RECOLECTADA]: El paquete ha sido recogido por el conductor.
 * - [EN_DISTRIBUCION]: El paquete está en tránsito hacia el destinatario.
 * - [ENTREGADA]: El paquete ha sido entregado exitosamente al destinatario.
 * - [CANCELADA]: La solicitud fue cancelada antes de su entrega.
 */
enum class EstadoSolicitud {
    PENDIENTE,
    ASIGNADA,
    EN_RECOLECCION,
    RECOLECTADA,
    EN_DISTRIBUCION,
    ENTREGADA,
    CANCELADA
}
