package com.comicverse.products.models

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Int? = null,
    val email: String,
    val name: String,
    val password: String? = null,
    val address: String? = null,
    val phone: String? = null,
    val created_at: String? = null
)

@Serializable
data class CreateUserRequest(
    val email: String,
    val name: String,
    val password: String,
    val address: String? = null,
    val phone: String? = null
)

@Serializable
data class UpdateUserRequest(
    val email: String? = null,
    val name: String? = null,
    val password: String? = null,
    val address: String? = null,
    val phone: String? = null
)

@Serializable
data class UserResponse(
    val id: Int,
    val email: String,
    val name: String,
    val address: String? = null,
    val phone: String? = null,
    val created_at: String? = null
)
