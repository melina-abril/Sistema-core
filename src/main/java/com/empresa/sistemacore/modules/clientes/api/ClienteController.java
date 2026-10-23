package com.empresa.sistemacore.modules.clientes.api;

import com.empresa.sistemacore.modules.clientes.application.ClienteDto;
import com.empresa.sistemacore.modules.clientes.application.ClienteService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {
    private final ClienteService service;

    public ClienteController(ClienteService service) {
        this.service = service;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('OPERADOR', 'SUPERVISOR')")
    public List<ClienteDto.Response> searchClients(@RequestParam(required = false) String name,
                                                    @RequestParam(required = false) String document,
                                                    @RequestParam(required = false) String number) {
        return service.searchClients(name, document, number);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('OPERADOR', 'SUPERVISOR')")
    public ClienteDto.Response getClient(@PathVariable Long id) {
        return service.getClient(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('OPERADOR', 'SUPERVISOR')")
    public ClienteDto.Response createClient(@Valid @RequestBody ClienteDto.Request request, Authentication authentication) {
        return service.createClient(request, authentication.getName());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('OPERADOR', 'SUPERVISOR')")
    public ClienteDto.Response updateClient(@PathVariable Long id, @Valid @RequestBody ClienteDto.Request request,
                                            Authentication authentication) {
        return service.updateClient(id, request, authentication.getName());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SUPERVISOR')")
    public ClienteDto.Response deactivateClient(@PathVariable Long id, Authentication authentication) {
        return service.deactivateClient(id, authentication.getName());
    }
}
