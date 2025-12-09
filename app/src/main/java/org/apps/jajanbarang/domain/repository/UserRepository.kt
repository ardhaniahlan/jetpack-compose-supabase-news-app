package org.apps.jajanbarang.domain.repository

import org.apps.jajanbarang.domain.model.User

interface UserRepository {
    suspend fun getProfile(userId: String): User?
}