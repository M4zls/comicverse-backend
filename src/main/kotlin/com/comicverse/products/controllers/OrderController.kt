package com.comicverse.products.controllers

import com.comicverse.products.models.*
import com.comicverse.products.service.OrderService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = ["*"])
@Tag(name = "Ventas", description = "API de gestión de pedidos y ventas")
class OrderController(
    private val orderService: OrderService
) {

    @Operation(
        summary = "Obtener todas las ventas",
        description = "Retorna la lista completa de todos los pedidos/ventas registrados"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Lista de ventas obtenida exitosamente"),
        ApiResponse(responseCode = "500", description = "Error interno del servidor", content = [Content()])
    ])
    @GetMapping
    suspend fun getAllOrders(): ResponseEntity<List<Order>> {
        return try {
            val orders = orderService.getAllOrders()
            ResponseEntity.ok(orders)
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(emptyList())
        }
    }

    @Operation(
        summary = "Consultar venta por ID",
        description = "Obtiene los detalles completos de una venta específica"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Venta encontrada"),
        ApiResponse(responseCode = "404", description = "Venta no encontrada", content = [Content()])
    ])
    @GetMapping("/{id}")
    suspend fun getOrderById(
        @Parameter(description = "ID único de la venta", required = true)
        @PathVariable id: Int
    ): ResponseEntity<OrderWithDetails> {
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

    @Operation(
        summary = "Registrar nueva venta",
        description = "Crea un nuevo pedido/venta con los productos y detalles proporcionados"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "201", description = "Venta registrada exitosamente"),
        ApiResponse(responseCode = "400", description = "Datos inválidos", content = [Content()])
    ])
    @PostMapping
    suspend fun createOrder(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Datos de la nueva venta a registrar",
            required = true
        )
        @RequestBody request: CreateOrderRequest
    ): ResponseEntity<Any> {
        return try {
            println("Received create order request: $request")
            val orderWithDetails = orderService.createOrder(request)
            ResponseEntity.status(HttpStatus.CREATED).body(orderWithDetails)
        } catch (e: Exception) {
            println("Error creating order: ${e.message}")
            e.printStackTrace()
            val errorMessage = mapOf("error" to (e.message ?: "Failed to create order"))
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage)
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
