package com.comicverse.products.controllers

import com.comicverse.products.models.*
import com.comicverse.products.service.OrderService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = ["*"])
class OrderController(
    private val orderService: OrderService
) {

    @GetMapping
    suspend fun getAllOrders(): ResponseEntity<List<Order>> {
        return try {
            val orders = orderService.getAllOrders()
            ResponseEntity.ok(orders)
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(emptyList())
        }
    }

    @GetMapping("/{id}")
    suspend fun getOrderById(@PathVariable id: Int): ResponseEntity<OrderWithDetails> {
        return try {
            val orderWithDetails = orderService.getOrderById(id)
            ResponseEntity.ok(orderWithDetails)
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
    }

    @GetMapping("/user/{userId}")
    suspend fun getOrdersByUserId(@PathVariable userId: Int): ResponseEntity<List<OrderWithDetails>> {
        return try {
            val ordersWithDetails = orderService.getOrdersByUserId(userId)
            ResponseEntity.ok(ordersWithDetails)
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(emptyList())
        }
    }

    @PostMapping
    suspend fun createOrder(@RequestBody request: CreateOrderRequest): ResponseEntity<OrderWithDetails> {
        return try {
            val orderWithDetails = orderService.createOrder(request)
            ResponseEntity.status(HttpStatus.CREATED).body(orderWithDetails)
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
        }
    }

    @PutMapping("/{id}")
    suspend fun updateOrderStatus(
        @PathVariable id: Int,
        @RequestBody request: UpdateOrderRequest
    ): ResponseEntity<Order> {
        return try {
            val order = orderService.updateOrderStatus(id, request)
            ResponseEntity.ok(order)
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
    }

    @DeleteMapping("/{id}")
    suspend fun deleteOrder(@PathVariable id: Int): ResponseEntity<Void> {
        return try {
            orderService.deleteOrder(id)
            ResponseEntity.noContent().build()
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
    }
}
