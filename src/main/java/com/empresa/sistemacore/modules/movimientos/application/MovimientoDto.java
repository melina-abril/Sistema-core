package com.empresa.sistemacore.modules.movimientos.application;

import com.empresa.sistemacore.modules.movimientos.domain.Movimiento;
import com.empresa.sistemacore.modules.movimientos.domain.TipoMovimiento;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public final class MovimientoDto {
    private MovimientoDto() { }

    public record Request(
            @NotNull @Positive Long cuentaId,
            @NotNull TipoMovimiento tipo,
            @NotNull @Positive BigDecimal importe,
            @NotNull LocalDate fechaOperativa,
            @NotBlank String concepto
    ) { }

    public record AdjustmentRequest(
            @NotNull TipoMovimiento direccion,
            @NotNull @Positive BigDecimal importe,
            @NotBlank String concepto
    ) { }

    public record Response(
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
            Long movimientoOriginalId,
            BigDecimal saldoResultante
    ) {
        public static Response from(Movimiento movimiento, BigDecimal saldoResultante) {
            return new Response(movimiento.id(), movimiento.cuentaId(), movimiento.tipo(),
                    movimiento.direccion(), movimiento.importe(), movimiento.fechaOperativa(),
                    movimiento.concepto(), movimiento.usuario(), movimiento.fechaHora(),
                    movimiento.cerrado(), movimiento.movimientoOriginalId(), saldoResultante);
        }
    }

    public record SearchResponse(List<Response> movimientos, BigDecimal saldoResultante) { }

    public record DeletionResponse(long id, String estado, String usuario, LocalDateTime fechaHora,
                                   BigDecimal saldoResultante) { }
}
