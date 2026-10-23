package com.empresa.sistemacore.modules.clientes.application;

import com.empresa.sistemacore.modules.clientes.domain.Cliente;
import com.empresa.sistemacore.modules.clientes.domain.ClienteRepository;
import com.empresa.sistemacore.modules.clientes.domain.SaldoCuentasPort;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Locale;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
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

    public List<ClienteDto.Response> searchClients(String name, String document, String number) {
        return repository.findAll().stream()
                .filter(client -> matches(client.name(), name))
                .filter(client -> matches(client.document(), document))
                .filter(client -> matches(client.number(), number))
                .map(client -> ClienteDto.Response.from(client, null))
                .toList();
    }

    public ClienteDto.Response getClient(Long id) {
        return ClienteDto.Response.from(requireClient(id), null);
    }

    public ClienteDto.Response createClient(ClienteDto.Request request, String user) {
        validateDocument(request.document(), null);
        // Respuesta dummy: no se persiste el alta; reemplazar por el caso de uso transaccional.
        Cliente client = new Cliente(1003L, "CLI-001003", request.name().trim(), request.document().trim(),
                request.email(), request.phone(), request.address(), true);
        return changed(client, null, "CREATE", user);
    }

    public ClienteDto.Response updateClient(Long id, ClienteDto.Request request, String user) {
        Cliente previous = requireClient(id);
        if (!previous.active()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Inactive clients cannot be modified");
        }
        validateDocument(request.document(), id);
        // Respuesta dummy: no se persiste la modificación; reemplazar por el caso de uso transaccional.
        Cliente updated = new Cliente(id, previous.number(), request.name().trim(), request.document().trim(),
                request.email(), request.phone(), request.address(), true);
        return changed(updated, previous, "UPDATE", user);
    }

    public ClienteDto.Response deactivateClient(Long id, String user) {
        Cliente previous = requireClient(id);
        if (!previous.active()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Client is already inactive");
        }
        if (balances.hasNonZeroBalance(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "BR-02: Client has accounts with a non-zero balance");
        }
        // Respuesta dummy: no se persiste la baja; reemplazar por el caso de uso transaccional.
        Cliente inactive = new Cliente(id, previous.number(), previous.name(), previous.document(),
                previous.email(), previous.phone(), previous.address(), false);
        return changed(inactive, previous, "DEACTIVATE", user);
    }

    private ClienteDto.Response changed(Cliente client, Cliente previous, String action, String user) {
        OffsetDateTime occurredAt = OffsetDateTime.now();
        // Evento dummy para auditoría: sustituir por una escritura append-only transaccional.
        events.publishEvent(new ClienteChangedEvent(client.id(), action,
                previous == null ? null : previous.toString(), client.toString(), user, occurredAt));
        return ClienteDto.Response.from(client, new ClienteDto.Audit(action, user, occurredAt));
    }

    private Cliente requireClient(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found"));
    }

    private void validateDocument(String document, Long excludedId) {
        if (repository.findAll().stream().anyMatch(client -> client.document().equalsIgnoreCase(document.trim())
                && !client.id().equals(excludedId))) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Document already exists");
        }
    }

    private boolean matches(String value, String filter) {
        return filter == null || filter.isBlank()
                || value.toLowerCase(Locale.ROOT).contains(filter.trim().toLowerCase(Locale.ROOT));
    }
}
