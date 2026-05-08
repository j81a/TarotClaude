package com.waveapp.tarotai.core.di

import android.content.Context
import androidx.room.Room
import com.waveapp.tarotai.data.local.dao.ReadingHistoryDao
import com.waveapp.tarotai.data.local.dao.TarotCardDao
import com.waveapp.tarotai.data.local.database.MIGRATION_1_2
import com.waveapp.tarotai.data.local.database.MIGRATION_2_3
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
 * Este módulo se encarga de crear e inyectar la base de datos y los DAOs en toda la aplicación.
 *
 * v1.1.0: Agregada migración 1→2 y ReadingHistoryDao
 * v1.2.0: Agregada migración 2→3 para sistema de notas mejorado
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
     * - addMigrations(): Agrega migraciones para preservar datos al actualizar
     *
     * v1.1.0: Agregada MIGRATION_1_2 para tabla reading_history
     * v1.2.0: Agregada MIGRATION_2_3 para cambio de notes a notesJson
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
        )
            .addMigrations(MIGRATION_1_2, MIGRATION_2_3)  // 🆕 Migraciones v1→v2, v2→v3
            .build()
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

    /**
     * Provee el DAO para acceder al historial de lecturas.
     *
     * @Provides: Indica que este método provee una dependencia.
     * @param database: Instancia de TarotDatabase (inyectada automáticamente por Hilt)
     * @return ReadingHistoryDao para hacer queries a la tabla reading_history
     *
     * @since v1.1.0
     */
    @Provides
    fun provideReadingHistoryDao(database: TarotDatabase): ReadingHistoryDao {
        return database.readingHistoryDao()
    }
}
