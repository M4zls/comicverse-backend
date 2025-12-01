package com.comicverse.products.service

import com.comicverse.products.models.*
import com.comicverse.products.repository.MangaRepository
import com.comicverse.products.repository.OrderRepository
import com.comicverse.products.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class OrderService(
    private val orderRepository: OrderRepository,
    private val mangaRepository: MangaRepository,
    private val userRepository: UserRepository
) {

    suspend fun getAllOrders(): List<Order> {
        return orderRepository.findAll()
    }

    suspend fun getOrderById(id: Int): OrderWithDetails {
        val order = orderRepository.findById(id)
        val user = userRepository.findById(order.user_id)
        val orderItems = orderRepository.findOrderItemsByOrderId(id)

        val itemsWithMangas = orderItems.map { item ->
            val manga = mangaRepository.findById(item.manga_id)
            OrderItemWithManga(
                id = item.id!!,
                manga_id = item.manga_id,
                manga_name = manga.name,
                manga_poster = manga.poster,
                quantity = item.quantity,
                price = item.price
            )
        }

        return OrderWithDetails(
            id = order.id!!,
            user_id = order.user_id,
            user_name = user.name,
            total = order.total,
            status = order.status,
            created_at = order.created_at,
            items = itemsWithMangas
        )
    }

    suspend fun getOrdersByUserId(userId: Int): List<OrderWithDetails> {
        val orders = orderRepository.findByUserId(userId)
        val user = userRepository.findById(userId)

        return orders.map { order ->
            val orderItems = orderRepository.findOrderItemsByOrderId(order.id!!)
            val itemsWithMangas = orderItems.map { item ->
                val manga = mangaRepository.findById(item.manga_id)
                OrderItemWithManga(
                    id = item.id!!,
                    manga_id = item.manga_id,
                    manga_name = manga.name,
                    manga_poster = manga.poster,
                    quantity = item.quantity,
                    price = item.price
                )
            }

            OrderWithDetails(
                id = order.id,
                user_id = order.user_id,
                user_name = user.name,
                total = order.total,
                status = order.status,
                created_at = order.created_at,
                items = itemsWithMangas
            )
        }
    }

    suspend fun createOrder(request: CreateOrderRequest): OrderWithDetails {
        // Calcular el total
        var total = 0
        for (item in request.items) {
            val manga = mangaRepository.findById(item.manga_id)
            total += (manga.price ?: 0) * item.quantity
        }

        // Crear la orden
        val order = orderRepository.save(request.user_id, total)

        // Crear los items de la orden
        val orderItems = mutableListOf<OrderItemWithManga>()
        for (item in request.items) {
            val manga = mangaRepository.findById(item.manga_id)
            val orderItem = orderRepository.saveOrderItem(
                order.id!!,
                item.manga_id,
                item.quantity,
                manga.price ?: 0
            )

            orderItems.add(
                OrderItemWithManga(
                    id = orderItem.id!!,
                    manga_id = manga.id!!,
                    manga_name = manga.name,
                    manga_poster = manga.poster,
                    quantity = item.quantity,
                    price = manga.price ?: 0
                )
            )
        }

        val user = userRepository.findById(request.user_id)

        return OrderWithDetails(
            id = order.id!!,
            user_id = order.user_id,
            user_name = user.name,
            total = order.total,
            status = order.status,
            created_at = order.created_at,
            items = orderItems
        )
    }

    suspend fun updateOrderStatus(id: Int, request: UpdateOrderRequest): Order {
        return orderRepository.updateStatus(id, request.status!!)
    }

    suspend fun deleteOrder(id: Int) {
        orderRepository.deleteOrderItemsByOrderId(id)
        orderRepository.deleteById(id)
    }
}
