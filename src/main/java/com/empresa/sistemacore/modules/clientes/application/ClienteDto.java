package com.empresa.sistemacore.modules.clientes.application;

import com.empresa.sistemacore.modules.clientes.domain.Cliente;
import jakarta.validation.constraints.NotBlank;
import java.time.OffsetDateTime;

public final class ClienteDto {
    private ClienteDto() {
    }

    public record Request(@NotBlank String name, @NotBlank String document, String email, String phone, String address) {
    }

    public record Audit(String action, String user, OffsetDateTime occurredAt) {
    }

    public record Response(Long id, String number, String name, String document, String email,
                           String phone, String address, String status, Audit audit) {
        public static Response from(Cliente client, Audit audit) {
            return new Response(client.id(), client.number(), client.name(), client.document(),
                    client.email(), client.phone(), client.address(),
                    client.active() ? "ACTIVE" : "INACTIVE", audit);
        }
    }
}
