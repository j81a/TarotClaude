# 10. Animaciones

## 10.1 Animación de Revelado de Cartas

**Enfoque: Compose Animations**

```kotlin
// Animación de flip de carta (dorso → frente)
@Composable
fun CardFlipAnimation(
    isFaceUp: Boolean,
    frontContent: @Composable () -> Unit,
    backContent: @Composable () -> Unit
) {
    val rotation by animateFloatAsState(
        targetValue = if (isFaceUp) 180f else 0f,
        animationSpec = tween(durationMillis = 600)
    )

    Box(modifier = Modifier.graphicsLayer { rotationY = rotation }) {
        if (rotation <= 90f) {
            backContent()
        } else {
            Box(modifier = Modifier.graphicsLayer { rotationY = 180f }) {
                frontContent()
            }
        }
    }
}
```

**Secuencia de tirada:**
1. Aparece dorso de carta con `fadeIn()` (300ms)
2. Espera 200ms
3. Flip animation (600ms)
4. Carta queda visible
5. Repetir para siguiente carta
