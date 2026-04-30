package com.waveapp.tarotai.core.di

import android.content.Context
import com.waveapp.tarotai.data.local.dao.TarotCardDao
import com.waveapp.tarotai.data.remote.api.ClaudeApiService
import com.waveapp.tarotai.data.repository.ClaudeRepositoryImpl
import com.waveapp.tarotai.data.repository.TarotCardRepositoryImpl
import com.waveapp.tarotai.domain.repository.ClaudeRepository
import com.waveapp.tarotai.domain.repository.TarotCardRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Módulo de Hilt para proveer los Repositories.
 *
 * @Module: Marca esta clase como un módulo de Hilt
 * @InstallIn(SingletonComponent::class): Los repositories viven durante toda la app
 *
 * Este módulo conecta las interfaces de los repositories (dominio)
 * con sus implementaciones concretas (datos).
 */
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    /**
     * Provee la implementación del TarotCardRepository.
     *
     * @param context Contexto para leer el JSON de assets
     * @param cardDao DAO para acceder a Room
     * @return Instancia única del repository
     *
     * Hilt inyectará esta implementación donde se requiera TarotCardRepository.
     */
    @Provides
    @Singleton
    fun provideTarotCardRepository(
        @ApplicationContext context: Context,
        cardDao: TarotCardDao
    ): TarotCardRepository {
        return TarotCardRepositoryImpl(context, cardDao)
    }

    /**
     * Provee la implementación del ClaudeRepository.
     *
     * @param claudeApiService Servicio de la API de Claude
     * @return Instancia única del repository
     *
     * Hilt inyectará esta implementación donde se requiera ClaudeRepository.
     */
    @Provides
    @Singleton
    fun provideClaudeRepository(
        claudeApiService: ClaudeApiService
    ): ClaudeRepository {
        return ClaudeRepositoryImpl(claudeApiService)
    }
}
