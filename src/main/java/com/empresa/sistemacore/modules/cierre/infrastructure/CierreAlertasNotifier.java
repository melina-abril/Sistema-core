package com.empresa.sistemacore.modules.cierre.infrastructure;

import java.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CierreAlertasNotifier {
    private static final Logger LOG = LoggerFactory.getLogger(CierreAlertasNotifier.class);

    public void notificarFalla(LocalDate fechaOperativa, String motivo) {
        // Notificación dummy local: reemplazar por el monitoreo operativo.
        LOG.warn("Cierre diario fallido para {}: {}", fechaOperativa, motivo);
    }
}
