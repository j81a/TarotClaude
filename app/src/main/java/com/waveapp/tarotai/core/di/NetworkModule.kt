package com.waveapp.tarotai.core.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.waveapp.tarotai.data.remote.api.ClaudeApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Módulo de Hilt para configurar Retrofit y la API de Claude.
 *
 * @Module: Marca esta clase como un módulo de Hilt.
 * @InstallIn(SingletonComponent::class): Estas dependencias viven durante toda la app.
 *
 * Este módulo configura:
 * - OkHttpClient con interceptors (logging y autenticación)
 * - Retrofit con Kotlin Serialization
 * - ClaudeApiService
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    /**
     * URL base de la API de Claude.
     */
    private const val BASE_URL = "https://api.anthropic.com/"

    /**
     * Versión de la API de Claude (header requerido).
     */
    private const val ANTHROPIC_VERSION = "2023-06-01"

    /**
     * Provee la instancia de Json para serialización.
     *
     * ignoreUnknownKeys = true: Ignora campos del JSON que no están en nuestras data classes.
     * Útil cuando la API devuelve campos extras que no necesitamos.
     */
    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
    }

    /**
     * Provee un interceptor para logging de requests/responses.
     *
     * HttpLoggingInterceptor de OkHttp imprime en Logcat:
     * - URLs de los requests
     * - Headers
     * - Body de request y response
     *
     * Útil para debugging. En producción se debería usar LEVEL.NONE.
     */
    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    /**
     * Provee un interceptor para agregar headers de autenticación.
     *
     * Este interceptor se ejecuta antes de cada request y agrega:
     * - x-api-key: API key de Claude (debe ser configurada por el usuario)
     * - anthropic-version: Versión de la API
     * - content-type: Tipo de contenido (JSON)
     *
     * TODO: La API key debe venir de SharedPreferences o configuración del usuario.
     * Por ahora retornamos un interceptor vacío hasta implementar la pantalla de configuración.
     */
    @Provides
    @Singleton
    fun provideAuthInterceptor(): Interceptor {
        return Interceptor { chain ->
            val originalRequest = chain.request()

            // TODO: Obtener la API key de SharedPreferences
            // Por ahora usamos una key placeholder
            val apiKey = "YOUR_API_KEY_HERE"

            val newRequest = originalRequest.newBuilder()
                .header("x-api-key", apiKey)
                .header("anthropic-version", ANTHROPIC_VERSION)
                .header("content-type", "application/json")
                .build()

            chain.proceed(newRequest)
        }
    }

    /**
     * Provee la instancia de OkHttpClient.
     *
     * OkHttpClient maneja las conexiones HTTP.
     * Configuramos:
     * - Timeouts: Tiempo máximo para conectar y leer
     * - Interceptors: Logging y autenticación
     */
    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        authInterceptor: Interceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)  // Timeout para conectar
            .readTimeout(30, TimeUnit.SECONDS)     // Timeout para leer respuesta
            .addInterceptor(authInterceptor)       // Primero auth (agrega headers)
            .addInterceptor(loggingInterceptor)    // Luego logging (para ver el request completo)
            .build()
    }

    /**
     * Provee la instancia de Retrofit.
     *
     * Retrofit convierte la interfaz ClaudeApiService en llamadas HTTP reales.
     * Configuramos:
     * - baseUrl: URL base de la API
     * - client: OkHttpClient configurado
     * - converterFactory: Usa Kotlin Serialization para JSON
     */
    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        json: Json
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(
                json.asConverterFactory("application/json".toMediaType())
            )
            .build()
    }

    /**
     * Provee la instancia de ClaudeApiService.
     *
     * Retrofit genera automáticamente la implementación de la interfaz.
     * Hilt inyectará esta instancia donde se necesite.
     */
    @Provides
    @Singleton
    fun provideClaudeApiService(retrofit: Retrofit): ClaudeApiService {
        return retrofit.create(ClaudeApiService::class.java)
    }
}
