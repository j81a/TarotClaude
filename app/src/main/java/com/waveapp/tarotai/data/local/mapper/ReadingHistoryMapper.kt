package com.waveapp.tarotai.data.local.mapper

import com.waveapp.tarotai.data.local.entities.ReadingHistoryEntity
import com.waveapp.tarotai.domain.model.DrawnCard
import com.waveapp.tarotai.domain.model.Interpretation
import com.waveapp.tarotai.domain.model.ReadingHistory
import com.waveapp.tarotai.domain.model.ReadingNote
import com.waveapp.tarotai.domain.model.SpreadType
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * Mappers para convertir entre ReadingHistoryEntity (capa de datos)
 * y ReadingHistory (capa de dominio).
 *
 * Estos mappers permiten:
 * - Separar la lógica de negocio de los detalles de persistencia
 * - Cambiar la implementación de BD sin afectar el dominio
 * - Mantener los modelos de dominio limpios
 *
 * Los campos JSON (drawnCardsJson, interpretationJson) se serializan/deserializan aquí
 * manualmente, aunque también tenemos TypeConverters en Room como backup.
 *
 * @since v1.1.0
 */

/**
 * JSON configurado para serialización con ignoreUnknownKeys.
 */
private val json = Json {
    ignoreUnknownKeys = true
    prettyPrint = false
}

/**
 * Convierte un ReadingHistoryEntity (Room) a ReadingHistory (Domain).
 *
 * Deserializa los campos JSON (drawnCardsJson, interpretationJson, notesJson) a objetos.
 *
 * @return Modelo de dominio ReadingHistory
 */
fun ReadingHistoryEntity.toDomain(): ReadingHistory {
    val drawnCards: List<DrawnCard> = try {
        json.decodeFromString(drawnCardsJson)
    } catch (e: Exception) {
        emptyList() // Fallback si falla la deserialización
    }

    val interpretation: Interpretation = try {
        json.decodeFromString(interpretationJson)
    } catch (e: Exception) {
        // Fallback con interpretación vacía
        Interpretation(
            individualInterpretations = emptyList(),
            generalInterpretation = "Error al cargar interpretación"
        )
    }

    val notes: List<ReadingNote> = try {
        json.decodeFromString(notesJson)
    } catch (e: Exception) {
        emptyList() // Fallback si falla la deserialización
    }

    return ReadingHistory(
        id = id,
        timestamp = timestamp,
        consultantName = consultantName,
        spreadType = SpreadType.valueOf(spreadType),
        question = question,
        drawnCards = drawnCards,
        interpretation = interpretation,
        notes = notes
    )
}

/**
 * Convierte un ReadingHistory (Domain) a ReadingHistoryEntity (Room).
 *
 * Serializa los campos complejos (drawnCards, interpretation, notes) a JSON.
 *
 * @return Entidad de Room ReadingHistoryEntity
 */
fun ReadingHistory.toEntity(): ReadingHistoryEntity {
    return ReadingHistoryEntity(
        id = id,
        timestamp = timestamp,
        consultantName = consultantName,
        spreadType = spreadType.name,
        question = question,
        drawnCardsJson = json.encodeToString(drawnCards),
        interpretationJson = json.encodeToString(interpretation),
        notesJson = json.encodeToString(notes)
    )
}
