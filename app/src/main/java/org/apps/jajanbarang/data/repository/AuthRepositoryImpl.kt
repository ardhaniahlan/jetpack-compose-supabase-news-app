package org.apps.jajanbarang.data.repository

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import org.apps.jajanbarang.domain.model.User
import org.apps.jajanbarang.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val client: SupabaseClient
): AuthRepository {
    override suspend fun register(
        name: String,
        email: String,
        password: String,
    ): Result<User> {
        return try {
            client.auth.signUpWith(Email) {
                this.email = email
                this.password = password
            }

            val userId = client.auth.currentUserOrNull()?.id
                ?: throw Exception("User ID not found")

            client.from("users").insert(
                mapOf(
                    "id" to userId,
                    "name" to name,
                    "email" to email
                )
            )

            Result.success(
                User(
                    id = userId,
                    name = name,
                    email = email
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun login(
        email: String,
        password: String,
    ): Result<User> {

        return try {
            client.auth.signInWith(Email) {
                this.email = email
                this.password = password
            }

            val userId = client.auth.currentUserOrNull()?.id
                ?: throw Exception("User ID not found")

            val userResponse = client.postgrest["users"]
                .select {
                    filter {
                        eq("id", userId)
                    }
                }
                .decodeSingle<User>()

            Result.success(userResponse)
        } catch (e: Exception){
            Result.failure(e)
        }
    }

    override suspend fun logout(): Result<Unit> {
        return try {
            client.auth.signOut()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
