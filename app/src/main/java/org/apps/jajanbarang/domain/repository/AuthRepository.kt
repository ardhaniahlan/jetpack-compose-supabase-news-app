package org.apps.jajanbarang.domain.repository

import org.apps.jajanbarang.domain.model.User


interface AuthRepository {
    suspend fun register(name: String, email: String, password: String): Result<User>
    suspend fun login(email: String, password: String): Result<User>
    suspend fun logout(): Result<Unit>
}