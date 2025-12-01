package com.comicverse.products.service

import com.comicverse.products.models.*
import com.comicverse.products.repository.MangaRepository
import org.springframework.stereotype.Service

@Service
class MangaService(
    private val mangaRepository: MangaRepository
) {

    suspend fun getAllMangas(): List<Manga> {
        return mangaRepository.findAll()
    }

    suspend fun getMangaById(id: String): Manga {
        return mangaRepository.findById(id)
    }

    suspend fun getMangasByCategory(category: String): List<Manga> {
        return mangaRepository.findByCategory(category)
    }

    suspend fun createManga(request: CreateMangaRequest): Manga {
        return mangaRepository.save(request)
    }

    suspend fun updateManga(id: String, request: UpdateMangaRequest): Manga {
        return mangaRepository.update(id, request)
    }

    suspend fun deleteManga(id: String) {
        mangaRepository.deleteById(id)
    }
}
