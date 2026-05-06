package com.waveapp.tarotai.domain.usecase

import com.waveapp.tarotai.domain.model.ArcanaType
import com.waveapp.tarotai.domain.model.CardOrientation
import com.waveapp.tarotai.domain.model.SpreadType
import com.waveapp.tarotai.domain.model.TarotCard
import com.waveapp.tarotai.domain.repository.TarotCardRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * Tests unitarios para PerformReadingUseCase.
 *
 * Verifica la lógica de selección de cartas, orientaciones y posiciones
 * para diferentes tipos de tiradas.
 */
class PerformReadingUseCaseTest {

    // Mocks
    private lateinit var cardRepository: TarotCardRepository
    private lateinit var getSpreadConfigurationUseCase: GetSpreadConfigurationUseCase
    private lateinit var performReadingUseCase: PerformReadingUseCase

    // Datos de prueba
    private val testCards = listOf(
        TarotCard(
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
        ),
        TarotCard(
            id = 1,
            name = "El Mago",
            arcanaType = ArcanaType.MAJOR,
            suit = null,
            imagePath = "card_major_01",
            generalMeaning = "Manifestación",
            uprightMeaning = "Poder",
            reversedMeaning = "Manipulación",
            symbolism = "Los cuatro elementos",
            keywords = listOf("poder", "habilidad", "concentración")
        ),
        TarotCard(
            id = 2,
            name = "La Sacerdotisa",
            arcanaType = ArcanaType.MAJOR,
            suit = null,
            imagePath = "card_major_02",
            generalMeaning = "Intuición",
            uprightMeaning = "Sabiduría",
            reversedMeaning = "Secretos",
            symbolism = "La luna",
            keywords = listOf("intuición", "misterio", "sabiduría")
        ),
        TarotCard(
            id = 3,
            name = "La Emperatriz",
            arcanaType = ArcanaType.MAJOR,
            suit = null,
            imagePath = "card_major_03",
            generalMeaning = "Abundancia",
            uprightMeaning = "Fertilidad",
            reversedMeaning = "Dependencia",
            symbolism = "La naturaleza",
            keywords = listOf("abundancia", "maternidad", "naturaleza")
        ),
        TarotCard(
            id = 4,
            name = "El Emperador",
            arcanaType = ArcanaType.MAJOR,
            suit = null,
            imagePath = "card_major_04",
            generalMeaning = "Autoridad",
            uprightMeaning = "Estructura",
            reversedMeaning = "Dominación",
            symbolism = "El trono",
            keywords = listOf("autoridad", "estructura", "control")
        )
    )

    @Before
    fun setup() {
        // Crear mocks con relaxed para evitar problemas con métodos no mockeados
        cardRepository = mockk(relaxed = true)
        getSpreadConfigurationUseCase = GetSpreadConfigurationUseCase()
        performReadingUseCase = PerformReadingUseCase(
            cardRepository,
            getSpreadConfigurationUseCase
        )

        // Configurar comportamiento del mock
        coEvery { cardRepository.getAllCards() } returns flowOf(testCards)
    }

    @Test
    fun `perform simple reading returns 1 card`() = runTest {
        // Given
        val spreadType = SpreadType.SIMPLE
        val question: String? = null

        // When
        val result = performReadingUseCase(spreadType, question)

        // Then
        assertTrue("Result should be success", result.isSuccess)
        val reading = result.getOrNull()
        assertNotNull("Reading should not be null", reading)
        assertEquals("Should have 1 card", 1, reading!!.drawnCards.size)
        assertEquals("SpreadType should match", spreadType, reading.spreadType)
        assertEquals("Question should be null", null, reading.question)
    }

    @Test
    fun `perform yes_no reading with question returns 1 card`() = runTest {
        // Given
        val spreadType = SpreadType.YES_NO
        val question = "¿Debería cambiar de trabajo?"

        // When
        val result = performReadingUseCase(spreadType, question)

        // Then
        assertTrue("Result should be success", result.isSuccess)
        val reading = result.getOrNull()
        assertNotNull("Reading should not be null", reading)
        assertEquals("Should have 1 card", 1, reading!!.drawnCards.size)
        assertEquals("Question should match", question, reading.question)
    }

    @Test
    fun `perform present reading returns 3 cards with correct positions`() = runTest {
        // Given
        val spreadType = SpreadType.PRESENT
        val question = "¿Cuál es mi situación actual?"

        // When
        val result = performReadingUseCase(spreadType, question)

        // Then
        assertTrue("Result should be success", result.isSuccess)
        val reading = result.getOrNull()
        assertNotNull("Reading should not be null", reading)
        assertEquals("Should have 3 cards", 3, reading!!.drawnCards.size)

        // Verificar posiciones
        assertEquals("Position 0 should be Presente", "Presente", reading.drawnCards[0].positionName)
        assertEquals("Position 1 should be Obstáculo", "Obstáculo", reading.drawnCards[1].positionName)
        assertEquals("Position 2 should be Ayuda", "Ayuda", reading.drawnCards[2].positionName)
    }

    @Test
    fun `perform tendency reading returns 3 cards with correct positions`() = runTest {
        // Given
        val spreadType = SpreadType.TENDENCY
        val question = "¿Hacia dónde me dirijo?"

        // When
        val result = performReadingUseCase(spreadType, question)

        // Then
        assertTrue("Result should be success", result.isSuccess)
        val reading = result.getOrNull()
        assertNotNull("Reading should not be null", reading)
        assertEquals("Should have 3 cards", 3, reading!!.drawnCards.size)

        // Verificar posiciones
        assertEquals("Position 0", "De dónde vengo", reading.drawnCards[0].positionName)
        assertEquals("Position 1", "Dónde estoy", reading.drawnCards[1].positionName)
        assertEquals("Position 2", "A dónde voy", reading.drawnCards[2].positionName)
    }

    @Test
    fun `perform cross reading returns 5 cards with correct positions`() = runTest {
        // Given
        val spreadType = SpreadType.CROSS
        val question = "¿Qué me depara el futuro en este asunto?"

        // When
        val result = performReadingUseCase(spreadType, question)

        // Then
        assertTrue("Result should be success", result.isSuccess)
        val reading = result.getOrNull()
        assertNotNull("Reading should not be null", reading)
        assertEquals("Should have 5 cards", 5, reading!!.drawnCards.size)

        // Verificar posiciones
        assertEquals("Position 0", "De dónde vengo", reading.drawnCards[0].positionName)
        assertEquals("Position 1", "Hacia dónde voy", reading.drawnCards[1].positionName)
        assertEquals("Position 2", "Ayuda", reading.drawnCards[2].positionName)
        assertEquals("Position 3", "Obstáculo", reading.drawnCards[3].positionName)
        assertEquals("Position 4", "Conclusión", reading.drawnCards[4].positionName)
    }

    @Test
    fun `cards should not repeat in same reading`() = runTest {
        // Given
        val spreadType = SpreadType.CROSS // 5 cartas
        val question = "Test"

        // When
        val result = performReadingUseCase(spreadType, question)

        // Then
        assertTrue("Result should be success", result.isSuccess)
        val reading = result.getOrNull()
        assertNotNull("Reading should not be null", reading)

        // Verificar que no hay cartas repetidas
        val cardIds = reading!!.drawnCards.map { it.card.id }
        val uniqueIds = cardIds.toSet()
        assertEquals("All cards should be unique", cardIds.size, uniqueIds.size)
    }

    @Test
    fun `cards should have orientation assigned`() = runTest {
        // Given
        val spreadType = SpreadType.PRESENT
        val question = "Test"

        // When
        val result = performReadingUseCase(spreadType, question)

        // Then
        assertTrue("Result should be success", result.isSuccess)
        val reading = result.getOrNull()
        assertNotNull("Reading should not be null", reading)

        // Verificar que todas las cartas tienen orientación
        reading!!.drawnCards.forEach { drawnCard ->
            assertTrue(
                "Card should have valid orientation",
                drawnCard.orientation == CardOrientation.UPRIGHT ||
                drawnCard.orientation == CardOrientation.REVERSED
            )
        }
    }

    @Test
    fun `each drawn card should have correct position index`() = runTest {
        // Given
        val spreadType = SpreadType.CROSS
        val question = "Test"

        // When
        val result = performReadingUseCase(spreadType, question)

        // Then
        assertTrue("Result should be success", result.isSuccess)
        val reading = result.getOrNull()
        assertNotNull("Reading should not be null", reading)

        // Verificar que cada carta tiene el índice correcto
        reading!!.drawnCards.forEachIndexed { index, drawnCard ->
            assertEquals("Position index should match", index, drawnCard.position)
        }
    }

    @Test
    fun `reading should have unique ID`() = runTest {
        // Given
        val spreadType = SpreadType.SIMPLE
        val question = null

        // When - Realizar dos tiradas
        val result1 = performReadingUseCase(spreadType, question)
        val result2 = performReadingUseCase(spreadType, question)

        // Then
        assertTrue("Result 1 should be success", result1.isSuccess)
        assertTrue("Result 2 should be success", result2.isSuccess)

        val reading1 = result1.getOrNull()
        val reading2 = result2.getOrNull()

        assertNotNull("Reading 1 should not be null", reading1)
        assertNotNull("Reading 2 should not be null", reading2)

        // Los IDs deben ser diferentes
        assertFalse(
            "Reading IDs should be unique",
            reading1!!.id == reading2!!.id
        )
    }

    @Test
    fun `repository error should return failure`() = runTest {
        // Given
        val spreadType = SpreadType.SIMPLE
        val question = null

        // Mock lanza excepción
        coEvery { cardRepository.getAllCards() } throws Exception("Database error")

        // When
        val result = performReadingUseCase(spreadType, question)

        // Then
        assertTrue("Result should be failure", result.isFailure)
        val exception = result.exceptionOrNull()
        assertNotNull("Exception should not be null", exception)
        assertEquals("Database error", exception!!.message)
    }
}