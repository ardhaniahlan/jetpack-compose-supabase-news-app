package org.apps.jajanbarang.data.repository

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import org.apps.jajanbarang.domain.model.User
import org.apps.jajanbarang.domain.repository.UserRepository

class UserRepositoryImpl(
    private val client: SupabaseClient
) : UserRepository {
    override suspend fun getProfile(userId: String): User? {
        return client
            .from("users")
            .select {
                filter {
                    eq("id", userId)
                }
            }
            .decodeSingle<User>()
    }
}