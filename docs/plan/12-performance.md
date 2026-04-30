# 12. Performance

## 12.1 Optimizaciones

**Carga de imágenes:**
- Usar Coil con caché en memoria y disco
- Lazy loading en listas (LazyColumn)
- Placeholder mientras carga

**Base de datos:**
- Índices en columnas de búsqueda frecuente
- Queries asíncronas con Coroutines
- Prepoblar DB en background al instalar

**Compose:**
- `remember` para evitar recomposiciones innecesarias
- `derivedStateOf` para cálculos derivados
- `key()` en listas para identificación estable
