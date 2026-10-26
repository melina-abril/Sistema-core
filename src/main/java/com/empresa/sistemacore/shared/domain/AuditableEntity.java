package com.empresa.sistemacore.shared.domain;

import java.time.Instant;

public abstract class AuditableEntity {
    private String creadoPor;
    private Instant creadoEn;
    private String modificadoPor;
    private Instant modificadoEn;

    public String getCreadoPor() { return creadoPor; }
    public Instant getCreadoEn() { return creadoEn; }
    public String getModificadoPor() { return modificadoPor; }
    public Instant getModificadoEn() { return modificadoEn; }

    protected void registrarCreacion(String usuario, Instant fechaHora) {
        creadoPor = usuario;
        creadoEn = fechaHora;
        registrarModificacion(usuario, fechaHora);
    }

    protected void registrarModificacion(String usuario, Instant fechaHora) {
        modificadoPor = usuario;
        modificadoEn = fechaHora;
    }
}
