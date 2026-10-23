package com.empresa.sistemacore.modules.inicio.api;

import com.empresa.sistemacore.modules.inicio.application.ResumenDiaDto;
import com.empresa.sistemacore.modules.inicio.application.ResumenDiaService;
import java.util.Set;
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

    private final ResumenDiaService summaryService;

    public InicioController(ResumenDiaService summaryService) {
        this.summaryService = summaryService;
    }

    @GetMapping("/resumen")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ResumenDiaDto> getDailySummary(Authentication authentication) {
        Set<String> authorities = authentication.getAuthorities().stream()
                .map(authority -> authority.getAuthority())
                .collect(java.util.stream.Collectors.toSet());
        String role = authorities.contains("ROLE_SUPERVISOR") ? "SUPERVISOR"
                : authorities.contains("ROLE_OPERADOR") ? "OPERADOR" : "INTEGRACION";
        return ResponseEntity.ok()
                .cacheControl(CacheControl.noStore())
                .body(summaryService.getDailySummary(authentication.getName(), role));
    }
}
