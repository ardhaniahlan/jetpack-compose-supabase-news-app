package org.apps.jajanbarang.data.remote

import io.github.jan.supabase.*
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.postgrest.Postgrest

object SupabaseClientProvider {
    val client by lazy {
        createSupabaseClient(
            supabaseUrl = "https://clbkwxcajmtnukrgyfah.supabase.co",
            supabaseKey = "sb_publishable_4Ttl4hPC4tJTGRj_ysTw8A_fre6Lk64"
        ) {
            install(Auth)
            install(Postgrest)
        }
    }
}
