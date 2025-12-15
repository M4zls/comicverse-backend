package com.comicverse.products.config

import com.mercadopago.MercadoPagoConfig
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import jakarta.annotation.PostConstruct

@Configuration
class MercadoPagoConfiguration {
    
    @Value("\${mercadopago.access-token}")
    private lateinit var accessToken: String
    
    @Value("\${mercadopago.public-key}")
    lateinit var publicKey: String
    
    @Value("\${mercadopago.success-url}")
    lateinit var successUrl: String
    
    @Value("\${mercadopago.failure-url}")
    lateinit var failureUrl: String
    
    @Value("\${mercadopago.pending-url}")
    lateinit var pendingUrl: String
    
    @PostConstruct
    fun init() {
        MercadoPagoConfig.setAccessToken(accessToken)
        println("✅ Mercado Pago configurado correctamente")
    }
}
