package com.empresa.sistemacore.modules.clientes.api;

import com.empresa.sistemacore.modules.clientes.application.ClienteDto;
import com.empresa.sistemacore.modules.clientes.application.ClienteService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public List<ClienteDto.ClientResponse> buscarClientes(@RequestParam(required = false) String search) {
        return service.buscarClientes(search);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('OPERADOR', 'SUPERVISOR')")
    public ClienteDto.ClientResponse obtenerCliente(@PathVariable Long id) {
        return service.obtenerCliente(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('OPERADOR', 'SUPERVISOR')")
    public ClienteDto.ClientResponse crearCliente(@Valid @RequestBody ClienteDto.ClientRequest request) {
        return service.crearCliente(request);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('OPERADOR', 'SUPERVISOR')")
    public ClienteDto.ClientResponse actualizarCliente(@PathVariable Long id,
            @Valid @RequestBody ClienteDto.ClientRequest request) {
        return service.actualizarCliente(id, request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SUPERVISOR')")
    public ClienteDto.ClientResponse darDeBajaCliente(@PathVariable Long id) {
        return service.darDeBajaCliente(id);
    }
}
