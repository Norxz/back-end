package co.edu.unipiloto.backend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

/**
 * 🚀 Clase principal de la aplicación Spring Boot para el backend del sistema de envíos.
 *
 * La anotación `@SpringBootApplication` combina:
 * 1. `@Configuration`: Marca la clase como una fuente de definición de beans.
 * 2. `@EnableAutoConfiguration`: Habilita la configuración automática de Spring Boot
 * basándose en las dependencias del classpath (e.g., configuración de JPA, Spring MVC).
 * 3. `@ComponentScan`: Busca componentes (clases con @Component, @Service, @Controller, etc.)
 * en el paquete actual y sus subpaquetes.
 */
@SpringBootApplication
class BackEndApplication

/**
 * Función principal (`main`) de Kotlin que sirve como punto de entrada de la aplicación.
 *
 * Utiliza la función de extensión de Spring Boot `runApplication` para inicializar y arrancar
 * el servidor (generalmente embebido como Tomcat o Jetty) que aloja la aplicación Spring.
 *
 * @param args Argumentos de línea de comandos pasados al iniciar la aplicación.
 */
fun main(args: Array<String>) {
    runApplication<BackEndApplication>(*args)
}