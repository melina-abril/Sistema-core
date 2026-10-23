package com.empresa.sistemacore.modules.cierre.application;

import com.empresa.sistemacore.modules.cierre.domain.CierreRepository;
import com.empresa.sistemacore.modules.cierre.domain.EstadoCierre;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ReprocesoCierreService {
    private final CierreRepository repository;
    private final EjecutarCierreService ejecutarCierre;

    public ReprocesoCierreService(CierreRepository repository, EjecutarCierreService ejecutarCierre) {
        this.repository = repository;
        this.ejecutarCierre = ejecutarCierre;
    }

    public synchronized CierreDto reprocesar() {
        var fecha = repository.fechaOperativaVigente();
        var fallido = repository.findByDate(fecha)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.CONFLICT,
                        "No hay un cierre fallido para reprocesar"));
        if (fallido.estado() != EstadoCierre.CON_ERROR) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Solo se puede reprocesar un cierre fallido de la fecha abierta");
        }
        return ejecutarCierre.procesar(fallido);
    }
}
