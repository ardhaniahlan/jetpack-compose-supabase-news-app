package org.apps.jajanbarang.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
import org.apps.jajanbarang.BuildConfig
import org.apps.jajanbarang.data.remote.NewsApiService
import org.apps.jajanbarang.data.remote.RetrofitClient
import org.apps.jajanbarang.data.remote.SupabaseClientProvider
import org.apps.jajanbarang.data.repository.AuthRepositoryImpl
import org.apps.jajanbarang.data.repository.NewsRepositoryImpl
import org.apps.jajanbarang.data.repository.UserRepositoryImpl
import org.apps.jajanbarang.domain.repository.AuthRepository
import org.apps.jajanbarang.domain.repository.NewsRepository
import org.apps.jajanbarang.domain.repository.UserRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideSupabaseAuth(): SupabaseClient = SupabaseClientProvider.client

    @Provides
    @Singleton
    fun provideNewsApiService(): NewsApiService = RetrofitClient.newsApiService

    @Provides
    @Singleton
    fun provideAuthRepository(client: SupabaseClient): AuthRepository =
        AuthRepositoryImpl(client)

    @Provides
    @Singleton
    fun provideNewsRepository(apiService: NewsApiService): NewsRepository =
        NewsRepositoryImpl(
            apiService = apiService,
            apiKey = BuildConfig.NEWS_API_KEYS
        )

    @Provides
    @Singleton
    fun provideUserRepository(client: SupabaseClient): UserRepository =
        UserRepositoryImpl(client)
}