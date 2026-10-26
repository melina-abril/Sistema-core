package com.empresa.sistemacore.modules.cuentas.domain;

import java.util.List;
import java.util.Optional;

public interface CuentaRepository {
    List<Cuenta> findByClienteId(Long clienteId);
    Optional<Cuenta> findById(Long id);
}