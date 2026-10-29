package com.empresa.sistemacore.modules.autenticacion.application;

import com.empresa.sistemacore.shared.security.Roles;
import java.time.Instant;

/** Contratos HTTP del inicio de sesión. */
public final class AuthDto {

    private AuthDto() {
    }

    public record LoginRequest(String usuario, String contrasena) {
    }

    public record LoginResponse(String token, Instant expiraEn, String nombre, Roles rol) {
    }
}
