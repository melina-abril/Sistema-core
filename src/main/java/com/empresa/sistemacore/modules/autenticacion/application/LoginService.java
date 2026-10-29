package com.empresa.sistemacore.modules.autenticacion.application;

import com.empresa.sistemacore.modules.autenticacion.domain.AuthEvent;
import com.empresa.sistemacore.modules.autenticacion.domain.Usuario;
import com.empresa.sistemacore.modules.autenticacion.domain.UsuarioRepository;
import com.empresa.sistemacore.shared.security.CurrentUser;
import com.empresa.sistemacore.shared.security.JwtService;
import java.time.Clock;
import java.time.Instant;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LoginService {

    private final UsuarioRepository usuarios;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final CurrentUser currentUser;
    private final ApplicationEventPublisher eventos;
    private final Clock clock;

    public LoginService(UsuarioRepository usuarios, PasswordEncoder passwordEncoder, JwtService jwtService,
                        CurrentUser currentUser, ApplicationEventPublisher eventos, Clock clock) {
        this.usuarios = usuarios;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.currentUser = currentUser;
        this.eventos = eventos;
        this.clock = clock;
    }

    // Los errores de acceso no revierten el contador ni el evento de auditoría ya registrados.
    @Transactional(noRollbackFor = {BadCredentialsException.class, LockedException.class})
    public AuthDto.LoginResponse login(AuthDto.LoginRequest request) {
        if (request == null || request.usuario() == null || request.usuario().isBlank()
                || request.contrasena() == null || request.contrasena().isBlank()) {
            throw new IllegalArgumentException("Completa usuario y contraseña para ingresar");
        }

        String username = request.usuario().trim();
        Usuario usuario = usuarios.porUsernameParaActualizar(username).orElse(null);
        if (usuario == null) {
            publicar(username, username, "INTENTO_FALLIDO", username);
            throw new BadCredentialsException("Usuario o contraseña inválidos");
        }
        if (usuario.estaBloqueado()) {
            publicar(usuario.getId(), username, "INGRESO_BLOQUEADO", username);
            throw new LockedException("El usuario está bloqueado");
        }
        if (!passwordEncoder.matches(request.contrasena(), usuario.getPasswordHash())) {
            usuario.registrarIntentoFallido(Instant.now(clock));
            usuarios.guardar(usuario);
            publicar(usuario.getId(), username,
                    usuario.estaBloqueado() ? "BLOQUEO" : "INTENTO_FALLIDO", username);
            if (usuario.estaBloqueado()) {
                throw new LockedException("El usuario está bloqueado");
            }
            throw new BadCredentialsException("Usuario o contraseña inválidos");
        }

        usuario.registrarIngreso();
        usuarios.guardar(usuario);
        publicar(usuario.getId(), username, "INGRESO", username);
        JwtService.IssuedToken token = jwtService.issue(username, usuario.getNombre(), usuario.getRol());
        return new AuthDto.LoginResponse(token.token(), token.expiresAt(), usuario.getNombre(), usuario.getRol());
    }

    @Transactional
    public void logout(String authorization) {
        if (authorization == null || !authorization.startsWith("Bearer ")
                || authorization.substring(7).isBlank()) {
            throw new BadCredentialsException("Se requiere un token válido");
        }
        String token = authorization.substring(7).trim();
        String username = currentUser.username();
        publicar(username, username, "CIERRE_SESION", username);
        jwtService.revoke(token);
    }

    private void publicar(String entityId, String username, String action, String actor) {
        eventos.publishEvent(new AuthEvent(entityId, username, action, actor, Instant.now(clock)));
    }
}
