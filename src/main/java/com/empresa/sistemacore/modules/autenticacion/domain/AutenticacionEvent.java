package com.empresa.sistemacore.modules.autenticacion.domain;

import java.time.Instant;

/** Evento de acceso publicado para su registro append-only por auditoría. */
public record AutenticacionEvent(String usuario, Accion accion, Instant fecha, int intentosFallidos) {

    public enum Accion { INGRESO, INTENTO_FALLIDO, BLOQUEO, DESBLOQUEO, CIERRE_SESION }
}
