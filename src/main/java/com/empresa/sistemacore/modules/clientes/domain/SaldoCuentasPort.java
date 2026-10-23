package com.empresa.sistemacore.modules.clientes.domain;

public interface SaldoCuentasPort {
    boolean hasNonZeroBalance(Long clientId);
}
