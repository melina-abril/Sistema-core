package com.empresa.sistemacore.shared.domain;

import java.time.LocalDate;
import java.util.Objects;

public record FechaOperativa(LocalDate valor) {
    public FechaOperativa {
        Objects.requireNonNull(valor, "La fecha operativa es obligatoria");
    }
}
