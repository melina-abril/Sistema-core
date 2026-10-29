package com.empresa.sistemacore.modules.autenticacion.infrastructure;

import com.empresa.sistemacore.modules.autenticacion.domain.AuthEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/** Inserta eventos de acceso en el log compartido dentro de la transacción que los originó. */
@Component
public class AuthAuditListener {

    private final AuthAuditJpaRepository repository;

    public AuthAuditListener(AuthAuditJpaRepository repository) {
        this.repository = repository;
    }

    @EventListener
    public void registrar(AuthEvent event) {
        repository.saveAndFlush(new AuthAuditJpaEntity(event));
    }
}
