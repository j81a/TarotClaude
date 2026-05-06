package com.waveapp.tarotai.domain.usecase

import com.waveapp.tarotai.domain.model.LayoutType
import com.waveapp.tarotai.domain.model.SpreadType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * Tests unitarios para GetSpreadConfigurationUseCase.
 *
 * Verifica que cada tipo de tirada tenga la configuración correcta
 * (cantidad de cartas, posiciones, layout, etc.).
 */
class GetSpreadConfigurationUseCaseTest {

    private lateinit var getSpreadConfigurationUseCase: GetSpreadConfigurationUseCase

    @Before
    fun setup() {
        getSpreadConfigurationUseCase = GetSpreadConfigurationUseCase()
    }

    @Test
    fun `simple spread configuration is correct`() {
        // When
        val config = getSpreadConfigurationUseCase(SpreadType.SIMPLE)

        // Then
        assertEquals("SpreadType should be SIMPLE", SpreadType.SIMPLE, config.type)
        assertEquals("Should have 1 card", 1, config.cardCount)
        assertEquals("Should have 1 position", 1, config.positions.size)
        assertEquals("Position should be 'Respuesta'", "Respuesta", config.positions[0])
        assertFalse("Should not require question", config.requiresQuestion)
        assertEquals("Layout should be HORIZONTAL", LayoutType.HORIZONTAL, config.layout)
    }

    @Test
    fun `yes_no spread configuration is correct`() {
        // When
        val config = getSpreadConfigurationUseCase(SpreadType.YES_NO)

        // Then
        assertEquals("SpreadType should be YES_NO", SpreadType.YES_NO, config.type)
        assertEquals("Should have 1 card", 1, config.cardCount)
        assertEquals("Should have 1 position", 1, config.positions.size)
        assertEquals("Position should be 'Respuesta'", "Respuesta", config.positions[0])
        assertTrue("Should require question", config.requiresQuestion)
        assertEquals("Layout should be HORIZONTAL", LayoutType.HORIZONTAL, config.layout)
    }

    @Test
    fun `present spread configuration is correct`() {
        // When
        val config = getSpreadConfigurationUseCase(SpreadType.PRESENT)

        // Then
        assertEquals("SpreadType should be PRESENT", SpreadType.PRESENT, config.type)
        assertEquals("Should have 3 cards", 3, config.cardCount)
        assertEquals("Should have 3 positions", 3, config.positions.size)
        assertEquals("Position 0 should be 'Presente'", "Presente", config.positions[0])
        assertEquals("Position 1 should be 'Obstáculo'", "Obstáculo", config.positions[1])
        assertEquals("Position 2 should be 'Ayuda'", "Ayuda", config.positions[2])
        assertTrue("Should require question", config.requiresQuestion)
        assertEquals("Layout should be HORIZONTAL", LayoutType.HORIZONTAL, config.layout)
    }

    @Test
    fun `tendency spread configuration is correct`() {
        // When
        val config = getSpreadConfigurationUseCase(SpreadType.TENDENCY)

        // Then
        assertEquals("SpreadType should be TENDENCY", SpreadType.TENDENCY, config.type)
        assertEquals("Should have 3 cards", 3, config.cardCount)
        assertEquals("Should have 3 positions", 3, config.positions.size)
        assertEquals("Position 0 should be 'De dónde vengo'", "De dónde vengo", config.positions[0])
        assertEquals("Position 1 should be 'Dónde estoy'", "Dónde estoy", config.positions[1])
        assertEquals("Position 2 should be 'A dónde voy'", "A dónde voy", config.positions[2])
        assertTrue("Should require question", config.requiresQuestion)
        assertEquals("Layout should be HORIZONTAL", LayoutType.HORIZONTAL, config.layout)
    }

    @Test
    fun `cross spread configuration is correct`() {
        // When
        val config = getSpreadConfigurationUseCase(SpreadType.CROSS)

        // Then
        assertEquals("SpreadType should be CROSS", SpreadType.CROSS, config.type)
        assertEquals("Should have 5 cards", 5, config.cardCount)
        assertEquals("Should have 5 positions", 5, config.positions.size)
        assertEquals("Position 0 should be 'De dónde vengo'", "De dónde vengo", config.positions[0])
        assertEquals("Position 1 should be 'Hacia dónde voy'", "Hacia dónde voy", config.positions[1])
        assertEquals("Position 2 should be 'Ayuda'", "Ayuda", config.positions[2])
        assertEquals("Position 3 should be 'Obstáculo'", "Obstáculo", config.positions[3])
        assertEquals("Position 4 should be 'Conclusión'", "Conclusión", config.positions[4])
        assertTrue("Should require question", config.requiresQuestion)
        assertEquals("Layout should be CROSS", LayoutType.CROSS, config.layout)
    }

    @Test
    fun `all spread configurations have matching card count and positions size`() {
        // Given - Todos los tipos de tirada
        val allSpreadTypes = SpreadType.entries

        // When & Then
        allSpreadTypes.forEach { spreadType ->
            val config = getSpreadConfigurationUseCase(spreadType)
            assertEquals(
                "CardCount should match positions size for $spreadType",
                config.cardCount,
                config.positions.size
            )
        }
    }

    @Test
    fun `all spread configurations have positive card count`() {
        // Given
        val allSpreadTypes = SpreadType.entries

        // When & Then
        allSpreadTypes.forEach { spreadType ->
            val config = getSpreadConfigurationUseCase(spreadType)
            assertTrue(
                "CardCount should be positive for $spreadType",
                config.cardCount > 0
            )
        }
    }

    @Test
    fun `all spread configurations have non-empty positions`() {
        // Given
        val allSpreadTypes = SpreadType.entries

        // When & Then
        allSpreadTypes.forEach { spreadType ->
            val config = getSpreadConfigurationUseCase(spreadType)
            assertTrue(
                "Positions should not be empty for $spreadType",
                config.positions.isNotEmpty()
            )
            config.positions.forEach { position ->
                assertTrue(
                    "Position name should not be blank for $spreadType",
                    position.isNotBlank()
                )
            }
        }
    }

    @Test
    fun `only simple spread does not require question`() {
        // Given
        val allSpreadTypes = SpreadType.entries

        // When & Then
        allSpreadTypes.forEach { spreadType ->
            val config = getSpreadConfigurationUseCase(spreadType)
            if (spreadType == SpreadType.SIMPLE) {
                assertFalse(
                    "SIMPLE should not require question",
                    config.requiresQuestion
                )
            } else {
                assertTrue(
                    "$spreadType should require question",
                    config.requiresQuestion
                )
            }
        }
    }

    @Test
    fun `only cross spread has cross layout`() {
        // Given
        val allSpreadTypes = SpreadType.entries

        // When & Then
        allSpreadTypes.forEach { spreadType ->
            val config = getSpreadConfigurationUseCase(spreadType)
            if (spreadType == SpreadType.CROSS) {
                assertEquals(
                    "CROSS should have CROSS layout",
                    LayoutType.CROSS,
                    config.layout
                )
            } else {
                assertEquals(
                    "$spreadType should have HORIZONTAL layout",
                    LayoutType.HORIZONTAL,
                    config.layout
                )
            }
        }
    }

    @Test
    fun `configurations should be consistent across multiple calls`() {
        // Given
        val spreadType = SpreadType.CROSS

        // When - Llamar múltiples veces
        val config1 = getSpreadConfigurationUseCase(spreadType)
        val config2 = getSpreadConfigurationUseCase(spreadType)
        val config3 = getSpreadConfigurationUseCase(spreadType)

        // Then - Todas las configuraciones deben ser iguales
        assertEquals("Config 1 and 2 should match", config1.cardCount, config2.cardCount)
        assertEquals("Config 2 and 3 should match", config2.cardCount, config3.cardCount)
        assertEquals("Positions should match", config1.positions, config2.positions)
        assertEquals("Positions should match", config2.positions, config3.positions)
    }
}