package com.empresa.sistemacore.modules.cierre.domain;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Map;

public record CierreDiario(
        LocalDate fechaOperativa,
        EstadoCierre estado,
        int cantidadMovimientos,
        BigDecimal totalDebitos,
        BigDecimal totalCreditos,
        Map<String, BigDecimal> saldosCierre,
        String motivoError,
        Instant cerradoEn
) {
    public CierreDiario {
        saldosCierre = Map.copyOf(saldosCierre);
    }
}
