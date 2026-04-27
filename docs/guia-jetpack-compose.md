# Guía: Jetpack Compose para TarotAI

> **Documento educativo para aprender Jetpack Compose**
> De XML a UI declarativa moderna

---

## 🎨 ¿Qué es Jetpack Compose?

**Jetpack Compose** es el **toolkit moderno de Google** para crear interfaces de usuario en Android.

### Antes (XML + Activities/Fragments)
```xml
<!-- layout.xml -->
<LinearLayout>
    <TextView android:id="@+id/title" />
    <Button android:id="@+id/button" />
</LinearLayout>
```

```kotlin
// MainActivity.kt
val title = findViewById<TextView>(R.id.title)
title.text = "Hola"
val button = findViewById<Button>(R.id.button)
button.setOnClickListener { ... }
```

### Ahora (Compose - Declarativo)
```kotlin
@Composable
fun MyScreen() {
    Column {
        Text("Hola")
        Button(onClick = { ... }) {
            Text("Click aquí")
        }
    }
}
```

**Ventajas**:
- ✅ Menos código (menos boilerplate)
- ✅ Más legible (describes QUÉ ves, no CÓMO construirlo)
- ✅ Reactivo (UI se actualiza automáticamente)
- ✅ Todo en Kotlin (sin XML)

---

## 🧱 Conceptos Fundamentales

### 1. **@Composable: Funciones que dibujan UI**

```kotlin
@Composable
fun Greeting(name: String) {
    Text("Hola, $name!")
}

@Composable
fun MyApp() {
    Greeting("Jorge")  // Muestra: "Hola, Jorge!"
}
```

**Reglas importantes**:
- Deben tener `@Composable`
- Pueden llamar a otras funciones `@Composable`
- Se "recomponen" (redibujan) cuando los datos cambian

### 2. **Composables básicos para TarotAI**

```kotlin
// Texto
Text(
    text = "El Loco",
    style = MaterialTheme.typography.headlineMedium,
    color = Color.White
)

// Botón
Button(onClick = { /* acción */ }) {
    Text("Nueva Tirada")
}

// Imagen
Image(
    painter = painterResource(id = R.drawable.card_major_00),
    contentDescription = "El Loco"
)

// Columna (vertical)
Column {
    Text("Línea 1")
    Text("Línea 2")
}

// Fila (horizontal)
Row {
    Icon(Icons.Default.Star)
    Text("Favorito")
}

// Caja (apilados)
Box {
    Image(...)       // Fondo
    Text("Título")   // Encima de la imagen
}
```

### 3. **Layouts: Column, Row, Box**

```kotlin
@Composable
fun TarotCardDetail() {
    Column {  // Vertical
        Image(...)                    // Imagen arriba
        Text("El Loco")              // Título
        Text("Significado: ...")     // Descripción

        Row {  // Horizontal
            Button(...) { Text("Atrás") }
            Spacer(modifier = Modifier.weight(1f))  // Espacio flexible
            Button(...) { Text("Siguiente") }
        }
    }
}
```

---

## 🎨 Modifiers: Personalizando Componentes

Los **Modifiers** cambian el aspecto y comportamiento de los composables:

```kotlin
@Composable
fun StyledCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()        // Ocupa todo el ancho
            .height(200.dp)        // Altura fija
            .padding(16.dp)        // Margen interno
            .clickable { ... }     // Hace que sea clickeable
            .background(Color.Blue) // Fondo azul
    ) {
        Text("Carta de Tarot")
    }
}
```

**Modifiers comunes**:
- `Modifier.size(width.dp, height.dp)` - Tamaño fijo
- `Modifier.fillMaxSize()` - Ocupa toda la pantalla
- `Modifier.fillMaxWidth()` - Ocupa todo el ancho
- `Modifier.padding(16.dp)` - Espaciado
- `Modifier.clickable { }` - Hace clickeable
- `Modifier.background(Color.Red)` - Color de fondo
- `Modifier.weight(1f)` - Distribución proporcional en Row/Column

---

## 🔄 Estado y Recomposición

### ¿Qué es el Estado?

**Estado** = datos que pueden cambiar y que la UI necesita mostrar

```kotlin
@Composable
fun Counter() {
    var count by remember { mutableStateOf(0) }  // Estado local

    Column {
        Text("Has clickeado $count veces")
        Button(onClick = { count++ }) {  // Cambia el estado
            Text("Click")
        }
    }
}
// Cuando count cambia, la UI se REDIBUJA automáticamente
```

**Conceptos clave**:
- `remember`: Guarda el valor entre recomposiciones
- `mutableStateOf`: Crea un valor observable
- **Cuando el estado cambia → la UI se redibuja automáticamente**

### Estado en ViewModel (recomendado para TarotAI)

```kotlin
// ViewModel
@HiltViewModel
class ReadingViewModel @Inject constructor(...) : ViewModel() {
    private val _selectedCard = MutableStateFlow<TarotCard?>(null)
    val selectedCard: StateFlow<TarotCard?> = _selectedCard.asStateFlow()

    fun selectCard(card: TarotCard) {
        _selectedCard.value = card  // Actualiza el estado
    }
}

// UI
@Composable
fun ReadingScreen(viewModel: ReadingViewModel) {
    val selectedCard by viewModel.selectedCard.collectAsState()  // Observa el estado

    if (selectedCard != null) {
        Text("Carta seleccionada: ${selectedCard.name}")
    } else {
        Text("No hay carta seleccionada")
    }
}
```

---

## 📜 Listas: LazyColumn y LazyRow

Para mostrar listas grandes eficientemente (como las 78 cartas):

```kotlin
@Composable
fun EncyclopediaList(cards: List<TarotCard>) {
    LazyColumn {  // Solo dibuja los items visibles (eficiente)
        items(cards) { card ->  // Por cada carta
            CardListItem(card)
        }
    }
}

@Composable
fun CardListItem(card: TarotCard) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { /* navegar a detalle */ }
    ) {
        Image(
            painter = painterResource(id = getCardDrawable(card.id)),
            contentDescription = card.name,
            modifier = Modifier.size(50.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(card.name, style = MaterialTheme.typography.titleMedium)
            Text(card.arcanaType.toString(), style = MaterialTheme.typography.bodySmall)
        }
    }
}
```

---

## 🚀 Navegación: Compose Navigation

```kotlin
@Composable
fun TarotNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "main"  // Pantalla inicial
    ) {
        composable("main") {
            MainScreen(
                onNewReading = { navController.navigate("spread_type") },
                onEncyclopedia = { navController.navigate("encyclopedia") }
            )
        }

        composable("spread_type") {
            SpreadTypeScreen(
                onSpreadSelected = { type ->
                    navController.navigate("question/$type")
                }
            )
        }

        composable("question/{spreadType}") { backStackEntry ->
            val spreadType = backStackEntry.arguments?.getString("spreadType")
            QuestionScreen(
                spreadType = spreadType,
                onContinue = { question ->
                    navController.navigate("reading/$spreadType/$question")
                }
            )
        }

        composable("encyclopedia") {
            EncyclopediaScreen(
                onCardClick = { cardId ->
                    navController.navigate("card_detail/$cardId")
                }
            )
        }

        composable("card_detail/{cardId}") { backStackEntry ->
            val cardId = backStackEntry.arguments?.getString("cardId")?.toInt()
            CardDetailScreen(
                cardId = cardId,
                onBack = { navController.popBackStack() }  // Volver atrás
            )
        }
    }
}
```

**Navegación básica**:
- `navController.navigate("ruta")` - Ir a otra pantalla
- `navController.popBackStack()` - Volver atrás
- `"ruta/{param}"` - Pasar parámetros

---

## 🎭 Material 3: Tema y Diseño

```kotlin
@Composable
fun TarotAITheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = darkColorScheme(  // Esquema de colores oscuro
            primary = Color(0xFF9C27B0),      // Morado místico
            secondary = Color(0xFFFFD700),    // Dorado
            background = Color(0xFF121212),   // Negro
            surface = Color(0xFF1E1E1E)       // Gris oscuro
        ),
        typography = Typography(
            headlineLarge = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.Bold),
            titleMedium = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Medium),
            bodyMedium = TextStyle(fontSize = 14.sp)
        )
    ) {
        content()  // Tu UI va aquí
    }
}

// Uso
@Composable
fun MyApp() {
    TarotAITheme {
        Surface(  // Fondo de la app
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            TarotNavHost(...)
        }
    }
}
```

**Componentes Material 3**:
- `Card` - Tarjetas con elevación
- `Button`, `OutlinedButton`, `TextButton` - Botones
- `TextField` - Campo de texto
- `Icon` - Iconos
- `Surface` - Contenedor con color y elevación
- `Scaffold` - Layout con TopBar, BottomBar, FAB

---

## 🎬 Animaciones en Compose

### Animación simple
```kotlin
@Composable
fun PulsingCard() {
    var isLarge by remember { mutableStateOf(false) }
    val size by animateDpAsState(targetValue = if (isLarge) 200.dp else 100.dp)

    Image(
        painter = painterResource(R.drawable.card_back),
        contentDescription = null,
        modifier = Modifier
            .size(size)  // Tamaño animado
            .clickable { isLarge = !isLarge }
    )
}
```

### Animación de flip para cartas (del plan.md)
```kotlin
@Composable
fun CardFlipAnimation(
    isFaceUp: Boolean,
    frontContent: @Composable () -> Unit,
    backContent: @Composable () -> Unit
) {
    val rotation by animateFloatAsState(
        targetValue = if (isFaceUp) 180f else 0f,
        animationSpec = tween(durationMillis = 600)  // Duración de la animación
    )

    Box(modifier = Modifier.graphicsLayer { rotationY = rotation }) {
        if (rotation <= 90f) {
            backContent()  // Muestra el dorso
        } else {
            Box(modifier = Modifier.graphicsLayer { rotationY = 180f }) {
                frontContent()  // Muestra el frente
            }
        }
    }
}

// Uso
@Composable
fun TarotCardView(card: TarotCard, isRevealed: Boolean) {
    CardFlipAnimation(
        isFaceUp = isRevealed,
        frontContent = {
            Image(painter = painterResource(card.imageRes), ...)
        },
        backContent = {
            Image(painter = painterResource(R.drawable.card_back), ...)
        }
    )
}
```

---

## 🖼️ Cargar Imágenes con Coil

```kotlin
@Composable
fun CardImage(card: TarotCard) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(card.imagePath)
            .crossfade(true)  // Transición suave
            .build(),
        contentDescription = card.name,
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(3f / 5f),  // Ratio 3:5 para cartas
        contentScale = ContentScale.Crop,
        placeholder = painterResource(R.drawable.card_placeholder),  // Mientras carga
        error = painterResource(R.drawable.card_error)  // Si falla
    )
}
```

---

## 📱 Ejemplo Completo: Pantalla de Enciclopedia

```kotlin
@Composable
fun EncyclopediaScreen(
    viewModel: EncyclopediaViewModel = hiltViewModel(),
    onCardClick: (Int) -> Unit
) {
    // Observar estado
    val uiState by viewModel.cardsState.collectAsState()

    // Cargar cartas al entrar
    LaunchedEffect(Unit) {
        viewModel.loadCards()
    }

    // UI según el estado
    when (val state = uiState) {
        is UiState.Idle -> {
            // No hacer nada
        }

        is UiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()  // Spinner de carga
            }
        }

        is UiState.Success -> {
            CardsList(
                cards = state.data,
                onCardClick = onCardClick
            )
        }

        is UiState.Error -> {
            ErrorScreen(
                message = state.message,
                onRetry = { viewModel.loadCards() }
            )
        }
    }
}

@Composable
fun CardsList(cards: List<TarotCard>, onCardClick: (Int) -> Unit) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(cards, key = { it.id }) { card ->
            CardListItem(
                card = card,
                onClick = { onCardClick(card.id) }
            )
        }
    }
}

@Composable
fun CardListItem(card: TarotCard, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = card.imagePath,
                contentDescription = null,
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = card.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = when (card.arcanaType) {
                        ArcanaType.MAJOR -> "Arcano Mayor"
                        ArcanaType.MINOR -> "Arcano Menor - ${card.suit}"
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Ver detalle",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun ErrorScreen(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Error: $message",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRetry) {
            Text("Reintentar")
        }
    }
}
```

---

## 🎯 Mejores Prácticas para TarotAI

### 1. **Separar lógica de UI**
```kotlin
// ❌ MAL: Lógica en el Composable
@Composable
fun BadScreen() {
    var cards by remember { mutableStateOf<List<Card>>(emptyList()) }
    LaunchedEffect(Unit) {
        cards = database.getAllCards()  // ❌ NO
    }
}

// ✅ BIEN: Lógica en ViewModel
@Composable
fun GoodScreen(viewModel: EncyclopediaViewModel = hiltViewModel()) {
    val cards by viewModel.cards.collectAsState()  // ✅ SÍ
}
```

### 2. **Composables pequeños y reutilizables**
```kotlin
// ✅ Composables pequeños
@Composable
fun TarotCard(card: TarotCard) { ... }

@Composable
fun TarotCardGrid(cards: List<TarotCard>) {
    LazyVerticalGrid(columns = GridCells.Fixed(3)) {
        items(cards) { card ->
            TarotCard(card)  // Reutilizable
        }
    }
}
```

### 3. **Preview para desarrollo rápido**
```kotlin
@Preview(showBackground = true)
@Composable
fun PreviewCardListItem() {
    TarotAITheme {
        CardListItem(
            card = TarotCard(
                id = 0,
                name = "El Loco",
                arcanaType = ArcanaType.MAJOR,
                suit = null,
                imagePath = "",
                generalMeaning = "...",
                uprightMeaning = "...",
                reversedMeaning = "...",
                symbolism = "...",
                keywords = listOf("inicio", "locura")
            ),
            onClick = {}
        )
    }
}
```

### 4. **Manejar estados correctamente**
```kotlin
@Composable
fun MyScreen(viewModel: MyViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()

    when (state) {
        is UiState.Idle -> IdleView()
        is UiState.Loading -> LoadingView()
        is UiState.Success -> SuccessView(state.data)
        is UiState.Error -> ErrorView(state.message)
    }
}
```

---

## 📚 Recursos para Aprender Más

1. **Documentación oficial**: https://developer.android.com/jetpack/compose
2. **Codelabs**: https://developer.android.com/codelabs/jetpack-compose-basics
3. **Compose Samples**: https://github.com/android/compose-samples

---

## 🎓 Resumen: Conceptos Clave

| Concepto | Qué es | Ejemplo |
|----------|--------|---------|
| `@Composable` | Función que dibuja UI | `@Composable fun MyScreen()` |
| `remember` | Guarda estado entre recomposiciones | `var count by remember { mutableStateOf(0) }` |
| `Modifier` | Personaliza aspecto/comportamiento | `.size(100.dp).clickable { }` |
| `LazyColumn` | Lista eficiente (solo dibuja visible) | `LazyColumn { items(list) { ... } }` |
| `StateFlow` | Flujo observable desde ViewModel | `val state by viewModel.state.collectAsState()` |
| `NavHost` | Navegación entre pantallas | `navController.navigate("screen")` |
| `MaterialTheme` | Tema de colores y tipografía | `MaterialTheme.colorScheme.primary` |

---

**¿Listo para implementar?** Con estos conocimientos ya puedes entender el código Compose que vamos a escribir. ¡Vamos a construir TarotAI! 🚀
