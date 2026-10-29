# Base de datos de Sistema Core

PostgreSQL 16 con migraciones SQL aplicadas por Flyway. El backend no ejecuta las migraciones.

## Inicio

Desde la raíz del repositorio:

```sh
cp .env.example .env
docker compose up -d
```

El servicio `db` inicia PostgreSQL y, cuando está listo, el servicio `flyway` aplica las migraciones de `db/migration`. Para comprobar el resultado:

```sh
docker compose logs flyway
```

## Migraciones y tablas

Flyway aplica los archivos en orden de versión:

1. `V1__create_core_tables.sql`: crea las tablas compartidas `usuarios`, `fecha_operativa_vigente` y `auditoria`, además del trigger que impide modificar o borrar entradas de auditoría.
2. `V1_1__seed_core.sql`: carga los usuarios de ejemplo y la fecha operativa inicial.

Los módulos `autenticacion` (Inicio de sesión) e `inicio` (Pantalla de inicio) no tienen tablas propias. Utilizan las tablas compartidas.

## Conexión

Con los valores de `.env.example`, PostgreSQL queda disponible en `localhost:5432`, base `sistema_core`, usuario `sistema_core`. Para abrir una consola SQL sin escribir las credenciales:

```sh
docker compose exec db sh -c 'psql -U "$POSTGRES_USER" -d "$POSTGRES_DB"'
```

Usuarios ficticios de prueba: `operador` / `Operador123!` y `supervisor` / `Supervisor123!`. Sus contraseñas se almacenan como hashes BCrypt. Cambie las credenciales de ejemplo antes de utilizar el sistema fuera de un entorno local.
