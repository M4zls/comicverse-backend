package com.comicverse.products.repository

import com.comicverse.products.SupabaseClient
import com.comicverse.products.models.*
import io.github.jan.supabase.postgrest.from
import org.springframework.stereotype.Repository

@Repository
class MangaRepository {

    suspend fun findAll(): List<Manga> {
        return SupabaseClient.client
            .from("mangas")
            .select()
            .decodeList<Manga>()
    }

    suspend fun findById(id: String): Manga {
        return SupabaseClient.client
            .from("mangas")
            .select {
                filter {
                    eq("id", id)
                }
            }
            .decodeSingle<Manga>()
    }

    suspend fun findByCategory(category: String): List<Manga> {
        return SupabaseClient.client
            .from("mangas")
            .select {
                filter {
                    eq("type", category)
                }
            }
            .decodeList<Manga>()
    }

    suspend fun save(request: CreateMangaRequest): Manga {
        return SupabaseClient.client
            .from("mangas")
            .insert(request) {
                select()
            }
            .decodeSingle<Manga>()
    }

    suspend fun update(id: String, request: UpdateMangaRequest): Manga {
        return SupabaseClient.client
            .from("mangas")
            .update(request) {
                filter {
                    eq("id", id)
                }
                select()
            }
            .decodeSingle<Manga>()
    }

    suspend fun deleteById(id: String) {
        SupabaseClient.client
            .from("mangas")
            .delete {
                filter {
                    eq("id", id)
                }
            }
    }
}
