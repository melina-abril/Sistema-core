package com.empresa.sistemacore.modules.clientes.domain;

public record Cliente(Long id, String customerNumber, String document, String name, String email, String phone, Status status) {
    public enum Status { ACTIVE, INACTIVE }

    public Cliente withDetails(String document, String name, String email, String phone) {
        return new Cliente(id, customerNumber, document, name, email, phone, status);
    }

    public Cliente deactivate() {
        return new Cliente(id, customerNumber, document, name, email, phone, Status.INACTIVE);
    }
}
