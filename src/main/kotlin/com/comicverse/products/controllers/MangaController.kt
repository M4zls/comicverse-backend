package com.comicverse.products.controllers

import com.comicverse.products.models.*
import com.comicverse.products.service.MangaService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/mangas")
@CrossOrigin(origins = ["*"])
class MangaController(
    private val mangaService: MangaService
) {

    @GetMapping
    suspend fun getAllMangas(): ResponseEntity<List<Manga>> {
        return try {
            val mangas = mangaService.getAllMangas()
            ResponseEntity.ok(mangas)
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(emptyList())
        }
    }

    @GetMapping("/{id}")
    suspend fun getMangaById(@PathVariable id: String): ResponseEntity<Manga> {
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

    @PostMapping
    suspend fun createManga(@RequestBody request: CreateMangaRequest): ResponseEntity<Manga> {
        return try {
            val manga = mangaService.createManga(request)
            ResponseEntity.status(HttpStatus.CREATED).body(manga)
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
        }
    }

    @PutMapping("/{id}")
    suspend fun updateManga(
        @PathVariable id: String,
        @RequestBody request: UpdateMangaRequest
    ): ResponseEntity<Manga> {
        return try {
            val manga = mangaService.updateManga(id, request)
            ResponseEntity.ok(manga)
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
    }

    @DeleteMapping("/{id}")
    suspend fun deleteManga(@PathVariable id: String): ResponseEntity<Void> {
        return try {
            mangaService.deleteManga(id)
            ResponseEntity.noContent().build()
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
    }
}
