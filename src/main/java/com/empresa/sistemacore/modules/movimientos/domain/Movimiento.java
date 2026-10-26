package com.empresa.sistemacore.modules.movimientos.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;

public record Movimiento(
        Long id,
        Long cuentaId,
        TipoMovimiento tipo,
        BigDecimal importe,
        LocalDate fechaOperativa,
        String concepto,
        String usuario,
        OffsetDateTime registradoEn,
        boolean cerrado,
        BigDecimal saldoResultante,
        Long movimientoOriginalId
) {
}
