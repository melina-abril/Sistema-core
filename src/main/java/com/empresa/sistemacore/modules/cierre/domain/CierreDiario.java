package com.empresa.sistemacore.modules.cierre.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

public record CierreDiario(
        LocalDate fechaOperativa,
        int cantidadMovimientos,
        EstadoCierre estado,
        BigDecimal totalDebitos,
        BigDecimal totalCreditos,
        Map<String, BigDecimal> saldosCierre,
        String motivoError) {

    public CierreDiario {
        saldosCierre = Map.copyOf(saldosCierre);
    }
}
