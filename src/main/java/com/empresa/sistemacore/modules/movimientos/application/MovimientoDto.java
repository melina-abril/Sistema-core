package com.empresa.sistemacore.modules.movimientos.application;

import com.empresa.sistemacore.modules.movimientos.domain.Movimiento;
import com.empresa.sistemacore.modules.movimientos.domain.TipoMovimiento;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

public final class MovimientoDto {
    private MovimientoDto() {
    }

    public record Request(
            @NotNull Long cuentaId,
            @NotNull TipoMovimiento tipo,
            @NotNull @Positive BigDecimal importe,
            @NotNull LocalDate fechaOperativa,
            @NotBlank String concepto
    ) {
    }

    public record AjusteRequest(
            @NotNull TipoMovimiento direccion,
            @NotNull @Positive BigDecimal importe,
            @NotBlank String concepto
    ) {
    }

    public record Response(
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
        public static Response from(Movimiento movimiento) {
            return new Response(movimiento.id(), movimiento.cuentaId(), movimiento.tipo(),
                    movimiento.importe(), movimiento.fechaOperativa(), movimiento.concepto(),
                    movimiento.usuario(), movimiento.registradoEn(), movimiento.cerrado(),
                    movimiento.saldoResultante(), movimiento.movimientoOriginalId());
        }
    }

    public record ConsultaResponse(List<Response> movimientos, BigDecimal saldoResultante) {
    }

    public record BajaResponse(Long id, String estado, String usuario, OffsetDateTime registradoEn) {
    }
}
