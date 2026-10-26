package com.empresa.sistemacore.modules.auditoria.domain;

import java.time.LocalDateTime;

public record RegistroAuditoria(
        Long id,
        String entidad,
        String accion,
        String valoresAnterior,
        String valoresNuevo,
        String usuario,
        LocalDateTime fechaHora
) {
}
