# Sistema Core — backend

Monolito modular en Java 21 y Spring Boot 3. Los módulos `autenticacion` e `inicio` exponen, respectivamente, `POST /api/auth/login`, `POST /api/auth/logout`, `POST /api/auth/usuarios/{id}/desbloqueo` y `GET /api/inicio/resumen`. Spring Boot detecta sus controladores mediante el escaneo del paquete `com.empresa.sistemacore`; no hace falta registrar rutas manualmente.

## Desarrollo

Necesitas Java 21, Maven y PostgreSQL. Aplica primero las migraciones y los datos de ejemplo del proyecto de base de datos; este backend no crea ni migra tablas. El perfil `dev`, activo por defecto, usa `jdbc:postgresql://localhost:5432/sistema_core`, usuario `sistema_core` y contraseña de ejemplo `change-me`, salvo que definas `DB_URL`, `DB_USER` y `DB_PASSWORD`. Configura `JWT_SECRET` con al menos 32 bytes fuera del entorno local.

```sh
mvn spring-boot:run
mvn -B package
```

Para producción, establece `SPRING_PROFILES_ACTIVE=prod` y todas las variables de `deploy/application-prod.env.example` con valores propios. Nunca utilices sus valores de ejemplo en producción. El esquema pertenece exclusivamente a las migraciones de la base de datos (`ddl-auto: none`).

## Convenciones

Cada módulo reside en `modules/{modulo}` y separa `domain`, `application`, `infrastructure` y `api`. La autenticación utiliza JWT; salvo el inicio de sesión y `/actuator/health`, los endpoints requieren un token Bearer. El desbloqueo requiere el rol `SUPERVISOR`. Las tablas compartidas aprobadas son `usuarios`, `fecha_operativa_vigente` y `auditoria`.
