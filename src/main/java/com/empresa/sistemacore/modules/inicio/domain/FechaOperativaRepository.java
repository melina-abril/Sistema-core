package com.empresa.sistemacore.modules.inicio.domain;

import java.time.LocalDate;
import java.util.Optional;

/** Puerto de lectura de la fecha operativa vigente. */
public interface FechaOperativaRepository {

    Optional<LocalDate> vigente();
}
