package co.edu.unipiloto.backend.exception

/**
 * Excepción lanzada cuando un recurso (como un email, ID de guía, etc.)
 * que debe ser único ya existe en la base de datos.
 *
 * Usado para retornar un código de estado HTTP 409 Conflict al cliente.
 */
class ResourceAlreadyExistsException(message: String) : RuntimeException(message)