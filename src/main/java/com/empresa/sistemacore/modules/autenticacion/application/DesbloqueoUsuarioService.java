package com.empresa.sistemacore.modules.autenticacion.application;

import com.empresa.sistemacore.modules.autenticacion.domain.AutenticacionEvent;
import com.empresa.sistemacore.modules.autenticacion.domain.Usuario;
import com.empresa.sistemacore.modules.autenticacion.domain.UsuarioRepository;
import com.empresa.sistemacore.shared.security.CurrentUser;
import com.empresa.sistemacore.shared.security.Roles;
import java.time.Instant;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@Service
public class DesbloqueoUsuarioService {

    private final UsuarioRepository usuarios;
    private final CurrentUser currentUser;
    private final ApplicationEventPublisher eventos;

    public DesbloqueoUsuarioService(UsuarioRepository usuarios, CurrentUser currentUser,
                                    ApplicationEventPublisher eventos) {
        this.usuarios = usuarios;
        this.currentUser = currentUser;
        this.eventos = eventos;
    }

    public void unlockUser(String id) {
        if (currentUser.role() != Roles.SUPERVISOR) {
            throw new AccessDeniedException("Solo un supervisor puede desbloquear usuarios");
        }
        Usuario usuario = usuarios.porId(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe el usuario " + id));
        synchronized (usuario) {
            eventos.publishEvent(new AutenticacionEvent(usuario.getUsername(),
                    AutenticacionEvent.Accion.DESBLOQUEO, Instant.now(), 0));
            usuario.reiniciarIntentos();
            usuarios.guardar(usuario);
        }
    }
}
