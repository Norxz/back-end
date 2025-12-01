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
    /** 🛍️ Usuario externo o registrado que utiliza los servicios para crear y rastrear solicitudes de envío. */
    CLIENTE,

    /** 👩‍💼 Usuario interno con funciones administrativas generales (ej. atención al cliente, facturación) que no incluyen asignación de rutas ni conducción. */
    FUNCIONARIO,

    /** 👨‍💻 Usuario interno clave en la logística; encargado de revisar, aceptar, y asignar solicitudes a conductores dentro de su sucursal. */
    GESTOR,

    /** 🚚 Usuario interno responsable de la recolección física y la entrega final de los paquetes (actualiza el estado de la ruta). */
    CONDUCTOR,

    /** 📈 Usuario interno encargado de la supervisión, reportes, análisis de rendimiento y eficiencia, sin funciones operativas directas sobre las solicitudes. */
    ANALISTA,

    /** 👑 **Máximo Nivel:** Usuario con acceso total a la configuración y gestión del sistema (usuarios, sucursales, configuraciones globales). */
    ADMIN
}
