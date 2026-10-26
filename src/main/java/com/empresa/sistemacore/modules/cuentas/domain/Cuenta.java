package com.empresa.sistemacore.modules.cuentas.domain;

import java.math.BigDecimal;

public record Cuenta(Long id, Long clienteId, String numero, Tipo tipo, Moneda moneda, BigDecimal saldo, Estado estado) {
    public enum Tipo { CORRIENTE, AHORRO }
    public enum Moneda { ARS, USD }
    public enum Estado { ACTIVA, CERRADA }

    public Cuenta cerrar() {
        if (estado != Estado.ACTIVA || saldo.compareTo(BigDecimal.ZERO) != 0) {
            throw new IllegalStateException("La cuenta debe estar activa y tener saldo cero para cerrarse");
        }
        return new Cuenta(id, clienteId, numero, tipo, moneda, saldo, Estado.CERRADA);
    }
}