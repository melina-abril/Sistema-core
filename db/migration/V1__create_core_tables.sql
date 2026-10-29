CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- Usuarios compartidos por autenticación y los módulos transaccionales.
CREATE TABLE usuarios (
    id UUID NOT NULL DEFAULT gen_random_uuid(),
    username VARCHAR(50) NOT NULL,
    nombre VARCHAR(120) NOT NULL,
    password_hash VARCHAR(100) NOT NULL,
    rol VARCHAR(20) NOT NULL,
    estado VARCHAR(20) NOT NULL DEFAULT 'ACTIVO',
    intentos_fallidos SMALLINT NOT NULL DEFAULT 0,
    bloqueado_en TIMESTAMPTZ NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    CONSTRAINT pk_usuarios PRIMARY KEY (id),
    CONSTRAINT uq_usuarios_username UNIQUE (username),
    CONSTRAINT ck_usuarios_rol CHECK (rol IN ('OPERADOR', 'SUPERVISOR')),
    CONSTRAINT ck_usuarios_estado CHECK (estado IN ('ACTIVO', 'BLOQUEADO')),
    CONSTRAINT ck_usuarios_intentos_fallidos CHECK (intentos_fallidos BETWEEN 0 AND 5)
);

COMMENT ON TABLE usuarios IS 'Usuarios operativos, roles y estado de acceso al sistema.';

-- Fecha operativa única, compartida por el inicio y el cierre diario.
CREATE TABLE fecha_operativa_vigente (
    id SMALLINT NOT NULL,
    fecha DATE NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    CONSTRAINT pk_fecha_operativa_vigente PRIMARY KEY (id),
    CONSTRAINT ck_fecha_operativa_vigente_id CHECK (id = 1)
);

COMMENT ON TABLE fecha_operativa_vigente IS 'Única fecha operativa vigente, independiente de la fecha calendario.';

-- Registro compartido de auditoría: sus entradas no pueden modificarse ni borrarse.
CREATE TABLE auditoria (
    id BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL,
    entidad VARCHAR(30) NOT NULL,
    entidad_id VARCHAR(50) NOT NULL,
    accion VARCHAR(30) NOT NULL,
    valores_anteriores JSONB NULL,
    valores_nuevos JSONB NULL,
    usuario VARCHAR(50) NOT NULL,
    fecha TIMESTAMPTZ NOT NULL DEFAULT now(),
    CONSTRAINT pk_auditoria PRIMARY KEY (id)
);

COMMENT ON TABLE auditoria IS 'Log append-only de operaciones y eventos de autenticación.';

CREATE INDEX ix_auditoria_entidad_entidad_id ON auditoria (entidad, entidad_id);
CREATE INDEX ix_auditoria_usuario ON auditoria (usuario);
CREATE INDEX ix_auditoria_fecha ON auditoria (fecha);

CREATE FUNCTION reject_auditoria_mutation() RETURNS trigger
LANGUAGE plpgsql
AS $$
BEGIN
    RAISE EXCEPTION 'Los registros de auditoria no pueden modificarse ni eliminarse';
END;
$$;

CREATE TRIGGER trg_auditoria_append_only
BEFORE UPDATE OR DELETE ON auditoria
FOR EACH ROW
EXECUTE FUNCTION reject_auditoria_mutation();
