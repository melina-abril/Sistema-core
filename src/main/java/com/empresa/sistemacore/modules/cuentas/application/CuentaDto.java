package com.empresa.sistemacore.modules.cuentas.application;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public final class CuentaDto {
    private CuentaDto() {}

    public record OpenRequest(@NotNull Long customerId, @NotBlank String type, @NotBlank String currency) {}

    public record Account(Long id, Long customerId, String number, String type, String currency, BigDecimal balance, String status) {}

    public record RecentMovement(Long id, String type, BigDecimal amount, LocalDate date, String concept, BigDecimal resultingBalance) {}

    public record Detail(Account account, List<RecentMovement> recentMovements) {}
}
