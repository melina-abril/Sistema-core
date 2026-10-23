package com.empresa.sistemacore.shared.domain;

import java.time.LocalDate;
import java.util.Objects;

public record FechaOperativa(LocalDate value) {
    public FechaOperativa {
        Objects.requireNonNull(value, "Operating date is required");
    }
}
