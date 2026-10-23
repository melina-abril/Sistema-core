package com.empresa.sistemacore.modules.cuentas.infrastructure;

import com.empresa.sistemacore.modules.cuentas.domain.Cuenta;
import com.empresa.sistemacore.modules.cuentas.domain.CuentaRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class CuentaRepositoryAdapter implements CuentaRepository {
    // Respuestas dummy fijas; reemplazar por persistencia mediante CuentaJpaRepository.
    private static final List<Cuenta> SAMPLE_ACCOUNTS = List.of(
            new Cuenta(901L, 101L, "000-000901", "CHECKING", "ARS", new BigDecimal("1250.00"), Cuenta.Status.OPEN),
            new Cuenta(902L, 101L, "000-000902", "SAVINGS", "USD", new BigDecimal("0.00"), Cuenta.Status.OPEN),
            new Cuenta(903L, 102L, "000-000903", "SAVINGS", "ARS", new BigDecimal("0.00"), Cuenta.Status.OPEN));

    @Override
    public List<Cuenta> findByCustomerId(Long customerId) {
        return SAMPLE_ACCOUNTS.stream().filter(account -> account.customerId().equals(customerId)).toList();
    }

    @Override
    public Optional<Cuenta> findById(Long id) {
        return SAMPLE_ACCOUNTS.stream().filter(account -> account.id().equals(id)).findFirst();
    }

    @Override
    public Cuenta save(Cuenta account) {
        // Respuesta dummy: no persiste ni altera los datos de ejemplo.
        return account;
    }
}
