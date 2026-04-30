# 11. Manejo de Errores

## 11.1 Estrategias por Capa

**Networking (API Claude):**
- Timeout: 30 segundos
- Reintentos: 2 intentos con backoff exponencial
- Errores HTTP mapeados a mensajes amigables:
  - 401: "Error de autenticación"
  - 429: "Demasiadas solicitudes, intenta más tarde"
  - 500: "Error del servidor, intenta nuevamente"

**Base de Datos:**
- Si falla carga inicial: mostrar mensaje y botón "Reintentar"
- Si falta una carta: usar placeholder

**UI:**
- Todos los estados de error muestran:
  - Mensaje descriptivo
  - Botón "Reintentar"
  - Opción de volver atrás
