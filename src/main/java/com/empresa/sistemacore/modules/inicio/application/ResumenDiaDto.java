package com.empresa.sistemacore.modules.inicio.application;

import java.time.LocalDate;
import java.util.List;

public record ResumenDiaDto(
        String userName,
        String role,
        LocalDate operationalDate,
        int movementCount,
        String closureStatus,
        List<Access> accesses
) {
    public record Access(String name, String path) {
    }
}
