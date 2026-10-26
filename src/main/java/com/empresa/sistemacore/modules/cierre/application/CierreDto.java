package com.empresa.sistemacore.modules.cierre.application;

import com.empresa.sistemacore.modules.cierre.domain.CierreDiario;
import com.empresa.sistemacore.modules.cierre.domain.EstadoCierre;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Map;

public record CierreDto(
        LocalDate fechaOperativa,
        EstadoCierre estado,
        int cantidadMovimientos,
        BigDecimal totalDebitos,
        BigDecimal totalCreditos,
        Map<String, BigDecimal> saldosCierre,
        String motivoError,
        Instant cerradoEn
) {
    public static CierreDto desde(CierreDiario cierre) {
        return new CierreDto(
                cierre.fechaOperativa(), cierre.estado(), cierre.cantidadMovimientos(),
                cierre.totalDebitos(), cierre.totalCreditos(), cierre.saldosCierre(),
                cierre.motivoError(), cierre.cerradoEn()
        );
    }
}
