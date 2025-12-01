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
    /** Creada, pendiente de ser asignada a un gestor o ruta. */
    PENDIENTE,

    /** Asignada a un gestor o conductor; esperando el inicio de la operación. */
    ASIGNADA,

    /** El conductor está en camino para recoger el paquete del remitente. */
    EN_RUTA_RECOLECCION,

    /** El paquete ha sido recogido y está en tránsito hacia el centro de distribución/ruta. */
    EN_DISTRIBUCION,

    /** El paquete salió del centro de distribución y está en ruta final hacia el destinatario. */
    EN_RUTA_REPARTO,

    /** Finalizada exitosamente. */
    ENTREGADA,

    /** Finalizada sin éxito (cancelada por cliente o por logística). */
    CANCELADA
}
