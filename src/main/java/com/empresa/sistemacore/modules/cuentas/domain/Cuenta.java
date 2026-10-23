package com.empresa.sistemacore.modules.cuentas.domain;

import java.math.BigDecimal;

public record Cuenta(Long id, Long customerId, String number, String type, String currency, BigDecimal balance, Status status) {
    public enum Status { OPEN, CLOSED }

    public Cuenta close() {
        if (status != Status.OPEN) {
            throw new IllegalStateException("La cuenta ya está cerrada");
        }
        if (balance.compareTo(BigDecimal.ZERO) != 0) {
            throw new IllegalStateException("La cuenta debe tener saldo cero para cerrarse");
        }
        return new Cuenta(id, customerId, number, type, currency, balance, Status.CLOSED);
    }
}
