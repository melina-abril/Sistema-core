package com.empresa.sistemacore.modules.autenticacion.domain;

import java.time.Instant;

/** Evento de acceso sin credenciales ni tokens; el adaptador de auditoría lo registra sin modificarlo. */
public record AuthEvent(String entityId, String username, String action, String actor, Instant fecha) {
}
