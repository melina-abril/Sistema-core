package com.empresa.sistemacore.modules.clientes.domain;

public record Cliente(Long id, String number, String name, String document, String email, String phone, String address, boolean active) {
}
