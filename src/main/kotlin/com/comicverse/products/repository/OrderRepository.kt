package com.comicverse.products.repository

import com.comicverse.products.SupabaseClient
import com.comicverse.products.models.*
import io.github.jan.supabase.postgrest.from
import org.springframework.stereotype.Repository

@Repository
class OrderRepository {

    suspend fun findAll(): List<Order> {
        return SupabaseClient.client
            .from("orders")
            .select()
            .decodeList<Order>()
    }

    suspend fun findById(id: Int): Order {
        return SupabaseClient.client
            .from("orders")
            .select {
                filter {
                    eq("id", id)
                }
            }
            .decodeSingle<Order>()
    }

    suspend fun findByUserId(userId: Int): List<Order> {
        return SupabaseClient.client
            .from("orders")
            .select {
                filter {
                    eq("user_id", userId)
                }
            }
            .decodeList<Order>()
    }

    suspend fun save(userId: Int, total: Int): Order {
        return SupabaseClient.client
            .from("orders")
            .insert(
                mapOf(
                    "user_id" to userId,
                    "total" to total,
                    "status" to "PENDING"
                )
            ) {
                select()
            }
            .decodeSingle<Order>()
    }

    suspend fun updateStatus(id: Int, status: String): Order {
        return SupabaseClient.client
            .from("orders")
            .update(
                mapOf("status" to status)
            ) {
                filter {
                    eq("id", id)
                }
                select()
            }
            .decodeSingle<Order>()
    }

    suspend fun deleteById(id: Int) {
        SupabaseClient.client
            .from("orders")
            .delete {
                filter {
                    eq("id", id)
                }
            }
    }

    suspend fun findOrderItemsByOrderId(orderId: Int): List<OrderItem> {
        return SupabaseClient.client
            .from("order_items")
            .select {
                filter {
                    eq("order_id", orderId)
                }
            }
            .decodeList<OrderItem>()
    }

    suspend fun saveOrderItem(orderId: Int, mangaId: String, quantity: Int, price: Int): OrderItem {
        return SupabaseClient.client
            .from("order_items")
            .insert(
                mapOf(
                    "order_id" to orderId,
                    "manga_id" to mangaId,
                    "quantity" to quantity,
                    "price" to price
                )
            ) {
                select()
            }
            .decodeSingle<OrderItem>()
    }

    suspend fun deleteOrderItemsByOrderId(orderId: Int) {
        SupabaseClient.client
            .from("order_items")
            .delete {
                filter {
                    eq("order_id", orderId)
                }
            }
    }
}
