package com.comicverse.products.models

import kotlinx.serialization.Serializable

@Serializable
data class Manga(
    val id: String? = null,
    val created_at: String? = null,
    val name: String? = null,
    val type: String? = null,
    val year: Int? = null,
    val stock: Int? = null,
    val price: Int? = null,
    val sale_price: Int? = null,
    val description: String? = null,
    val poster: String? = null
)

@Serializable
data class CreateMangaRequest(
    val name: String,
    val type: String? = null,
    val year: Int? = null,
    val stock: Int? = null,
    val price: Int,
    val sale_price: Int? = null,
    val description: String? = null,
    val poster: String? = null
)

@Serializable
data class UpdateMangaRequest(
    val name: String? = null,
    val type: String? = null,
    val year: Int? = null,
    val stock: Int? = null,
    val price: Int? = null,
    val sale_price: Int? = null,
    val description: String? = null,
    val poster: String? = null
)
