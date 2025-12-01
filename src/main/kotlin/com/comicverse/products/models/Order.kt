package com.comicverse.products.models

import kotlinx.serialization.Serializable

@Serializable
data class Order(
    val id: Int? = null,
    val user_id: Int,
    val total: Int,
    val status: String = "PENDING",
    val created_at: String? = null,
    val updated_at: String? = null
)

@Serializable
data class OrderItem(
    val id: Int? = null,
    val order_id: Int,
    val manga_id: String,
    val quantity: Int,
    val price: Int
)

@Serializable
data class CreateOrderRequest(
    val user_id: Int,
    val items: List<CreateOrderItemRequest>
)

@Serializable
data class CreateOrderItemRequest(
    val manga_id: String,
    val quantity: Int
)

@Serializable
data class UpdateOrderRequest(
    val status: String? = null
)

@Serializable
data class OrderWithDetails(
    val id: Int,
    val user_id: Int,
    val user_name: String,
    val total: Int,
    val status: String,
    val created_at: String? = null,
    val items: List<OrderItemWithManga>
)

@Serializable
data class OrderItemWithManga(
    val id: Int,
    val manga_id: String,
    val manga_name: String?,
    val manga_poster: String?,
    val quantity: Int,
    val price: Int
)
