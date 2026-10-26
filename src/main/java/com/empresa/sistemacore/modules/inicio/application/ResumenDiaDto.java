package com.empresa.sistemacore.modules.inicio.application;

import java.time.LocalDate;

public record ResumenDiaDto(
        String nombreUsuario,
        String rol,
        LocalDate fechaOperativa,
        long cantidadMovimientos,
        String estadoCierre
) {
}
