# 2. Estándares de Código

## 2.1 Documentación en Español

**REQUERIMIENTO OBLIGATORIO:** Todo el código debe estar documentado en español.

**Reglas de documentación:**

1. **Clases y sealed classes:**
   - Comentario de clase con descripción breve
   - `@property` para cada parámetro del constructor explicando su propósito

2. **Funciones y métodos:**
   - Comentario explicando qué hace la función
   - `@param` para cada parámetro
   - `@return` para el valor de retorno si aplica

3. **Enums:**
   - Comentario de enum explicando el propósito
   - Comentarios inline para cada valor si es necesario

**Ejemplo correcto:**

```kotlin
/**
 * Configuración de cada tipo de tirada.
 * Define cuántas cartas se usan, las posiciones y el layout visual.
 *
 * @property type Tipo de tirada
 * @property cardCount Cantidad de cartas en la tirada
 * @property positions Nombres de las posiciones de cada carta
 * @property requiresQuestion Indica si la tirada requiere una pregunta del usuario
 * @property layout Tipo de disposición visual (horizontal o cruz)
 */
sealed class SpreadConfiguration(
    val type: SpreadType,
    val cardCount: Int,
    val positions: List<String>,
    val requiresQuestion: Boolean,
    val layout: LayoutType
) { ... }

/**
 * Realiza una tirada de tarot seleccionando cartas aleatorias.
 *
 * @param spreadType Tipo de tirada a realizar
 * @param question Pregunta del usuario (opcional, según el tipo de tirada)
 * @return Result con la tirada generada o un error
 */
suspend fun performReading(
    spreadType: SpreadType,
    question: String?
): Result<TarotReading> { ... }
```

**Justificación:**
- Facilita la comprensión del código por todos los desarrolladores del equipo
- Genera documentación automática (KDoc) en español
- Mantiene consistencia con el idioma de la aplicación
