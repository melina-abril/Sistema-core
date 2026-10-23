# Arquitectura

Monolito modular con módulos de inicio, clientes, cuentas, movimientos y cierre. Spring detecta automáticamente sus controladores y servicios bajo `com.empresa.sistemacore`; no se importan ni duplican archivos de módulos en el arranque. El dominio no depende de Spring. Las interacciones entre módulos deben utilizar puertos o eventos, nunca acceso directo a tablas de otro módulo.

Las rutas de negocio entregan JSON dummy hasta que se sustituyan sus adaptadores. JWT protege la API; solo SUPERVISOR puede ejecutar o reprocesar cierres, incluso mediante acceso directo. Los errores de negocio se representan con ProblemDetail. Los importes se modelan con BigDecimal. Flyway y Oracle permanecen deshabilitados mientras se utilicen datos dummy.
