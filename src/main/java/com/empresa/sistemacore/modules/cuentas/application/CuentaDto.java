package com.empresa.sistemacore.modules.cuentas.application;

import com.empresa.sistemacore.modules.cuentas.domain.Cuenta;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public final class CuentaDto {
    private CuentaDto() {}

    public record OpenRequest(@NotNull Long clienteId, @NotNull Cuenta.Tipo tipo, @NotNull Cuenta.Moneda moneda) {}

    public record Summary(Long id, Long clienteId, String numero, Cuenta.Tipo tipo,
                          Cuenta.Moneda moneda, BigDecimal saldo, Cuenta.Estado estado) {
        public static Summary from(Cuenta cuenta) {
            return new Summary(cuenta.id(), cuenta.clienteId(), cuenta.numero(), cuenta.tipo(),
                    cuenta.moneda(), cuenta.saldo(), cuenta.estado());
        }
    }

    public record RecentMovement(Long id, LocalDate fecha, String tipo, String concepto,
                                 BigDecimal importe, BigDecimal saldoResultante) {}

    public record Detail(Long id, Long clienteId, String numero, Cuenta.Tipo tipo,
                         Cuenta.Moneda moneda, BigDecimal saldo, Cuenta.Estado estado,
                         List<RecentMovement> ultimosMovimientos) {
        public static Detail from(Cuenta cuenta, List<RecentMovement> movements) {
            return new Detail(cuenta.id(), cuenta.clienteId(), cuenta.numero(), cuenta.tipo(),
                    cuenta.moneda(), cuenta.saldo(), cuenta.estado(), List.copyOf(movements));
        }
    }
}