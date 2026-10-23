package com.empresa.sistemacore.modules.cierre.domain;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CierreRepository {
    LocalDate fechaOperativaVigente();
    Optional<CierreDiario> findByDate(LocalDate fecha);
    List<CierreDiario> findAll();
    CierreDiario save(CierreDiario cierre);
}
