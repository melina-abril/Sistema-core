# Sistema Core — backend

Monolito modular con Java 21, Spring Boot 3 y Maven. Spring detecta automáticamente los controladores bajo `com.empresa.sistemacore.modules`; no existe un router central que requiera registrar módulos manualmente. Esta generación incluye **Inicio de sesión** (`/api/auth/login`, `/api/auth/logout`, `/api/auth/usuarios/{id}/desbloqueo`) y **Pantalla de inicio** (`/api/inicio/resumen`).

## Desarrollo local

Requisitos: JDK 21 y Maven. Ejecutar `mvn spring-boot:run`; para compilar y ejecutar pruebas, `mvn -B package` y `mvn -B verify`. El perfil predeterminado es `dev` y utiliza un secreto JWT exclusivamente local. Para usar otro secreto, definir `JWT_SECRET` con al menos 32 bytes antes de arrancar. La comprobación pública de disponibilidad es `GET /actuator/health`. Los demás endpoints, salvo `POST /api/auth/login`, requieren autenticación; el cliente debe usar el JWT recibido en `Authorization: Bearer <token>` y conservar el JWT renovado que devuelve `X-Auth-Token`. La sesión vence tras 30 minutos sin actividad.

## Arquitectura y persistencia

Cada módulo reside en `modules/{modulo}` y separa dominio y puertos, casos de uso, adaptadores y API. El dominio no depende de Spring ni accede directamente a los datos de otro módulo. La seguridad y las respuestas de error `ProblemDetail` están en `shared`. Actualmente no existe una base de datos aprobada: no se configura DataSource, JPA ni migraciones. La persistencia de los módulos es en memoria y se pierde al reiniciar; no debe emplearse para operación productiva ni como registro de auditoría duradero. Para incorporar una base aprobada se necesitan el driver y la dependencia de acceso a datos correspondientes, además de reemplazar los adaptadores en memoria sin cambiar los puertos de dominio.

## Producción en RHEL

Configurar `SPRING_PROFILES_ACTIVE=prod` y un `JWT_SECRET` aleatorio de al menos 32 bytes. Usar `deploy/application-prod.env.example` como guía, nunca como secreto real. Instalar el JAR en `/opt/sistema-core/sistema-core.jar`, el entorno protegido en `/etc/sistema-core/application.env` y `deploy/sistema-core.service` como unidad systemd. El pipeline de Jenkins compila, ejecuta las pruebas —incluidas las reglas ArchUnit cuando estén presentes—, archiva el JAR y exige aprobación para desplegar fuera del horario operativo. Su agente `rhel-deploy` debe ejecutarse en el servidor de destino y tener permisos controlados para instalar el artefacto y reiniciar el servicio. No desplegar la implementación en memoria como plataforma transaccional productiva.
