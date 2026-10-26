package com.empresa.sistemacore.modules.inicio.api;

import com.empresa.sistemacore.modules.inicio.application.ResumenDiaDto;
import com.empresa.sistemacore.modules.inicio.application.ResumenDiaService;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/inicio")
public class InicioController {

    private final ResumenDiaService resumenDiaService;

    public InicioController(ResumenDiaService resumenDiaService) {
        this.resumenDiaService = resumenDiaService;
    }

    @GetMapping("/resumen")
    @PreAuthorize("hasAnyRole('OPERADOR', 'SUPERVISOR')")
    public ResponseEntity<ResumenDiaDto> obtenerResumenDia(Authentication authentication) {
        return ResponseEntity.ok()
                .cacheControl(CacheControl.noStore())
                .body(resumenDiaService.obtenerResumenDia(authentication));
    }
}
