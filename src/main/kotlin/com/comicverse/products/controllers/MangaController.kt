package com.comicverse.products.controllers

import com.comicverse.products.models.*
import com.comicverse.products.service.MangaService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/mangas")
@CrossOrigin(origins = ["*"])
@Tag(name = "Productos", description = "API de gestión de productos (Mangas/Comics)")
class MangaController(
    private val mangaService: MangaService
) {

    @Operation(
        summary = "Obtener todos los productos",
        description = "Retorna la lista completa de todos los mangas/comics disponibles en el catálogo"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Lista de productos obtenida exitosamente"),
        ApiResponse(responseCode = "500", description = "Error interno del servidor", content = [Content()])
    ])
    @GetMapping
    suspend fun getAllMangas(): ResponseEntity<List<Manga>> {
        return try {
            val mangas = mangaService.getAllMangas()
            ResponseEntity.ok(mangas)
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(emptyList())
        }
    }

    @Operation(
        summary = "Obtener producto por ID",
        description = "Retorna un producto específico buscado por su identificador único"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Producto encontrado"),
        ApiResponse(responseCode = "404", description = "Producto no encontrado", content = [Content()])
    ])
    @GetMapping("/{id}")
    suspend fun getMangaById(
        @Parameter(description = "ID único del producto", required = true)
        @PathVariable id: String
    ): ResponseEntity<Manga> {
        return try {
            val manga = mangaService.getMangaById(id)
            ResponseEntity.ok(manga)
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
    }

    @GetMapping("/category/{category}")
    suspend fun getMangasByCategory(@PathVariable category: String): ResponseEntity<List<Manga>> {
        return try {
            val mangas = mangaService.getMangasByCategory(category)
            ResponseEntity.ok(mangas)
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(emptyList())
        }
    }

    @Operation(
        summary = "Crear nuevo producto",
        description = "Crea un nuevo manga/comic en el catálogo con la información proporcionada"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "201", description = "Producto creado exitosamente"),
        ApiResponse(responseCode = "400", description = "Datos inválidos", content = [Content()])
    ])
    @PostMapping
    suspend fun createManga(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Datos del nuevo producto a crear",
            required = true
        )
        @RequestBody request: CreateMangaRequest
    ): ResponseEntity<Manga> {
        return try {
            val manga = mangaService.createManga(request)
            ResponseEntity.status(HttpStatus.CREATED).body(manga)
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
        }
    }

    @Operation(
        summary = "Actualizar producto",
        description = "Actualiza la información de un producto existente"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Producto actualizado exitosamente"),
        ApiResponse(responseCode = "404", description = "Producto no encontrado", content = [Content()])
    ])
    @PutMapping("/{id}")
    suspend fun updateManga(
        @Parameter(description = "ID del producto a actualizar", required = true)
        @PathVariable id: String,
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Datos actualizados del producto"
        )
        @RequestBody request: UpdateMangaRequest
    ): ResponseEntity<Manga> {
        return try {
            val manga = mangaService.updateManga(id, request)
            ResponseEntity.ok(manga)
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
    }

    @Operation(
        summary = "Eliminar producto",
        description = "Elimina un producto del catálogo de forma permanente"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "204", description = "Producto eliminado exitosamente"),
        ApiResponse(responseCode = "404", description = "Producto no encontrado", content = [Content()])
    ])
    @DeleteMapping("/{id}")
    suspend fun deleteManga(
        @Parameter(description = "ID del producto a eliminar", required = true)
        @PathVariable id: String
    ): ResponseEntity<Void> {
        return try {
            mangaService.deleteManga(id)
            ResponseEntity.noContent().build()
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
    }
}
