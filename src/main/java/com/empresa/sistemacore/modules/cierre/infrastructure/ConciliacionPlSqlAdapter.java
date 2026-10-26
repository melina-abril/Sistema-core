package com.empresa.sistemacore.modules.cierre.infrastructure;

import com.empresa.sistemacore.modules.cierre.domain.CierreDiario;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class ConciliacionPlSqlAdapter {
    public record Resultado(BigDecimal totalDebitos, BigDecimal totalCreditos,
                            Map<String, BigDecimal> saldosCierre) {
    }

    public Resultado conciliar(CierreDiario cierre) {
        // Conciliación dummy: reemplazar por el paquete PL/SQL al conectar Oracle.
        if (cierre.fechaOperativa().equals(LocalDate.of(2026, 5, 18))) {
            return new Resultado(new BigDecimal("55680.00"), new BigDecimal("210000.00"),
                    Map.of("CC-1001", new BigDecimal("854320.00"),
                           "CA-2002", new BigDecimal("138000.00")));
        }
        return new Resultado(new BigDecimal("0.00"), new BigDecimal("0.00"),
                Map.of("CC-1001", new BigDecimal("854320.00"),
                       "CA-2002", new BigDecimal("138000.00")));
    }
}
