package com.empresa.sistemacore.modules.autenticacion.application;

import com.empresa.sistemacore.modules.autenticacion.domain.AutenticacionEvent;
import com.empresa.sistemacore.modules.autenticacion.domain.Usuario;
import com.empresa.sistemacore.modules.autenticacion.domain.UsuarioRepository;
import com.empresa.sistemacore.shared.security.CurrentUser;
import com.empresa.sistemacore.shared.security.JwtService;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

/** Inicio y cierre de sesión. Los eventos no incluyen contraseñas ni tokens. */
@Service
public class LoginService {

    private final UsuarioRepository usuarios;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final CurrentUser currentUser;
    private final ApplicationEventPublisher eventos;
    private final Map<String, Integer> intentosDesconocidos = new ConcurrentHashMap<>();

    public LoginService(UsuarioRepository usuarios, PasswordEncoder passwordEncoder,
                        JwtService jwtService, CurrentUser currentUser, ApplicationEventPublisher eventos) {
        this.usuarios = usuarios;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.currentUser = currentUser;
        this.eventos = eventos;
    }

    public AuthDto.LoginResponse login(AuthDto.LoginRequest request) {
        if (request == null || request.usuario() == null || request.usuario().isBlank()
                || request.contrasena() == null || request.contrasena().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Completá usuario y contraseña para ingresar");
        }

        String username = request.usuario().trim();
        Usuario usuario = usuarios.porUsername(username).orElse(null);
        if (usuario == null) {
            registrarFalloDesconocido(username);
            throw new AssertionError("El registro de un intento desconocido debe rechazar el acceso");
        }

        synchronized (usuario) {
            if (usuario.getEstado() == Usuario.Estado.BLOQUEADO) {
                throw new LockedException("El usuario está bloqueado");
            }
            if (!passwordEncoder.matches(request.contrasena(), usuario.getPasswordHash())) {
                int intento = usuario.getIntentosFallidos() + 1;
                publicar(username, AutenticacionEvent.Accion.INTENTO_FALLIDO, intento);
                if (intento == 5) {
                    publicar(username, AutenticacionEvent.Accion.BLOQUEO, intento);
                }
                usuario.registrarFallo();
                usuarios.guardar(usuario);
                if (intento >= 5) {
                    throw new LockedException("El usuario está bloqueado");
                }
                throw credencialesInvalidas(intento);
            }

            JwtService.IssuedToken token = jwtService.issue(username, usuario.getNombre(), usuario.getRol());
            publicar(username, AutenticacionEvent.Accion.INGRESO, 0);
            usuario.reiniciarIntentos();
            usuarios.guardar(usuario);
            return new AuthDto.LoginResponse(token.token(), token.expiresAt(), usuario.getNombre(), usuario.getRol());
        }
    }

    public void logout(String authorization) {
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Se requiere iniciar sesión");
        }
        String username = currentUser.username();
        String token = authorization.substring(7).trim();
        jwtService.revoke(token);
        publicar(username, AutenticacionEvent.Accion.CIERRE_SESION, 0);
    }

    private void registrarFalloDesconocido(String username) {
        synchronized (intentosDesconocidos) {
            int anteriores = intentosDesconocidos.getOrDefault(username, 0);
            if (anteriores >= 5) {
                throw new LockedException("El usuario está bloqueado");
            }
            int intento = anteriores + 1;
            publicar(username, AutenticacionEvent.Accion.INTENTO_FALLIDO, intento);
            if (intento == 5) {
                publicar(username, AutenticacionEvent.Accion.BLOQUEO, intento);
            }
            intentosDesconocidos.put(username, intento);
            if (intento == 5) {
                throw new LockedException("El usuario está bloqueado");
            }
            throw credencialesInvalidas(intento);
        }
    }

    private ResponseStatusException credencialesInvalidas(int intento) {
        return new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                "Usuario o contraseña incorrectos. Revisá los datos e intentá nuevamente. Intento fallido "
                        + intento + " de 5");
    }

    private void publicar(String username, AutenticacionEvent.Accion accion, int intentos) {
        eventos.publishEvent(new AutenticacionEvent(username, accion, Instant.now(), intentos));
    }
}
