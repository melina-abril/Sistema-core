package com.empresa.sistemacore.modules.inicio.application;

import java.time.LocalDate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ResumenDiaService {

    public ResumenDiaDto obtenerResumenDia(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AuthenticationCredentialsNotFoundException("Se requiere una sesión autenticada");
        }

        String rol = authentication.getAuthorities().stream()
                .anyMatch(authority -> "ROLE_SUPERVISOR".equals(authority.getAuthority()))
                ? "SUPERVISOR" : "OPERADOR";

        // Respuesta dummy: reemplazar fecha, cantidad y estado por datos operativos vigentes.
        return new ResumenDiaDto(
                authentication.getName(),
                rol,
                LocalDate.of(2026, 2, 25),
                12,
                "Pendiente"
        );
    }
}
