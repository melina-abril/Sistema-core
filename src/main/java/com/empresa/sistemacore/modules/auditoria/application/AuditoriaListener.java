package com.empresa.sistemacore.modules.auditoria.application;

import com.empresa.sistemacore.modules.auditoria.domain.RegistroAuditoria;
import java.time.LocalDateTime;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class AuditoriaListener {
    private final AuditoriaService service;

    public AuditoriaListener(AuditoriaService service) {
        this.service = service;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    public void onChange(AuditChangeEvent event) {
        service.registrar(new RegistroAuditoria(null, event.entidad(), event.accion(),
                event.valoresAnterior(), event.valoresNuevo(), event.usuario(), event.fechaHora()));
    }

    // Contrato de evento para que otros módulos publiquen cambios sin acceder a la tabla de auditoría.
    public record AuditChangeEvent(
            String entidad,
            String accion,
            String valoresAnterior,
            String valoresNuevo,
            String usuario,
            LocalDateTime fechaHora
    ) {
    }
}
