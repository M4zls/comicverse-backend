package com.comicverse.products.repository

import com.comicverse.products.SupabaseClient
import com.comicverse.products.models.*
import io.github.jan.supabase.postgrest.from
import org.springframework.stereotype.Repository

@Repository
class UserRepository {

    suspend fun findAll(): List<User> {
        return SupabaseClient.client
            .from("users")
            .select()
            .decodeList<User>()
    }

    suspend fun findById(id: Int): User {
        return SupabaseClient.client
            .from("users")
            .select {
                filter {
                    eq("id", id)
                }
            }
            .decodeSingle<User>()
    }

    suspend fun findByEmail(email: String): User {
        return SupabaseClient.client
            .from("users")
            .select {
                filter {
                    eq("email", email)
                }
            }
            .decodeSingle<User>()
    }

    suspend fun save(request: CreateUserRequest): User {
        return SupabaseClient.client
            .from("users")
            .insert(request) {
                select()
            }
            .decodeSingle<User>()
    }

    suspend fun update(id: Int, request: UpdateUserRequest): User {
        return SupabaseClient.client
            .from("users")
            .update(request) {
                filter {
                    eq("id", id)
                }
                select()
            }
            .decodeSingle<User>()
    }

    suspend fun deleteById(id: Int) {
        SupabaseClient.client
            .from("users")
            .delete {
                filter {
                    eq("id", id)
                }
            }
    }
}
