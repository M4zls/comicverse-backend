package com.comicverse.products.service

import com.comicverse.products.models.*
import com.comicverse.products.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository
) {

    suspend fun getAllUsers(): List<User> {
        return userRepository.findAll()
    }

    suspend fun getUserById(id: Int): User {
        return userRepository.findById(id)
    }

    suspend fun getUserByEmail(email: String): User {
        return userRepository.findByEmail(email)
    }

    suspend fun createUser(request: CreateUserRequest): User {
        return userRepository.save(request)
    }

    suspend fun updateUser(id: Int, request: UpdateUserRequest): User {
        return userRepository.update(id, request)
    }

    suspend fun deleteUser(id: Int) {
        userRepository.deleteById(id)
    }

    suspend fun login(email: String, password: String): User {
        return try {
            val user = userRepository.findByEmail(email)
            if (user.password == password) {
                user
            } else {
                throw Exception("Invalid password")
            }
        } catch (e: Exception) {
            if (e.message == "Invalid password") {
                throw e
            }
            throw Exception("User not found with email: $email")
        }
    }
}
