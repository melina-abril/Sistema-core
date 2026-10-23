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
    @PreAuthorize("hasAnyRole('OPERADOR', 'SUPERVISOR')")
    public List<CuentaDto.Account> listAccounts(@RequestParam Long customerId) {
        return service.listAccounts(customerId);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('OPERADOR', 'SUPERVISOR')")
    public CuentaDto.Detail getAccount(@PathVariable Long id) {
        return service.getAccount(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('OPERADOR', 'SUPERVISOR')")
    public CuentaDto.Account openAccount(@Valid @RequestBody CuentaDto.OpenRequest request) {
        return service.openAccount(request);
    }

    @PatchMapping("/{id}/close")
    @PreAuthorize("hasRole('SUPERVISOR')")
    public CuentaDto.Account closeAccount(@PathVariable Long id) {
        return service.closeAccount(id);
    }
}
