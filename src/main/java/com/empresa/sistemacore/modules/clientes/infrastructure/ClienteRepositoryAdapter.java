package com.empresa.sistemacore.modules.clientes.infrastructure;

import com.empresa.sistemacore.modules.clientes.domain.Cliente;
import com.empresa.sistemacore.modules.clientes.domain.ClienteRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class ClienteRepositoryAdapter implements ClienteRepository {
    // Datos dummy fijos; reemplazar por persistencia Oracle/JPA.
    private static final List<Cliente> CLIENTS = List.of(
            new Cliente(1001L, "CLI-001001", "Ana García", "30123456", "ana.garcia@example.com",
                    "+54 11 5555 0101", "Av. Corrientes 1200, Buenos Aires", true),
            new Cliente(1002L, "CLI-001002", "Luis Fernández", "28456789", "luis.fernandez@example.com",
                    "+54 11 5555 0102", "San Martín 450, Buenos Aires", true));

    @Override
    public List<Cliente> findAll() {
        return CLIENTS;
    }

    @Override
    public Optional<Cliente> findById(Long id) {
        return CLIENTS.stream().filter(client -> client.id().equals(id)).findFirst();
    }
}
