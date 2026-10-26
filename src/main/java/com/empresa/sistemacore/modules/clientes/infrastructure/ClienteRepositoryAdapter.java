package com.empresa.sistemacore.modules.clientes.infrastructure;

import com.empresa.sistemacore.modules.clientes.domain.Cliente;
import com.empresa.sistemacore.modules.clientes.domain.ClienteRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class ClienteRepositoryAdapter implements ClienteRepository {
    // Datos dummy fijos: reemplazar este adaptador por persistencia Oracle/JPA.
    private static final List<Cliente> EXAMPLES = List.of(
            new Cliente(101L, "CLI-00101", "30123456", "María González", "maria.gonzalez@example.com", "+54 11 5555 0101", Cliente.Status.ACTIVE),
            new Cliente(102L, "CLI-00102", "28987654", "Carlos Pérez", "carlos.perez@example.com", "+54 11 5555 0102", Cliente.Status.ACTIVE)
    );

    @Override
    public List<Cliente> findAll() {
        return EXAMPLES;
    }

    @Override
    public Optional<Cliente> findById(Long id) {
        return EXAMPLES.stream().filter(client -> client.id().equals(id)).findFirst();
    }

    @Override
    public Cliente save(Cliente cliente) {
        // Respuesta dummy: no se escribe en base de datos.
        return cliente;
    }
}
