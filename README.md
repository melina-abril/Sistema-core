# Sistema Core — backend

Monolito modular en Java 21 y Spring Boot 3. Cada módulo reside en `src/main/java/com/empresa/sistemacore/modules` y sus controladores se registran automáticamente mediante component scanning; no existe un router manual. Los endpoints del MVP devuelven JSON dummy, sin persistencia ni llamadas a sistemas externos.

## Ejecución

Instalar Java 21 y Maven 3.9. Ejecutar `mvn spring-boot:run` y consultar `/actuator/health`. Para las rutas protegidas, configurar `JWT_JWK_SET_URI` con el endpoint de claves de un emisor autorizado y enviar un JWT válido cuyo claim `roles` contenga `OPERADOR`, `SUPERVISOR` o `INTEGRACION`. El valor `example.invalid` es solo un marcador y no valida tokens. No guardar credenciales en el repositorio.

El perfil predeterminado es `dev`. `application.yml` desactiva explícitamente DataSource, JPA y Flyway mientras los adaptadores sean dummy. Las variables Oracle del ejemplo de despliegue quedan reservadas para una futura integración; no activan una conexión. Ver `docs/arquitectura.md` y `docs/runbook-cierre-diario.md`.
