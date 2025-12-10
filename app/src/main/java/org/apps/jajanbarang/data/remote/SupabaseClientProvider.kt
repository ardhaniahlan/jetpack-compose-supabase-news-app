package org.apps.jajanbarang.data.remote

import io.github.jan.supabase.*
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.postgrest.Postgrest

object SupabaseClientProvider {
    val client by lazy {
        createSupabaseClient(
            supabaseUrl = "erl_supabase",
            supabaseKey = "your_api_keys"
        ) {
            install(Auth)
            install(Postgrest)
        }
    }
}
