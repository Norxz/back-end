package co.edu.unipiloto.backend.exception

/**
 * 🔍 Excepción personalizada lanzada cuando se intenta acceder o manipular
 * un **recurso** (entidad de base de datos, ej. [User], [Solicitud], [Sucursal])
 * utilizando un identificador (ID, tracking number) que **no existe** en el sistema.
 *
 * Ejemplos de uso:
 * - Intentar obtener una solicitud con un ID que no se encuentra.
 * - Intentar actualizar una sucursal que ha sido eliminada.
 *
 * Cuando esta excepción es capturada en un [RestControllerAdvice] o Controller,
 * se utiliza universalmente para retornar un código de estado HTTP **404 Not Found** al cliente,
 * indicando que la URL o el recurso solicitado no está disponible.
 *

[Image of 404 error page]

 *
 * Hereda de [RuntimeException] para ser una excepción no chequeada (unchecked exception).
 *
 * @param message Mensaje descriptivo que indica el recurso específico que no pudo ser encontrado (ej. "Solicitud ID 15 no encontrada").
 */
class ResourceNotFoundException(message: String) : RuntimeException(message)