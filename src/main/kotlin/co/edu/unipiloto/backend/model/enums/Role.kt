package co.edu.unipiloto.backend.model.enums

/**
 * Representa los roles de los usuarios en el sistema.
 *
 * - [CLIENTE]: Usuario que solicita servicios de envío.
 * - [FUNCIONARIO]: Usuario administrativo general (sin permisos de gestor/conductor).
 * - [GESTOR]: Encargado de gestionar solicitudes y asignar conductores.
 * - [CONDUCTOR]: Usuario encargado de recolectar y entregar paquetes.
 * - [ANALISTA]: Usuario encargado de análisis o supervisión, sin funciones operativas directas.
 * - [ADMIN]: Administrador del sistema con todos los permisos.
 */
enum class Role {
    CLIENTE,
    FUNCIONARIO,
    GESTOR,
    CONDUCTOR,
    ANALISTA,
    ADMIN
}
