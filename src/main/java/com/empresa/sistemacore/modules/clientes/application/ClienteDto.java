package com.empresa.sistemacore.modules.clientes.application;

import com.empresa.sistemacore.modules.clientes.domain.Cliente;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public final class ClienteDto {
    private ClienteDto() { }

    public record ClientRequest(@NotBlank String document, @NotBlank String name, @Email String email, String phone) { }

    public record ClientResponse(Long id, String customerNumber, String document, String name,
                                 String email, String phone, Cliente.Status status) {
        public static ClientResponse from(Cliente cliente) {
            return new ClientResponse(cliente.id(), cliente.customerNumber(), cliente.document(),
                    cliente.name(), cliente.email(), cliente.phone(), cliente.status());
        }
    }
}
