package com.empresa.sistemacore.shared.security;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class CurrentUser {

    private JwtService.TokenData authenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()
                || !(authentication.getPrincipal() instanceof JwtService.TokenData tokenData)) {
            throw new AuthenticationCredentialsNotFoundException("Se requiere iniciar sesión");
        }
        return tokenData;
    }

    public String username() {
        return authenticatedUser().username();
    }

    public String name() {
        return authenticatedUser().name();
    }

    public Roles role() {
        return authenticatedUser().role();
    }
}
