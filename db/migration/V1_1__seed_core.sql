-- Usuarios ficticios para probar los roles de operador y supervisor.
INSERT INTO usuarios (username, nombre, password_hash, rol, estado)
VALUES
    ('operador', 'Operador de ejemplo', crypt('Operador123!', gen_salt('bf', 10)), 'OPERADOR', 'ACTIVO'),
    ('supervisor', 'Supervisor de ejemplo', crypt('Supervisor123!', gen_salt('bf', 10)), 'SUPERVISOR', 'ACTIVO');

-- Fecha operativa inicial; solo puede existir la fila con id 1.
INSERT INTO fecha_operativa_vigente (id, fecha)
VALUES (1, DATE '2026-11-02');
