package com.comicverse.products.models

import java.math.BigDecimal

/**
 * Request para crear un pago
 */
data class PaymentRequest(
    val title: String,
    val description: String? = null,
    val price: String, // String para evitar problemas de serialización
    val quantity: Int = 1,
    val currencyId: String = "ARS",
    val externalReference: String? = null,
    val payerEmail: String? = null
)

/**
 * Response con la información de la preferencia de pago creada
 */
data class PaymentResponse(
    val id: String,
    val initPoint: String,
    val sandboxInitPoint: String? = null
)

/**
 * Notificación de webhook de Mercado Pago
 */
data class WebhookNotification(
    val action: String? = null,
    val apiVersion: String? = null,
    val data: WebhookData,
    val dateCreated: String? = null,
    val id: Long? = null,
    val liveMode: Boolean? = null,
    val type: String,
    val userId: String? = null
)

data class WebhookData(
    val id: String
)

/**
 * Información detallada de un pago
 */
data class PaymentInfo(
    val id: Long,
    val status: String,
    val statusDetail: String?,
    val transactionAmount: BigDecimal,
    val currencyId: String,
    val dateCreated: String?,
    val dateApproved: String?,
    val externalReference: String?,
    val payerEmail: String?
)
