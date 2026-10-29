package com.empresa.sistemacore.modules.inicio.application;

import com.empresa.sistemacore.shared.security.Roles;
import java.time.LocalDate;

/** Resumen de la fecha operativa para la persona autenticada. */
public record ResumenDiaDto(
        String nombre,
        Roles rol,
        LocalDate fechaOperativa,
        long cantidadMovimientos,
        EstadoCierre estadoCierre) {

    public enum EstadoCierre {
        PENDIENTE,
        EN_CURSO,
        COMPLETADO
    }
}
