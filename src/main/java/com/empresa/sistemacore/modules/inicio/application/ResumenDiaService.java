package com.empresa.sistemacore.modules.inicio.application;

import com.empresa.sistemacore.shared.security.CurrentUser;
import java.time.LocalDate;
import org.springframework.stereotype.Service;

@Service
public class ResumenDiaService {

    private final CurrentUser usuarioActual;

    public ResumenDiaService(CurrentUser usuarioActual) {
        this.usuarioActual = usuarioActual;
    }

    public ResumenDiaDto obtenerResumen() {
        // Respuesta dummy: reemplazar la fecha, los movimientos y el cierre al conectar sus fuentes operativas.
        return new ResumenDiaDto(
                usuarioActual.name(),
                usuarioActual.role(),
                LocalDate.now(),
                12L,
                ResumenDiaDto.EstadoCierre.PENDIENTE);
    }
}
