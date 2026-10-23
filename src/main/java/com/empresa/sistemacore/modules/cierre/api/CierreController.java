package com.empresa.sistemacore.modules.cierre.api;

import com.empresa.sistemacore.modules.cierre.application.CierreDto;
import com.empresa.sistemacore.modules.cierre.application.EjecutarCierreService;
import com.empresa.sistemacore.modules.cierre.application.ReprocesoCierreService;
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cierre-diario")
public class CierreController {
    private final EjecutarCierreService ejecutarCierre;
    private final ReprocesoCierreService reprocesoCierre;

    public CierreController(EjecutarCierreService ejecutarCierre,
            ReprocesoCierreService reprocesoCierre) {
        this.ejecutarCierre = ejecutarCierre;
        this.reprocesoCierre = reprocesoCierre;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('OPERADOR', 'SUPERVISOR')")
    public CierreDto getDailyClose() {
        return ejecutarCierre.consultar();
    }

    @GetMapping("/historial")
    @PreAuthorize("hasAnyRole('OPERADOR', 'SUPERVISOR')")
    public List<CierreDto> listDailyCloses() {
        return ejecutarCierre.historial();
    }

    @PostMapping
    @PreAuthorize("hasRole('SUPERVISOR')")
    public CierreDto executeDailyClose() {
        return ejecutarCierre.ejecutar();
    }

    @PostMapping("/reproceso")
    @PreAuthorize("hasRole('SUPERVISOR')")
    public CierreDto retryDailyClose() {
        return reprocesoCierre.reprocesar();
    }
}
