package co.edu.unipiloto.backend.exception

/**
 * ⚠️ Excepción personalizada lanzada cuando un **recurso** (ej. un registro de base de datos)
 * que debe poseer una **restricción de unicidad** ya existe al intentar crearlo.
 *
 * Ejemplos de uso:
 * - Intentar registrar un usuario con un email que ya está en uso.
 * - Intentar crear una guía con un `trackingNumber` duplicado.
 *
 * Cuando esta excepción es capturada en un [RestControllerAdvice] o Controller,
 * se utiliza generalmente para retornar un código de estado HTTP **409 Conflict** al cliente,
 * indicando que el conflicto se debe a la duplicidad del recurso.
 *
 * Hereda de [RuntimeException] para ser una excepción no chequeada (unchecked exception).
 *
 * @param message Mensaje descriptivo que indica el recurso y la razón del conflicto (ej. "El email ya está registrado").
 */
class ResourceAlreadyExistsException(message: String) : RuntimeException(message)