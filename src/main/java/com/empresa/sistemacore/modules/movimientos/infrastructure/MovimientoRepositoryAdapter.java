package com.empresa.sistemacore.modules.movimientos.infrastructure;

import com.empresa.sistemacore.modules.movimientos.domain.Movimiento;
import com.empresa.sistemacore.modules.movimientos.domain.MovimientoRepository;
import com.empresa.sistemacore.modules.movimientos.domain.TipoMovimiento;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class MovimientoRepositoryAdapter implements MovimientoRepository {
    // Repositorio dummy de solo lectura; reemplazar por un adaptador Oracle sin acceder a tablas de otros módulos.
    private static final List<Movimiento> EJEMPLOS = List.of(
            new Movimiento(500L, 1001L, TipoMovimiento.CREDITO, TipoMovimiento.CREDITO,
                    new BigDecimal("1000.00"), LocalDate.of(2025, 3, 9), "Depósito inicial",
                    "operador.demo", LocalDateTime.of(2025, 3, 9, 9, 15), true, null),
            new Movimiento(501L, 1001L, TipoMovimiento.CREDITO, TipoMovimiento.CREDITO,
                    new BigDecimal("300.00"), LocalDate.of(2025, 3, 10), "Cobro de servicio",
                    "operador.demo", LocalDateTime.of(2025, 3, 10, 10, 0), false, null),
            new Movimiento(502L, 1001L, TipoMovimiento.DEBITO, TipoMovimiento.DEBITO,
                    new BigDecimal("50.00"), LocalDate.of(2025, 3, 10), "Pago de comisión",
                    "operador.demo", LocalDateTime.of(2025, 3, 10, 10, 20), false, null)
    );

    @Override
    public List<Movimiento> findAll() {
        return EJEMPLOS;
    }

    @Override
    public Optional<Movimiento> findById(long id) {
        return EJEMPLOS.stream().filter(movimiento -> movimiento.id() == id).findFirst();
    }

    @Override
    public BigDecimal saldoActual(long cuentaId) {
        return cuentaId == 1001L ? new BigDecimal("1250.00") : BigDecimal.ZERO;
    }
}
