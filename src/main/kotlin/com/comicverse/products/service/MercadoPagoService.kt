package com.comicverse.products.service

import com.comicverse.products.config.MercadoPagoConfiguration
import com.comicverse.products.models.PaymentInfo
import com.comicverse.products.models.PaymentRequest
import com.comicverse.products.models.PaymentResponse
import com.mercadopago.client.payment.PaymentClient
import com.mercadopago.client.preference.*
import com.mercadopago.resources.payment.Payment
import com.mercadopago.resources.preference.Preference
import org.springframework.stereotype.Service
import java.util.concurrent.ConcurrentHashMap

@Service
class MercadoPagoService(
    private val mpConfig: MercadoPagoConfiguration
) {
    // Cache temporal para guardar los datos del pago mientras se procesa
    private val pendingPayments = ConcurrentHashMap<String, PaymentRequest>()
    
    /**
     * Crea una preferencia de pago en Mercado Pago
     * @param request Datos del pago a crear
     * @return PaymentResponse con el ID y URL de pago
     */
    fun createPaymentPreference(request: PaymentRequest): PaymentResponse {
        try {
            println("🔵 Creando preferencia de pago para: ${request.title} - Precio: ${request.price}")
            
            // Convertir el precio de String a BigDecimal
            val priceValue = request.price.toBigDecimal()
            println("🔵 Precio convertido a BigDecimal: $priceValue")
            
            // Crear item del pago
            val item = PreferenceItemRequest.builder()
                .id(request.externalReference ?: System.currentTimeMillis().toString())
                .title(request.title)
                .description(request.description)
                .categoryId("others")
                .quantity(request.quantity)
                .currencyId(request.currencyId)
                .unitPrice(priceValue)
                .build()
            
            println("🔵 Item creado: ${item.title} - ${item.unitPrice} ${item.currencyId}")

            // Configurar URLs de retorno
            val backUrls = PreferenceBackUrlsRequest.builder()
                .success(mpConfig.successUrl)
                .failure(mpConfig.failureUrl)
                .pending(mpConfig.pendingUrl)
                .build()
            
            println("🔵 URLs configuradas: success=${mpConfig.successUrl}")

            // Construir la preferencia
            val preferenceRequestBuilder = PreferenceRequest.builder()
                .items(listOf(item))
                .backUrls(backUrls)
                .statementDescriptor("COMICVERSE")
                .externalReference(request.externalReference)

            // Agregar email del pagador si está disponible
            request.payerEmail?.let { email ->
                println("🔵 Agregando email del pagador: $email")
                preferenceRequestBuilder.payer(
                    PreferencePayerRequest.builder()
                        .email(email)
                        .name("Test")
                        .surname("User")
                        .build()
                )
            }

            val preferenceRequest = preferenceRequestBuilder.build()
            println("🔵 Preferencia construida, enviando a Mercado Pago...")

            // Crear la preferencia en Mercado Pago
            val client = PreferenceClient()
            val preference: Preference = client.create(preferenceRequest)
            
            println("✅ Preferencia creada exitosamente: ${preference.id}")
            
            // Guardar los datos del pago para usar en el webhook
            request.externalReference?.let { ref ->
                pendingPayments[ref] = request
                println("💾 Datos del pago guardados con referencia: $ref")
                println("📦 User ID: ${request.userId}")
                println("📦 Items: ${request.items?.size ?: 0}")
                println("📊 Total pendingPayments: ${pendingPayments.size}")
            }

            return PaymentResponse(
                id = preference.id,
                initPoint = preference.initPoint,
                sandboxInitPoint = preference.sandboxInitPoint
            )
        } catch (e: com.mercadopago.exceptions.MPApiException) {
            println("❌ Error de API de Mercado Pago:")
            println("   Status: ${e.statusCode}")
            println("   Message: ${e.message}")
            println("   Cause: ${e.cause}")
            e.apiResponse?.content?.let { println("   Response: $it") }
            throw RuntimeException("Error de Mercado Pago (${e.statusCode}): ${e.message}", e)
        } catch (e: Exception) {
            println("❌ Error inesperado: ${e.javaClass.simpleName}")
            println("   Message: ${e.message}")
            e.printStackTrace()
            throw RuntimeException("Error al crear preferencia de pago: ${e.message}", e)
        }
    }

    /**
     * Obtiene información de un pago por su ID
     * @param paymentId ID del pago
     * @return Payment con la información completa
     */
    fun getPayment(paymentId: String): Payment {
        try {
            val client = PaymentClient()
            return client.get(paymentId.toLong())
        } catch (e: Exception) {
            throw RuntimeException("Error al obtener información del pago: ${e.message}", e)
        }
    }

    /**
     * Verifica el estado de un pago
     * @param paymentId ID del pago
     * @return String con el estado del pago (approved, pending, rejected, etc.)
     */
    fun verifyPaymentStatus(paymentId: String): String {
        val payment = getPayment(paymentId)
        return payment.status
    }

    /**
     * Recupera los datos del pago guardados
     */
    fun getPendingPaymentData(externalReference: String): PaymentRequest? {
        println("🔍 Buscando datos para referencia: $externalReference")
        println("📊 Total pendingPayments disponibles: ${pendingPayments.size}")
        println("🔑 Keys disponibles: ${pendingPayments.keys}")
        val data = pendingPayments[externalReference]
        println("📦 Datos encontrados: ${data != null}")
        return data
    }
    
    /**
     * Elimina los datos del pago guardados
     */
    fun removePendingPaymentData(externalReference: String) {
        pendingPayments.remove(externalReference)
        println("🗑️ Datos del pago eliminados: $externalReference")
    }
    
    /**
     * Obtiene información detallada de un pago
     * @param paymentId ID del pago
     * @return PaymentInfo con los datos del pago
     */
    fun getPaymentInfo(paymentId: String): PaymentInfo {
        val payment = getPayment(paymentId)
        return PaymentInfo(
            id = payment.id,
            status = payment.status,
            statusDetail = payment.statusDetail,
            transactionAmount = payment.transactionAmount,
            currencyId = payment.currencyId,
            dateCreated = payment.dateCreated?.toString(),
            dateApproved = payment.dateApproved?.toString(),
            externalReference = payment.externalReference,
            payerEmail = payment.payer?.email
        )
    }
}
