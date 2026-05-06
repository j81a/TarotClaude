package com.waveapp.tarotai.domain.usecase

import com.waveapp.tarotai.domain.model.ArcanaType
import com.waveapp.tarotai.domain.model.CardInterpretation
import com.waveapp.tarotai.domain.model.CardOrientation
import com.waveapp.tarotai.domain.model.DrawnCard
import com.waveapp.tarotai.domain.model.Interpretation
import com.waveapp.tarotai.domain.model.SpreadType
import com.waveapp.tarotai.domain.model.TarotCard
import com.waveapp.tarotai.domain.model.TarotReading
import com.waveapp.tarotai.domain.model.YesNoAnswer
import com.waveapp.tarotai.domain.repository.ClaudeRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * Tests unitarios para GenerateInterpretationUseCase.
 *
 * Verifica la lógica de validación y la llamada correcta al repositorio
 * para generar interpretaciones.
 */
class GenerateInterpretationUseCaseTest {

    // Mocks
    private lateinit var claudeRepository: ClaudeRepository
    private lateinit var generateInterpretationUseCase: GenerateInterpretationUseCase

    // Datos de prueba
    private val testCard = TarotCard(
        id = 0,
        name = "El Loco",
        arcanaType = ArcanaType.MAJOR,
        suit = null,
        imagePath = "card_major_00",
        generalMeaning = "Inicio del viaje",
        uprightMeaning = "Nuevos comienzos",
        reversedMeaning = "Imprudencia",
        symbolism = "El perro como símbolo",
        keywords = listOf("inicio", "locura", "libertad")
    )

    private val testReading = TarotReading(
        id = "test-id-123",
        spreadType = SpreadType.SIMPLE,
        question = "¿Qué me espera hoy?",
        drawnCards = listOf(
            DrawnCard(
                card = testCard,
                position = 0,
                positionName = "Respuesta",
                orientation = CardOrientation.UPRIGHT
            )
        )
    )

    private val testInterpretation = Interpretation(
        individualInterpretations = listOf(
            CardInterpretation(
                cardName = "El Loco",
                position = "Respuesta",
                interpretation = "Nuevos comienzos te esperan. Es momento de dar el salto."
            )
        ),
        generalInterpretation = "Esta tirada indica un día de nuevas oportunidades y aventuras.",
        yesNoAnswer = null,
        yesNoJustification = null
    )

    @Before
    fun setup() {
        claudeRepository = mockk()
        generateInterpretationUseCase = GenerateInterpretationUseCase(claudeRepository)
    }

    @Test
    fun `successful interpretation generation returns interpretation`() = runTest {
        // Given
        coEvery { claudeRepository.generateInterpretation(testReading) } returns Result.success(testInterpretation)

        // When
        val result = generateInterpretationUseCase(testReading)

        // Then
        assertTrue("Result should be success", result.isSuccess)
        val interpretation = result.getOrNull()
        assertNotNull("Interpretation should not be null", interpretation)
        assertEquals(
            "Should have 1 individual interpretation",
            1,
            interpretation!!.individualInterpretations.size
        )
        assertEquals(
            "General interpretation should match",
            testInterpretation.generalInterpretation,
            interpretation.generalInterpretation
        )

        // Verificar que se llamó al repositorio
        coVerify(exactly = 1) { claudeRepository.generateInterpretation(testReading) }
    }

    @Test
    fun `empty reading should return failure`() = runTest {
        // Given
        val emptyReading = TarotReading(
            id = "empty-id",
            spreadType = SpreadType.SIMPLE,
            question = "Test",
            drawnCards = emptyList()
        )

        // When
        val result = generateInterpretationUseCase(emptyReading)

        // Then
        assertTrue("Result should be failure", result.isFailure)
        val exception = result.exceptionOrNull()
        assertNotNull("Exception should not be null", exception)
        assertTrue(
            "Should be IllegalArgumentException",
            exception is IllegalArgumentException
        )
        assertEquals(
            "Error message should indicate no cards",
            "La tirada no tiene cartas para interpretar",
            exception!!.message
        )

        // Verificar que NO se llamó al repositorio
        coVerify(exactly = 0) { claudeRepository.generateInterpretation(any()) }
    }

    @Test
    fun `repository error should propagate`() = runTest {
        // Given
        val errorMessage = "API connection failed"
        coEvery { claudeRepository.generateInterpretation(testReading) } returns Result.failure(
            Exception(errorMessage)
        )

        // When
        val result = generateInterpretationUseCase(testReading)

        // Then
        assertTrue("Result should be failure", result.isFailure)
        val exception = result.exceptionOrNull()
        assertNotNull("Exception should not be null", exception)
        assertEquals("Error message should match", errorMessage, exception!!.message)
    }

    @Test
    fun `yes_no reading should include yes_no answer`() = runTest {
        // Given
        val yesNoReading = TarotReading(
            id = "yesno-id",
            spreadType = SpreadType.YES_NO,
            question = "¿Debería cambiar de trabajo?",
            drawnCards = listOf(
                DrawnCard(
                    card = testCard,
                    position = 0,
                    positionName = "Respuesta",
                    orientation = CardOrientation.UPRIGHT
                )
            )
        )

        val yesNoInterpretation = Interpretation(
            individualInterpretations = listOf(
                CardInterpretation(
                    cardName = "El Loco",
                    position = "Respuesta",
                    interpretation = "El Loco en posición derecha sugiere un nuevo comienzo."
                )
            ),
            generalInterpretation = "La respuesta es afirmativa.",
            yesNoAnswer = YesNoAnswer.YES,
            yesNoJustification = "El Loco representa nuevos comienzos y aventuras, lo que indica que es un buen momento para el cambio."
        )

        coEvery { claudeRepository.generateInterpretation(yesNoReading) } returns Result.success(yesNoInterpretation)

        // When
        val result = generateInterpretationUseCase(yesNoReading)

        // Then
        assertTrue("Result should be success", result.isSuccess)
        val interpretation = result.getOrNull()
        assertNotNull("Interpretation should not be null", interpretation)
        assertNotNull("YesNo answer should not be null", interpretation!!.yesNoAnswer)
        assertEquals("Answer should be YES", YesNoAnswer.YES, interpretation.yesNoAnswer)
        assertNotNull("Justification should not be null", interpretation.yesNoJustification)
    }

    @Test
    fun `cross reading with 5 cards should have 5 individual interpretations`() = runTest {
        // Given
        val crossReading = TarotReading(
            id = "cross-id",
            spreadType = SpreadType.CROSS,
            question = "¿Qué me depara el futuro?",
            drawnCards = List(5) { index ->
                DrawnCard(
                    card = testCard.copy(id = index, name = "Carta $index"),
                    position = index,
                    positionName = "Posición $index",
                    orientation = CardOrientation.UPRIGHT
                )
            }
        )

        val crossInterpretation = Interpretation(
            individualInterpretations = List(5) { index ->
                CardInterpretation(
                    cardName = "Carta $index",
                    position = "Posición $index",
                    interpretation = "Interpretación $index"
                )
            },
            generalInterpretation = "Interpretación general de la cruz",
            yesNoAnswer = null,
            yesNoJustification = null
        )

        coEvery { claudeRepository.generateInterpretation(crossReading) } returns Result.success(crossInterpretation)

        // When
        val result = generateInterpretationUseCase(crossReading)

        // Then
        assertTrue("Result should be success", result.isSuccess)
        val interpretation = result.getOrNull()
        assertNotNull("Interpretation should not be null", interpretation)
        assertEquals(
            "Should have 5 individual interpretations",
            5,
            interpretation!!.individualInterpretations.size
        )
    }

    @Test
    fun `network timeout error should return failure`() = runTest {
        // Given
        val timeoutException = Exception("Network timeout after 30 seconds")
        coEvery { claudeRepository.generateInterpretation(testReading) } returns Result.failure(timeoutException)

        // When
        val result = generateInterpretationUseCase(testReading)

        // Then
        assertTrue("Result should be failure", result.isFailure)
        val exception = result.exceptionOrNull()
        assertNotNull("Exception should not be null", exception)
        assertTrue(
            "Error message should mention timeout",
            exception!!.message!!.contains("timeout", ignoreCase = true)
        )
    }

    @Test
    fun `authentication error should return failure`() = runTest {
        // Given
        val authException = Exception("API authentication failed: Invalid API key")
        coEvery { claudeRepository.generateInterpretation(testReading) } returns Result.failure(authException)

        // When
        val result = generateInterpretationUseCase(testReading)

        // Then
        assertTrue("Result should be failure", result.isFailure)
        val exception = result.exceptionOrNull()
        assertNotNull("Exception should not be null", exception)
        assertTrue(
            "Error message should mention authentication",
            exception!!.message!!.contains("authentication", ignoreCase = true)
        )
    }
}