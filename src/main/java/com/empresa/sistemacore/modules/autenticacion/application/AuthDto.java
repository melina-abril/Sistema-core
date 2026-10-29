package com.empresa.sistemacore.modules.autenticacion.application;

import com.empresa.sistemacore.shared.security.Roles;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.Instant;

public final class AuthDto {

    private AuthDto() {
    }

    public record LoginRequest(
            @NotBlank @Size(max = 50) String usuario,
            @NotBlank String contrasena) {
    }

    public record LoginResponse(String token, Instant expiraEn, String nombre, Roles rol) {
    }
}
