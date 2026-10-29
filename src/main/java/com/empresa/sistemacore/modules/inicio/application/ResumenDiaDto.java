package com.empresa.sistemacore.modules.inicio.application;

import com.empresa.sistemacore.shared.security.Roles;
import java.time.LocalDate;

/** Respuesta del resumen correspondiente a la fecha operativa vigente. */
public final class ResumenDiaDto {

    private ResumenDiaDto() {
    }

    public record Respuesta(
            String nombre,
            Roles rol,
            LocalDate fechaOperativa,
            long cantidadMovimientos,
            String estadoCierre) {
    }
}
