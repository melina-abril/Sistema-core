package com.empresa.sistemacore.modules.cuentas.domain;

public interface ClienteActivoPort {
    boolean isActive(Long clienteId);
}