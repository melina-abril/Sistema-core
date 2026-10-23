package com.empresa.sistemacore.modules.movimientos.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record Movimiento(
        long id,
        long cuentaId,
        TipoMovimiento tipo,
        TipoMovimiento direccion,
        BigDecimal importe,
        LocalDate fechaOperativa,
        String concepto,
        String usuario,
        LocalDateTime fechaHora,
        boolean cerrado,
        Long movimientoOriginalId
) {
    public Movimiento {
        if (importe == null || importe.signum() <= 0) {
            throw new IllegalArgumentException("El importe debe ser mayor que cero");
        }
        if (direccion != TipoMovimiento.DEBITO && direccion != TipoMovimiento.CREDITO) {
            throw new IllegalArgumentException("La dirección debe ser DEBITO o CREDITO");
        }
    }
}
