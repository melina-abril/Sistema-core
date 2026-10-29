package com.empresa.sistemacore.modules.inicio.api;

import com.empresa.sistemacore.modules.inicio.application.ResumenDiaDto;
import com.empresa.sistemacore.modules.inicio.application.ResumenDiaService;
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
    public ResumenDiaDto obtenerResumen() {
        return resumenDiaService.obtenerResumen();
    }
}
