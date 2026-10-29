package com.empresa.sistemacore.modules.autenticacion.domain;

import com.empresa.sistemacore.shared.security.Roles;
import java.util.Objects;

/** Usuario y estado de acceso; el repositorio conserva este agregado en memoria. */
public final class Usuario {

    public enum Estado { ACTIVO, BLOQUEADO }

    private final String id;
    private final String username;
    private final String nombre;
    private final String passwordHash;
    private final Roles rol;
    private int intentosFallidos;
    private Estado estado;

    public Usuario(String id, String username, String nombre, String passwordHash, Roles rol) {
        this.id = Objects.requireNonNull(id, "id");
        this.username = Objects.requireNonNull(username, "username");
        this.nombre = Objects.requireNonNull(nombre, "nombre");
        this.passwordHash = Objects.requireNonNull(passwordHash, "passwordHash");
        this.rol = Objects.requireNonNull(rol, "rol");
        this.estado = Estado.ACTIVO;
    }

    public synchronized int registrarFallo() {
        if (estado == Estado.BLOQUEADO) {
            return intentosFallidos;
        }
        intentosFallidos++;
        if (intentosFallidos >= 5) {
            estado = Estado.BLOQUEADO;
        }
        return intentosFallidos;
    }

    public synchronized void reiniciarIntentos() {
        intentosFallidos = 0;
        estado = Estado.ACTIVO;
    }

    public String getId() { return id; }
    public String getUsername() { return username; }
    public String getNombre() { return nombre; }
    public String getPasswordHash() { return passwordHash; }
    public Roles getRol() { return rol; }
    public synchronized int getIntentosFallidos() { return intentosFallidos; }
    public synchronized Estado getEstado() { return estado; }
}
