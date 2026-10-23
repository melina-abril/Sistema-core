package com.empresa.sistemacore.modules.cuentas.domain;

import java.util.List;
import java.util.Optional;

public interface CuentaRepository {
    List<Cuenta> findByCustomerId(Long customerId);
    Optional<Cuenta> findById(Long id);
    Cuenta save(Cuenta account);
}
