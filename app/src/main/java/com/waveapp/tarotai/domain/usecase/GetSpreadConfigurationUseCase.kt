package com.waveapp.tarotai.domain.usecase

import com.waveapp.tarotai.domain.model.SpreadConfiguration
import com.waveapp.tarotai.domain.model.SpreadType
import javax.inject.Inject

/**
 * Caso de uso: Obtener configuración de tirada.
 * Devuelve la configuración (cantidad de cartas, posiciones, etc.) según el tipo de tirada.
 */
class GetSpreadConfigurationUseCase @Inject constructor() {

    operator fun invoke(type: SpreadType): SpreadConfiguration {
        return SpreadConfiguration.fromType(type)
    }
}
