package com.empresa.sistemacore.modules.cierre.infrastructure;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class ConciliacionPlSqlAdapter {
    public ResultadoConciliacion conciliar(LocalDate fechaOperativa) {
        // Resultado dummy: reemplazar por la conciliación PL/SQL y el bloqueo de movimientos.
        return new ResultadoConciliacion(18, new BigDecimal("42750.00"),
                new BigDecimal("53900.00"),
                Map.of("ARS", new BigDecimal("286150.00")));
    }

    public record ResultadoConciliacion(int cantidadMovimientos, BigDecimal totalDebitos,
            BigDecimal totalCreditos, Map<String, BigDecimal> saldosCierre) {
    }
}
