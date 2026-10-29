package com.empresa.sistemacore.modules.autenticacion.application;

import com.empresa.sistemacore.modules.autenticacion.domain.AuthEvent;
import com.empresa.sistemacore.modules.autenticacion.domain.Usuario;
import com.empresa.sistemacore.modules.autenticacion.domain.UsuarioRepository;
import com.empresa.sistemacore.shared.exception.NotFoundException;
import com.empresa.sistemacore.shared.security.CurrentUser;
import java.time.Clock;
import java.time.Instant;
import java.util.UUID;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DesbloqueoUsuarioService {

    private final UsuarioRepository usuarios;
    private final CurrentUser currentUser;
    private final ApplicationEventPublisher eventos;
    private final Clock clock;

    public DesbloqueoUsuarioService(UsuarioRepository usuarios, CurrentUser currentUser,
                                    ApplicationEventPublisher eventos, Clock clock) {
        this.usuarios = usuarios;
        this.currentUser = currentUser;
        this.eventos = eventos;
        this.clock = clock;
    }

    @Transactional
    public void desbloquear(String id) {
        try {
            UUID.fromString(id);
        } catch (IllegalArgumentException exception) {
            throw new NotFoundException("No existe el usuario solicitado");
        }
        Usuario usuario = usuarios.porIdParaActualizar(id)
                .orElseThrow(() -> new NotFoundException("No existe el usuario solicitado"));
        usuario.desbloquear();
        usuarios.guardar(usuario);
        eventos.publishEvent(new AuthEvent(usuario.getId(), usuario.getUsername(), "DESBLOQUEO",
                currentUser.username(), Instant.now(clock)));
    }
}
