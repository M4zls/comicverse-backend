package com.comicverse.products.controllers

import com.comicverse.products.models.PaymentInfo
import com.comicverse.products.models.PaymentRequest
import com.comicverse.products.models.PaymentResponse
import com.comicverse.products.models.WebhookNotification
import com.comicverse.products.service.MercadoPagoService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = ["*"])
@Tag(name = "Payments", description = "API de Pagos con Mercado Pago")
class PaymentController(
    private val mercadoPagoService: MercadoPagoService
) {

    /**
     * Crear una nueva preferencia de pago
     * POST /api/payments/create
     */
    @PostMapping("/create")
    @Operation(summary = "Crear preferencia de pago", description = "Crea una nueva preferencia de pago en Mercado Pago y retorna el link de pago")
    fun createPayment(@RequestBody request: PaymentRequest): ResponseEntity<Any> {
        return try {
            println("🔵 Recibiendo request de pago: $request")
            val response = mercadoPagoService.createPaymentPreference(request)
            println("✅ Pago creado exitosamente: ${response.id}")
            ResponseEntity.ok(response)
        } catch (e: Exception) {
            println("❌ Error al crear pago: ${e.message}")
            e.printStackTrace()
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(mapOf("error" to e.message, "details" to e.toString()))
        }
    }

    /**
     * Obtener estado de un pago
     * GET /api/payments/{paymentId}
     */
    @GetMapping("/{paymentId}")
    @Operation(summary = "Obtener información de un pago", description = "Obtiene el estado y detalles de un pago por su ID")
    fun getPaymentStatus(@PathVariable paymentId: String): ResponseEntity<PaymentInfo> {
        return try {
            val paymentInfo = mercadoPagoService.getPaymentInfo(paymentId)
            ResponseEntity.ok(paymentInfo)
        } catch (e: Exception) {
            println("❌ Error al obtener pago: ${e.message}")
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
    }

    /**
     * Webhook para notificaciones de Mercado Pago
     * POST /api/payments/webhook
     */
    @PostMapping("/webhook")
    @Operation(summary = "Webhook de Mercado Pago", description = "Recibe notificaciones de cambios en los pagos")
    fun handleWebhook(
        @RequestBody notification: WebhookNotification,
        @RequestParam("type", required = false) type: String?
    ): ResponseEntity<String> {
        return try {
            println("📨 Webhook recibido - Tipo: ${notification.type}")
            
            when (notification.type) {
                "payment" -> {
                    val paymentId = notification.data.id
                    val status = mercadoPagoService.verifyPaymentStatus(paymentId)
                    
                    println("💳 Payment ID: $paymentId - Status: $status")
                    
                    // Aquí puedes agregar tu lógica de negocio
                    when (status) {
                        "approved" -> {
                            println("✅ Pago aprobado: $paymentId")
                            // TODO: Actualizar estado de la orden en tu base de datos
                            // TODO: Enviar email de confirmación al cliente
                            // TODO: Procesar el pedido
                        }
                        "pending" -> {
                            println("⏳ Pago pendiente: $paymentId")
                            // TODO: Notificar al cliente que el pago está pendiente
                        }
                        "rejected" -> {
                            println("❌ Pago rechazado: $paymentId")
                            // TODO: Notificar al cliente que el pago fue rechazado
                        }
                        else -> {
                            println("ℹ️ Estado del pago: $status")
                        }
                    }
                }
            }
            ResponseEntity.ok("OK")
        } catch (e: Exception) {
            println("❌ Error procesando webhook: ${e.message}")
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing webhook")
        }
    }

    /**
     * Página de éxito (redirección desde MP)
     */
    @GetMapping("/success")
    @Operation(summary = "Pago exitoso", description = "Endpoint de redirección cuando el pago es exitoso")
    fun paymentSuccess(
        @RequestParam("collection_id", required = false) collectionId: String?,
        @RequestParam("collection_status", required = false) collectionStatus: String?,
        @RequestParam("payment_id", required = false) paymentId: String?,
        @RequestParam("status", required = false) status: String?,
        @RequestParam("external_reference", required = false) externalReference: String?,
        @RequestParam("payment_type", required = false) paymentType: String?,
        @RequestParam("merchant_order_id", required = false) merchantOrderId: String?,
        @RequestParam("preference_id", required = false) preferenceId: String?
    ): ResponseEntity<Map<String, Any?>> {
        println("✅ Pago exitoso - Payment ID: $paymentId, Status: $status")
        return ResponseEntity.ok(
            mapOf(
                "success" to true,
                "message" to "Pago exitoso",
                "paymentId" to paymentId,
                "status" to status,
                "externalReference" to externalReference,
                "collectionId" to collectionId,
                "collectionStatus" to collectionStatus,
                "paymentType" to paymentType,
                "merchantOrderId" to merchantOrderId,
                "preferenceId" to preferenceId
            )
        )
    }

    /**
     * Página de fallo (redirección desde MP)
     */
    @GetMapping("/failure")
    @Operation(summary = "Pago fallido", description = "Endpoint de redirección cuando el pago falla")
    fun paymentFailure(
        @RequestParam("collection_id", required = false) collectionId: String?,
        @RequestParam("collection_status", required = false) collectionStatus: String?,
        @RequestParam("payment_id", required = false) paymentId: String?,
        @RequestParam("status", required = false) status: String?,
        @RequestParam("external_reference", required = false) externalReference: String?
    ): ResponseEntity<Map<String, Any?>> {
        println("❌ Pago fallido - Payment ID: $paymentId, Status: $status")
        return ResponseEntity.ok(
            mapOf(
                "success" to false,
                "message" to "Pago fallido o cancelado",
                "paymentId" to paymentId,
                "status" to status,
                "externalReference" to externalReference
            )
        )
    }

    /**
     * Página de pendiente (redirección desde MP)
     */
    @GetMapping("/pending")
    @Operation(summary = "Pago pendiente", description = "Endpoint de redirección cuando el pago queda pendiente")
    fun paymentPending(
        @RequestParam("collection_id", required = false) collectionId: String?,
        @RequestParam("collection_status", required = false) collectionStatus: String?,
        @RequestParam("payment_id", required = false) paymentId: String?,
        @RequestParam("status", required = false) status: String?,
        @RequestParam("external_reference", required = false) externalReference: String?
    ): ResponseEntity<Map<String, Any?>> {
        println("⏳ Pago pendiente - Payment ID: $paymentId, Status: $status")
        return ResponseEntity.ok(
            mapOf(
                "success" to false,
                "message" to "Pago pendiente de confirmación",
                "paymentId" to paymentId,
                "status" to status,
                "externalReference" to externalReference
            )
        )
    }

    /**
     * Health check del servicio de pagos
     */
    @GetMapping("/health")
    @Operation(summary = "Health check", description = "Verifica que el servicio de pagos esté funcionando")
    fun health(): ResponseEntity<Map<String, String>> {
        return ResponseEntity.ok(
            mapOf(
                "status" to "UP",
                "service" to "Mercado Pago Payment Service",
                "timestamp" to System.currentTimeMillis().toString()
            )
        )
    }
    
    /**
     * Diagnóstico de configuración de Mercado Pago
     */
    @GetMapping("/config-check")
    @Operation(summary = "Verificar configuración", description = "Verifica que las credenciales de Mercado Pago estén configuradas")
    fun configCheck(mpConfig: MercadoPagoConfiguration): ResponseEntity<Map<String, Any>> {
        val accessTokenConfigured = try {
            mpConfig.publicKey.isNotEmpty()
        } catch (e: Exception) {
            false
        }
        
        return ResponseEntity.ok(
            mapOf(
                "publicKeyConfigured" to accessTokenConfigured,
                "publicKeyPrefix" to if (accessTokenConfigured) mpConfig.publicKey.take(10) + "..." else "NOT CONFIGURED",
                "successUrl" to mpConfig.successUrl,
                "failureUrl" to mpConfig.failureUrl,
                "pendingUrl" to mpConfig.pendingUrl,
                "timestamp" to System.currentTimeMillis().toString()
            )
        )
    }
}
