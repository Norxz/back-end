package co.edu.unipiloto.backend.exception

/**
 * Excepción lanzada cuando un recurso específico (ej. User, Solicitud, etc.)
 * buscado por ID no existe en la base de datos.
 *
 * Mapea a un código de estado HTTP 404 Not Found.
 */
class ResourceNotFoundException(message: String) : RuntimeException(message)