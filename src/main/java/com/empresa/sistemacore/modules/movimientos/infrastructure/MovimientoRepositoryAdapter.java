package com.empresa.sistemacore.modules.movimientos.infrastructure;

import com.empresa.sistemacore.modules.movimientos.domain.Movimiento;
import com.empresa.sistemacore.modules.movimientos.domain.MovimientoRepository;
import com.empresa.sistemacore.modules.movimientos.domain.TipoMovimiento;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class MovimientoRepositoryAdapter implements MovimientoRepository {
    // Datos dummy de solo lectura; reemplazar por el adaptador Oracle al conectar la persistencia.
    private static final List<Movimiento> SAMPLE = List.of(
            new Movimiento(1L, 1001L, TipoMovimiento.DEBITO, new BigDecimal("100.00"),
                    LocalDate.of(2026, 4, 30), "Pago de servicio", "operador.demo",
                    OffsetDateTime.parse("2026-04-30T10:15:00-03:00"), true,
                    new BigDecimal("1200.00"), null),
            new Movimiento(2L, 1001L, TipoMovimiento.CREDITO, new BigDecimal("300.00"),
                    LocalDate.of(2026, 5, 1), "Depósito", "operador.demo",
                    OffsetDateTime.parse("2026-05-01T09:30:00-03:00"), false,
                    new BigDecimal("1500.00"), null),
            new Movimiento(3L, 1001L, TipoMovimiento.DEBITO, new BigDecimal("75.00"),
                    LocalDate.of(2026, 5, 1), "Compra", "operador.demo",
                    OffsetDateTime.parse("2026-05-01T11:00:00-03:00"), false,
                    new BigDecimal("1425.00"), null)
    );

    @Override
    public List<Movimiento> findAll() {
        return SAMPLE;
    }

    @Override
    public Optional<Movimiento> findById(Long id) {
        return SAMPLE.stream().filter(movimiento -> movimiento.id().equals(id)).findFirst();
    }
}
