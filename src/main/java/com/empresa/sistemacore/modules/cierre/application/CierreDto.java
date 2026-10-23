package com.empresa.sistemacore.modules.cierre.application;

import com.empresa.sistemacore.modules.cierre.domain.CierreDiario;
import com.empresa.sistemacore.modules.cierre.domain.EstadoCierre;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

public record CierreDto(
        LocalDate fechaOperativa,
        int cantidadMovimientos,
        EstadoCierre estado,
        BigDecimal totalDebitos,
        BigDecimal totalCreditos,
        Map<String, BigDecimal> saldosCierre,
        String motivoError) {

    public static CierreDto from(CierreDiario cierre) {
        return new CierreDto(cierre.fechaOperativa(), cierre.cantidadMovimientos(),
                cierre.estado(), cierre.totalDebitos(), cierre.totalCreditos(),
                cierre.saldosCierre(), cierre.motivoError());
    }
}
