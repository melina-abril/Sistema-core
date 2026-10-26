package com.empresa.sistemacore.modules.auditoria.application;

import java.time.LocalDateTime;

public record AuditoriaDto(
        Long id,
        String entidad,
        String accion,
        String valoresAnterior,
        String valoresNuevo,
        String usuario,
        LocalDateTime fechaHora
) {
}
