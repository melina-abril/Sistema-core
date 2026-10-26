package com.empresa.sistemacore.modules.cierre.application;

import org.springframework.stereotype.Service;

@Service
public class ReprocesoCierreService {
    private final EjecutarCierreService ejecutarCierreService;

    public ReprocesoCierreService(EjecutarCierreService ejecutarCierreService) {
        this.ejecutarCierreService = ejecutarCierreService;
    }

    public CierreDto reprocesar() {
        return ejecutarCierreService.reprocesar();
    }
}
