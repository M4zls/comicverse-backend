package com.comicverse.products.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import io.swagger.v3.oas.models.servers.Server
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfig {

    @Bean
    fun customOpenAPI(): OpenAPI {
        return OpenAPI()
            .info(
                Info()
                    .title("ComicVerse API")
                    .version("1.0.0")
                    .description("""
                        API REST para la aplicación ComicVerse - Tienda de Manga y Comics
                        
                        Esta API proporciona endpoints para:
                        - Gestión de productos (mangas/comics)
                        - Gestión de usuarios
                        - Procesamiento de pedidos y ventas
                        
                        Documentación completa de todos los endpoints disponibles.
                    """.trimIndent())
                    .contact(
                        Contact()
                            .name("ComicVerse Team")
                            .email("support@comicverse.com")
                    )
                    .license(
                        License()
                            .name("MIT License")
                            .url("https://opensource.org/licenses/MIT")
                    )
            )
            .servers(
                listOf(
                    Server()
                        .url("http://localhost:8080")
                        .description("Servidor Local de Desarrollo"),
                    Server()
                        .url("https://api.comicverse.com")
                        .description("Servidor de Producción")
                )
            )
    }
}
