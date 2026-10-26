package com.empresa.sistemacore.modules.cuentas.api;

import com.empresa.sistemacore.modules.cuentas.application.CuentaDto;
import com.empresa.sistemacore.modules.cuentas.application.CuentaService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cuentas")
public class CuentaController {
    private final CuentaService service;

    public CuentaController(CuentaService service) {
        this.service = service;
    }

    @GetMapping
    public List<CuentaDto.Summary> listarCuentas(@RequestParam Long clienteId) {
        return service.listarCuentas(clienteId);
    }

    @GetMapping("/{id}")
    public CuentaDto.Detail obtenerCuenta(@PathVariable Long id) {
        return service.obtenerCuenta(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CuentaDto.Summary abrirCuenta(@Valid @RequestBody CuentaDto.OpenRequest request) {
        return service.abrirCuenta(request);
    }

    @PatchMapping("/{id}/cerrar")
    @PreAuthorize("hasRole('SUPERVISOR')")
    public CuentaDto.Summary cerrarCuenta(@PathVariable Long id) {
        return service.cerrarCuenta(id);
    }
}