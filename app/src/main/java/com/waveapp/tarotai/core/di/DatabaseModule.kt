package com.waveapp.tarotai.core.di

import android.content.Context
import androidx.room.Room
import com.waveapp.tarotai.data.local.dao.TarotCardDao
import com.waveapp.tarotai.data.local.database.TarotDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Módulo de Hilt para proveer la base de datos Room.
 *
 * @Module: Marca esta clase como un módulo de Hilt que provee dependencias.
 * @InstallIn(SingletonComponent::class): Indica que estas dependencias vivirán mientras viva la app.
 *
 * Este módulo se encarga de crear e inyectar la base de datos y el DAO en toda la aplicación.
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    /**
     * Provee la instancia única de TarotDatabase.
     *
     * @Provides: Indica que este método provee una dependencia.
     * @Singleton: Asegura que solo existe una instancia en toda la app.
     * @ApplicationContext: Inyecta el contexto de la aplicación.
     *
     * Room.databaseBuilder crea la base de datos SQLite.
     * - context: Contexto de Android necesario para crear la DB
     * - TarotDatabase::class.java: Clase de la base de datos
     * - "tarot_database": Nombre del archivo SQLite
     */
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): TarotDatabase {
        return Room.databaseBuilder(
            context,
            TarotDatabase::class.java,
            "tarot_database"
        ).build()
    }

    /**
     * Provee el DAO para acceder a las cartas.
     *
     * @Provides: Indica que este método provee una dependencia.
     * @param database: Instancia de TarotDatabase (inyectada automáticamente por Hilt)
     * @return TarotCardDao para hacer queries a la tabla tarot_cards
     *
     * Hilt inyectará automáticamente este DAO donde se necesite (Repositorios, ViewModels, etc.)
     */
    @Provides
    fun provideCardDao(database: TarotDatabase): TarotCardDao {
        return database.cardDao()
    }
}
