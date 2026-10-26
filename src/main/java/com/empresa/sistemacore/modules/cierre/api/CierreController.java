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
@PreAuthorize("hasRole('SUPERVISOR')")
public class CierreController {
    private final EjecutarCierreService ejecutarService;
    private final ReprocesoCierreService reprocesoService;

    public CierreController(EjecutarCierreService ejecutarService, ReprocesoCierreService reprocesoService) {
        this.ejecutarService = ejecutarService;
        this.reprocesoService = reprocesoService;
    }

    @GetMapping
    public CierreDto obtenerCierreDiario() {
        return ejecutarService.obtenerCierreDiario();
    }

    @GetMapping("/historial")
    public List<CierreDto> listarCierres() {
        return ejecutarService.listarCierres();
    }

    @PostMapping("/ejecutar")
    public CierreDto ejecutarCierre() {
        return ejecutarService.ejecutar();
    }

    @PostMapping("/reprocesar")
    public CierreDto reprocesarCierre() {
        return reprocesoService.reprocesar();
    }
}
