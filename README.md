# Sistema Core — backend

Monolito modular Java 21 / Spring Boot 3. Cada módulo expone sus rutas mediante controladores detectados por Spring; no existe un registro manual de rutas. Las respuestas de negocio son JSON dummy y no consultan Oracle ni sistemas externos.

## Inicio local

Instalar Java 21 y Maven 3.9. Ejecutar `mvn spring-boot:run` (perfil `dev` predeterminado). `GET /api/health` es público; las rutas de negocio requieren JWT. Para verificar tokens HS256, definir `JWT_SECRET` con al menos 32 bytes y enviar el rol mediante la lista `roles` del token. Sin secreto, los tokens se rechazan. No utilizar el archivo de ejemplo de despliegue como credencial.

El perfil `prod` conserva los adaptadores dummy y no abre conexiones. Antes de habilitar Oracle, implementar y probar los adaptadores de persistencia, revisar las migraciones y eliminar las exclusiones de autoconfiguración correspondientes. Las definiciones SQL se proporcionan para la futura integración, no se ejecutan en el modo dummy.

Los paquetes `modules/{moduleName}` separan `domain`, `application`, `infrastructure` y `api`; `shared` contiene únicamente código transversal. Véanse `docs/arquitectura.md` y `docs/runbook-cierre-diario.md`.
