package com.waveapp.tarotai.core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * Esquema de colores oscuro de TarotAI (tema principal).
 *
 * Define los colores para el modo oscuro siguiendo Material Design 3.
 * Este es el tema por defecto ya que evoca el misterio del tarot.
 */
private val DarkColorScheme = darkColorScheme(
    // Colores primarios - Azul brillante sobre fondo oscuro (alto contraste)
    primary = BluePrimary,
    onPrimary = Color(0xFF001D35),         // Azul muy oscuro para texto sobre azul
    primaryContainer = BlueDark,
    onPrimaryContainer = TextPrimary,

    // Colores secundarios - Dorado cálido
    secondary = GoldPrimary,
    onSecondary = Color(0xFF1A1A1A),       // Gris oscuro para texto sobre dorado
    secondaryContainer = GoldDark,
    onSecondaryContainer = TextPrimary,

    // Colores terciarios - Azul claro
    tertiary = BlueLight,
    onTertiary = Color(0xFF001D35),
    tertiaryContainer = BlueDark,
    onTertiaryContainer = TextPrimary,

    // Fondo y superficie - Azul medianoche
    background = DarkBackground,
    onBackground = TextPrimary,
    surface = DarkSurface,
    onSurface = TextPrimary,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = TextSecondary,

    // Otros
    error = ErrorColor,
    onError = Color(0xFF1A1A1A),
    outline = TextTertiary,
    outlineVariant = DarkSurfaceVariant,
    scrim = DarkBackground
)

/**
 * Esquema de colores claro de TarotAI (opcional).
 *
 * Define los colores para el modo claro.
 * Actualmente no se usa, pero está disponible para futuras versiones.
 */
private val LightColorScheme = lightColorScheme(
    primary = BlueDark,
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = BlueLight,
    onPrimaryContainer = Color(0xFF001D35),

    secondary = GoldDark,
    onSecondary = Color(0xFF1A1A1A),
    secondaryContainer = GoldLight,
    onSecondaryContainer = Color(0xFF1A1A1A),

    background = Color(0xFFFCFCFC),
    onBackground = Color(0xFF1A1A1A),
    surface = Color(0xFFF5F5F5),
    onSurface = Color(0xFF1A1A1A),

    error = Color(0xFFB00020),
    onError = Color(0xFFFFFFFF)
)

/**
 * Tema principal de TarotAI.
 *
 * @Composable: Marca esta función como un componente de UI de Compose.
 *
 * @param darkTheme: Si true, usa el tema oscuro; si false, usa el tema claro.
 *                   Por defecto se obtiene de la configuración del sistema.
 * @param content: Contenido (pantallas y componentes) que usará este tema.
 *
 * Aplica:
 * - Esquema de colores (oscuro o claro)
 * - Tipografía personalizada
 * - Formas de Material Design 3
 *
 * Uso:
 * ```kotlin
 * TarotAITheme {
 *     MyScreen()
 * }
 * ```
 */
@Composable
fun TarotAITheme(
    darkTheme: Boolean = true, // Siempre oscuro por defecto (temática de tarot)
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = TarotTypography,
        content = content
    )
}