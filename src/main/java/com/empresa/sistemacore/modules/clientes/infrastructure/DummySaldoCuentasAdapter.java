package com.empresa.sistemacore.modules.clientes.infrastructure;

import com.empresa.sistemacore.modules.clientes.domain.SaldoCuentasPort;
import org.springframework.stereotype.Component;

@Component
public class DummySaldoCuentasAdapter implements SaldoCuentasPort {
    @Override
    public boolean hasNonZeroBalance(Long customerId) {
        // Saldo dummy: reemplazar por un adaptador al módulo de cuentas.
        return Long.valueOf(101L).equals(customerId);
    }
}
