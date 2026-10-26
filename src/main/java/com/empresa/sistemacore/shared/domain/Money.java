package com.empresa.sistemacore.shared.domain;

import java.math.BigDecimal;
import java.util.Objects;

public record Money(BigDecimal importe, String moneda) {
    public Money {
        Objects.requireNonNull(importe, "El importe es obligatorio");
        if (moneda == null || moneda.isBlank()) throw new IllegalArgumentException("La moneda es obligatoria");
    }

    public Money sumar(Money otro) {
        verificarMoneda(otro);
        return new Money(importe.add(otro.importe), moneda);
    }

    public Money restar(Money otro) {
        verificarMoneda(otro);
        return new Money(importe.subtract(otro.importe), moneda);
    }

    private void verificarMoneda(Money otro) {
        if (!moneda.equals(otro.moneda)) throw new IllegalArgumentException("Las monedas no coinciden");
    }
}
