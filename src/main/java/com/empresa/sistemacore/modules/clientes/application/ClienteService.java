package com.empresa.sistemacore.modules.clientes.application;

import com.empresa.sistemacore.modules.clientes.domain.Cliente;
import com.empresa.sistemacore.modules.clientes.domain.ClienteRepository;
import com.empresa.sistemacore.modules.clientes.domain.SaldoCuentasPort;
import java.time.Instant;
import java.util.List;
import java.util.Locale;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ClienteService {
    private final ClienteRepository repository;
    private final SaldoCuentasPort balances;
    private final ApplicationEventPublisher events;

    public ClienteService(ClienteRepository repository, SaldoCuentasPort balances, ApplicationEventPublisher events) {
        this.repository = repository;
        this.balances = balances;
        this.events = events;
    }

    public List<ClienteDto.ClientResponse> buscarClientes(String search) {
        String term = search == null ? "" : search.trim().toLowerCase(Locale.ROOT);
        return repository.findAll().stream()
                .filter(client -> client.name().toLowerCase(Locale.ROOT).contains(term)
                        || client.document().toLowerCase(Locale.ROOT).contains(term)
                        || client.customerNumber().toLowerCase(Locale.ROOT).contains(term))
                .map(ClienteDto.ClientResponse::from).toList();
    }

    public ClienteDto.ClientResponse obtenerCliente(Long id) {
        return ClienteDto.ClientResponse.from(requireClient(id));
    }

    public ClienteDto.ClientResponse crearCliente(ClienteDto.ClientRequest request) {
        validateDocument(request.document(), null);
        // Respuesta dummy: reemplazar por persistencia y auditoría transaccional.
        Cliente created = repository.save(new Cliente(103L, "CLI-00103", request.document().trim(),
                request.name().trim(), request.email(), request.phone(), Cliente.Status.ACTIVE));
        publish("CREATE", null, created);
        return ClienteDto.ClientResponse.from(created);
    }

    public ClienteDto.ClientResponse actualizarCliente(Long id, ClienteDto.ClientRequest request) {
        Cliente previous = requireClient(id);
        if (previous.status() != Cliente.Status.ACTIVE) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "No se puede modificar un cliente inactivo");
        }
        validateDocument(request.document(), id);
        // Respuesta dummy: reemplazar por persistencia y auditoría transaccional.
        Cliente updated = repository.save(previous.withDetails(request.document().trim(), request.name().trim(),
                request.email(), request.phone()));
        publish("UPDATE", previous, updated);
        return ClienteDto.ClientResponse.from(updated);
    }

    public ClienteDto.ClientResponse darDeBajaCliente(Long id) {
        Cliente previous = requireClient(id);
        if (previous.status() == Cliente.Status.INACTIVE) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El cliente ya está inactivo");
        }
        if (balances.hasNonZeroBalance(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "BR-02: el cliente tiene cuentas con saldo distinto de cero");
        }
        // Respuesta dummy: reemplazar por persistencia y auditoría transaccional.
        Cliente updated = repository.save(previous.deactivate());
        publish("DEACTIVATE", previous, updated);
        return ClienteDto.ClientResponse.from(updated);
    }

    private Cliente requireClient(Long id) {
        return repository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado"));
    }

    private void validateDocument(String document, Long excludingId) {
        boolean duplicate = repository.findAll().stream().anyMatch(client ->
                client.document().equalsIgnoreCase(document.trim()) && !client.id().equals(excludingId));
        if (duplicate) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El documento ya está registrado");
        }
    }

    private void publish(String action, Cliente previous, Cliente current) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication == null ? "usuario-demo" : authentication.getName();
        events.publishEvent(new ClienteChangedEvent(action, previous, current, username, Instant.now()));
    }
}
