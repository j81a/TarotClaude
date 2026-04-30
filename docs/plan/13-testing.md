# 13. Testing Strategy

## 13.1 Pirámide de Testing

```
      ┌─────────┐
      │   E2E   │  (Pocos - flujos críticos)
      │  Tests  │
     ┌┴─────────┴┐
     │Integration│  (Algunos - interacción entre capas)
     │   Tests   │
    ┌┴───────────┴┐
    │    Unit     │  (Muchos - lógica de negocio)
    │    Tests    │
    └─────────────┘
```

**Unit Tests (prioritarios):**
- ✅ Use Cases (lógica de negocio)
- ✅ ViewModels (gestión de estado)
- ✅ Algoritmo de selección aleatoria de cartas
- ✅ Parseo de JSON de Claude

**Integration Tests:**
- ✅ Room DAOs
- ✅ Repository con DB

**UI Tests (mínimos para MVP):**
- ✅ Flujo completo de tirada simple
- ✅ Navegación a enciclopedia
