package com.empresa.sistemacore.shared.config;

import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {
    @GetMapping("/api/health")
    public Map<String, String> health() {
        // Respuesta dummy; reemplazar por una comprobación operativa cuando corresponda.
        return Map.of("status", "UP", "time", "2025-01-15T09:00:00Z");
    }
}
