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
 * Basado en la paleta "Mystic Teal" personalizada.
 */
private val DarkColorScheme = darkColorScheme(
    // Colores primarios - Verde Menta Brillante (máxima legibilidad)
    primary = TealPrimary,                 // #00DF82 - Verde menta brillante
    onPrimary = Color(0xFF001A12),         // Negro verdoso para texto sobre verde
    primaryContainer = TealDark,           // #03624C - Verde azulado oscuro
    onPrimaryContainer = TextPrimary,      // Blanco puro

    // Colores secundarios - Turquesa vibrante
    secondary = TurquoisePrimary,          // Turquesa vibrante
    onSecondary = Color(0xFF001A12),       // Negro verdoso
    secondaryContainer = TurquoiseDark,    // Turquesa oscuro
    onSecondaryContainer = TextPrimary,    // Blanco puro

    // Colores terciarios - Verde claro
    tertiary = TealLight,                  // Verde menta más claro
    onTertiary = Color(0xFF001A12),
    tertiaryContainer = TealDark,
    onTertiaryContainer = TextPrimary,

    // Fondo y superficie - Negro Verdoso
    background = DarkBackground,           // #030F0F - Negro verdoso profundo
    onBackground = TextPrimary,            // Blanco puro
    surface = DarkSurface,                 // Gris verdoso muy oscuro
    onSurface = TextPrimary,               // Blanco puro
    surfaceVariant = DarkSurfaceVariant,   // Gris verdoso oscuro
    onSurfaceVariant = TextSecondary,      // Gris verdoso claro

    // Otros
    error = ErrorColor,
    onError = Color(0xFFFFFFFF),
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
    primary = TealDark,
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = TealLight,
    onPrimaryContainer = Color(0xFF001A12),

    secondary = TurquoiseDark,
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = TurquoiseLight,
    onSecondaryContainer = Color(0xFF001A12),

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