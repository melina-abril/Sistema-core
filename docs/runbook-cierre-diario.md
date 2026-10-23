# Cierre diario — procedimiento operativo

1. Confirmar la fecha operativa y consultar `GET /api/cierre-diario`.
2. Un supervisor autorizado ejecuta `POST /api/cierre-diario` una sola vez para la fecha.
3. Revisar el estado, los totales y la conciliación. Si hay una falla, registrar su motivo y mantener abierto el día.
4. Tras resolver la causa, el supervisor utiliza `POST /api/cierre-diario/reproceso` y verifica el resultado antes del siguiente día hábil.

Las respuestas actuales son dummy: este procedimiento no constituye evidencia de un cierre real. No avanzar la fecha operativa basándose en ellas.
