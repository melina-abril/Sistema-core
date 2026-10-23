package com.empresa.sistemacore.modules.movimientos.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface MovimientoRepository {
    List<Movimiento> findAll();
    Optional<Movimiento> findById(long id);
    BigDecimal saldoActual(long cuentaId);
}
