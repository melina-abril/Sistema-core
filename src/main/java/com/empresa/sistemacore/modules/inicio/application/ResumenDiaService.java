package com.empresa.sistemacore.modules.inicio.application;

import com.empresa.sistemacore.modules.inicio.domain.FechaOperativaRepository;
import com.empresa.sistemacore.shared.security.CurrentUser;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

@Service
public class ResumenDiaService {

    private final FechaOperativaRepository fechas;
    private final CurrentUser usuarioActual;

    public ResumenDiaService(FechaOperativaRepository fechas, CurrentUser usuarioActual) {
        this.fechas = fechas;
        this.usuarioActual = usuarioActual;
    }

    @Transactional(readOnly = true)
    public ResumenDiaDto.Respuesta obtener() {
        LocalDate fecha = fechas.vigente().orElseThrow(() ->
                new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                        "No se pudo cargar el resumen del día"));

        // Respuesta dummy hasta disponer de tablas de movimientos y cierres aprobadas.
        long cantidadMovimientos = 0L;
        String estadoCierre = "Pendiente";
        return new ResumenDiaDto.Respuesta(usuarioActual.name(), usuarioActual.role(), fecha,
                cantidadMovimientos, estadoCierre);
    }
}
