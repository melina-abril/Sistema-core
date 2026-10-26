package com.empresa.sistemacore.modules.auditoria.api;

import com.empresa.sistemacore.modules.auditoria.application.AuditoriaDto;
import com.empresa.sistemacore.modules.auditoria.application.AuditoriaService;
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auditoria")
public class AuditoriaController {
    private final AuditoriaService service;

    public AuditoriaController(AuditoriaService service) {
        this.service = service;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('OPERADOR', 'SUPERVISOR')")
    public List<AuditoriaDto> consultarAuditoria() {
        return service.consultar();
    }
}
