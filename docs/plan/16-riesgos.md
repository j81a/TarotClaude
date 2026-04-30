# 16. Riesgos y Mitigaciones

| Riesgo | Probabilidad | Impacto | Mitigación |
|--------|--------------|---------|------------|
| **API de Claude inaccesible** | Media | Alto | Implementar caché de última interpretación + mensaje claro de error |
| **Imágenes de cartas de baja calidad** | Media | Medio | Buscar múltiples fuentes, validar visualmente antes de integrar |
| **Contenido de enciclopedia impreciso** | Media | Alto | Validación manual + consulta con expertos en tarot |
| **Límites de API de Claude (rate limiting)** | Alta | Medio | Implementar backoff exponencial + mensaje al usuario |
| **Tamaño del APK por imágenes** | Alta | Bajo | Comprimir imágenes WebP, máx 200KB por carta |
| **Performance en animaciones** | Baja | Medio | Testing en dispositivos de gama baja |
