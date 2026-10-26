# Cierre diario

En la implementación dummy, consultar el estado mediante `GET /api/cierre-diario` con un JWT de SUPERVISOR. Registrar fecha operativa y resultado antes de ejecutar `POST /api/cierre-diario/ejecutar`. No repetir un cierre completado. Si se informa una falla, conservar el día abierto, registrar el motivo y utilizar `POST /api/cierre-diario/reprocesar` únicamente después de revisarlo. Escalar cualquier discrepancia de saldos antes del siguiente día hábil. La conciliación Oracle real todavía no está conectada.
