package com.empresa.sistemacore.modules.autenticacion.domain;

import com.empresa.sistemacore.shared.security.Roles;
import java.time.Instant;
import java.util.Objects;

public final class Usuario {

    public enum Estado { ACTIVO, BLOQUEADO }

    private final String id;
    private final String username;
    private final String nombre;
    private final String passwordHash;
    private final Roles rol;
    private Estado estado;
    private int intentosFallidos;
    private Instant bloqueadoEn;

    public Usuario(String id, String username, String nombre, String passwordHash, Roles rol,
                   Estado estado, int intentosFallidos, Instant bloqueadoEn) {
        this.id = Objects.requireNonNull(id, "id");
        this.username = Objects.requireNonNull(username, "username");
        this.nombre = Objects.requireNonNull(nombre, "nombre");
        this.passwordHash = Objects.requireNonNull(passwordHash, "passwordHash");
        this.rol = Objects.requireNonNull(rol, "rol");
        this.estado = Objects.requireNonNull(estado, "estado");
        if (intentosFallidos < 0 || intentosFallidos > 5) {
            throw new IllegalArgumentException("El número de intentos fallidos no es válido");
        }
        this.intentosFallidos = intentosFallidos;
        this.bloqueadoEn = bloqueadoEn;
    }

    public void registrarIntentoFallido(Instant fecha) {
        if (estaBloqueado()) {
            return;
        }
        intentosFallidos++;
        if (intentosFallidos >= 5) {
            estado = Estado.BLOQUEADO;
            bloqueadoEn = Objects.requireNonNull(fecha, "fecha");
        }
    }

    public void registrarIngreso() {
        intentosFallidos = 0;
        bloqueadoEn = null;
    }

    public void desbloquear() {
        estado = Estado.ACTIVO;
        registrarIngreso();
    }

    public boolean estaBloqueado() { return estado == Estado.BLOQUEADO; }
    public String getId() { return id; }
    public String getUsername() { return username; }
    public String getNombre() { return nombre; }
    public String getPasswordHash() { return passwordHash; }
    public Roles getRol() { return rol; }
    public Estado getEstado() { return estado; }
    public int getIntentosFallidos() { return intentosFallidos; }
    public Instant getBloqueadoEn() { return bloqueadoEn; }
}
