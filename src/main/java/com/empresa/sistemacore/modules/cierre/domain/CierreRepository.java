package com.empresa.sistemacore.modules.cierre.domain;

import java.time.LocalDate;
import java.util.List;

public interface CierreRepository {
    LocalDate fechaOperativa();

    CierreDiario cierreActual();

    List<CierreDiario> historial();

    void guardar(CierreDiario cierre);

    void avanzarFechaOperativa(LocalDate fechaCerrada);
}
