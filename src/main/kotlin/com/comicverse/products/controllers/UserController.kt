package com.comicverse.products.controllers

import com.comicverse.products.models.*
import com.comicverse.products.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = ["*"])
class UserController(
    private val userService: UserService
) {

    @GetMapping
    suspend fun getAllUsers(): ResponseEntity<List<UserResponse>> {
        return try {
            val users = userService.getAllUsers().map { it.toResponse() }
            ResponseEntity.ok(users)
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(emptyList())
        }
    }

    @GetMapping("/{id}")
    suspend fun getUserById(@PathVariable id: Int): ResponseEntity<UserResponse> {
        return try {
            val user = userService.getUserById(id)
            ResponseEntity.ok(user.toResponse())
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
    }

    @GetMapping("/email/{email}")
    suspend fun getUserByEmail(@PathVariable email: String): ResponseEntity<UserResponse> {
        return try {
            val user = userService.getUserByEmail(email)
            ResponseEntity.ok(user.toResponse())
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
    }

    @PostMapping
    suspend fun createUser(@RequestBody request: CreateUserRequest): ResponseEntity<UserResponse> {
        return try {
            val user = userService.createUser(request)
            ResponseEntity.status(HttpStatus.CREATED).body(user.toResponse())
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
        }
    }

    @PutMapping("/{id}")
    suspend fun updateUser(
        @PathVariable id: Int,
        @RequestBody request: UpdateUserRequest
    ): ResponseEntity<UserResponse> {
        return try {
            val user = userService.updateUser(id, request)
            ResponseEntity.ok(user.toResponse())
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
    }

    @DeleteMapping("/{id}")
    suspend fun deleteUser(@PathVariable id: Int): ResponseEntity<Void> {
        return try {
            userService.deleteUser(id)
            ResponseEntity.noContent().build()
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
    }

    @PostMapping("/login")
    suspend fun login(@RequestBody request: LoginRequest): ResponseEntity<UserResponse> {
        return try {
            val user = userService.login(request.email, request.password)
            ResponseEntity.ok(user.toResponse())
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        }
    }

    private fun User.toResponse() = UserResponse(
        id = id!!,
        email = email,
        name = name,
        address = address,
        phone = phone,
        created_at = created_at
    )
}

@kotlinx.serialization.Serializable
data class LoginRequest(
    val email: String,
    val password: String
)
