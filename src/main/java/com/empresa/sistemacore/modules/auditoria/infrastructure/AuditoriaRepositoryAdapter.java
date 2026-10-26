package com.empresa.sistemacore.modules.auditoria.infrastructure;

import com.empresa.sistemacore.modules.auditoria.domain.AuditoriaRepository;
import com.empresa.sistemacore.modules.auditoria.domain.RegistroAuditoria;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class AuditoriaRepositoryAdapter implements AuditoriaRepository {
    // Respuesta dummy fija: reemplazar por inserción y consulta append-only en Oracle.
    private static final List<RegistroAuditoria> EXAMPLE_RECORDS = List.of(
            new RegistroAuditoria(1L, "CLIENTE", "ALTA", null,
                    "{\"numeroCliente\":\"CLI-1042\",\"estado\":\"ACTIVO\"}",
                    "operador.maria", LocalDateTime.of(2025, 3, 10, 9, 15, 32)),
            new RegistroAuditoria(2L, "MOVIMIENTO", "ALTA", null,
                    "{\"cuenta\":\"CTA-2081\",\"tipo\":\"CREDITO\",\"importe\":1250.00}",
                    "operador.maria", LocalDateTime.of(2025, 3, 10, 10, 42, 8))
    );

    @Override
    public void append(RegistroAuditoria registro) {
        // Dummy: no persiste ni modifica los registros de ejemplo; reemplazar por INSERT.
    }

    @Override
    public List<RegistroAuditoria> findAll() {
        return EXAMPLE_RECORDS;
    }
}
