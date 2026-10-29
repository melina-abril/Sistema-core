package com.empresa.sistemacore.modules.autenticacion.api;

import com.empresa.sistemacore.modules.autenticacion.application.AuthDto;
import com.empresa.sistemacore.modules.autenticacion.application.DesbloqueoUsuarioService;
import com.empresa.sistemacore.modules.autenticacion.application.LoginService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final LoginService loginService;
    private final DesbloqueoUsuarioService desbloqueoUsuarioService;

    public AuthController(LoginService loginService, DesbloqueoUsuarioService desbloqueoUsuarioService) {
        this.loginService = loginService;
        this.desbloqueoUsuarioService = desbloqueoUsuarioService;
    }

    @PostMapping("/login")
    public AuthDto.LoginResponse login(@Valid @RequestBody AuthDto.LoginRequest request) {
        return loginService.login(request);
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(@RequestHeader("Authorization") String authorization) {
        loginService.logout(authorization);
    }

    @PostMapping("/usuarios/{id}/desbloqueo")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('SUPERVISOR')")
    public void unlockUser(@PathVariable String id) {
        desbloqueoUsuarioService.desbloquear(id);
    }
}
