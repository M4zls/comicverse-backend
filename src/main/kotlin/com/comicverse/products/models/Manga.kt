package com.comicverse.products.models

import io.swagger.v3.oas.annotations.media.Schema
import kotlinx.serialization.Serializable

@Serializable
@Schema(description = "Modelo de producto (Manga/Comic)")
data class Manga(
    @Schema(description = "ID único del producto (UUID)", example = "550e8400-e29b-41d4-a716-446655440000")
    val id: String? = null,
    
    @Schema(description = "Fecha de creación", example = "2025-12-04T10:30:00Z")
    val created_at: String? = null,
    
    @Schema(description = "Nombre del producto", example = "One Piece Vol. 1", required = true)
    val name: String? = null,
    
    @Schema(description = "Tipo de producto", example = "Manga", required = false)
    val type: String? = null,
    
    @Schema(description = "Año de publicación", example = "1997")
    val year: Int? = null,
    
    @Schema(description = "Cantidad disponible en stock", example = "50")
    val stock: Int? = null,
    
    @Schema(description = "Precio regular en CLP", example = "15000", required = true)
    val price: Int? = null,
    
    @Schema(description = "Precio de oferta en CLP", example = "12000")
    val sale_price: Int? = null,
    
    @Schema(description = "Descripción del producto")
    val description: String? = null,
    
    @Schema(description = "URL de la imagen del producto")
    val poster: String? = null
)

@Serializable
@Schema(description = "Datos para crear un nuevo producto")
data class CreateMangaRequest(
    @Schema(description = "Nombre del producto", example = "One Piece Vol. 1", required = true)
    val name: String,
    
    @Schema(description = "Tipo de producto", example = "Manga")
    val type: String? = null,
    
    @Schema(description = "Año de publicación", example = "1997")
    val year: Int? = null,
    
    @Schema(description = "Cantidad inicial en stock", example = "50")
    val stock: Int? = null,
    
    @Schema(description = "Precio regular en CLP", example = "15000", required = true)
    val price: Int,
    
    @Schema(description = "Precio de oferta en CLP", example = "12000")
    val sale_price: Int? = null,
    
    @Schema(description = "Descripción del producto")
    val description: String? = null,
    
    @Schema(description = "URL de la imagen del producto")
    val poster: String? = null
)

@Serializable
@Schema(description = "Datos para actualizar un producto existente")
data class UpdateMangaRequest(
    @Schema(description = "Nuevo nombre del producto", example = "One Piece Vol. 1 - Edición Especial")
    val name: String? = null,
    
    @Schema(description = "Nuevo tipo", example = "Manga")
    val type: String? = null,
    
    @Schema(description = "Nuevo año", example = "1997")
    val year: Int? = null,
    
    @Schema(description = "Nuevo stock", example = "75")
    val stock: Int? = null,
    
    @Schema(description = "Nuevo precio regular", example = "16000")
    val price: Int? = null,
    
    @Schema(description = "Nuevo precio de oferta", example = "13000")
    val sale_price: Int? = null,
    
    @Schema(description = "Nueva descripción")
    val description: String? = null,
    
    @Schema(description = "Nueva URL de imagen")
    val poster: String? = null
)
