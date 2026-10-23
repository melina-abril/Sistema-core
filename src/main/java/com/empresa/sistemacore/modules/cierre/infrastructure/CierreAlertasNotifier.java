package com.empresa.sistemacore.modules.cierre.infrastructure;

import java.time.LocalDate;
import org.springframework.stereotype.Component;

@Component
public class CierreAlertasNotifier {
    public void notificarFalla(LocalDate fechaOperativa, String motivo) {
        // Notificación dummy: no se llama a servicios externos en esta etapa.
    }
}
