package com.empresa.sistemacore.modules.clientes.infrastructure;

import com.empresa.sistemacore.modules.clientes.domain.SaldoCuentasPort;
import org.springframework.stereotype.Component;

@Component
public class SaldoCuentasAdapter implements SaldoCuentasPort {
    @Override
    public boolean hasNonZeroBalance(Long clientId) {
        // Saldo dummy: el cliente 1001 tiene saldo; reemplazar por un puerto provisto por cuentas.
        return Long.valueOf(1001L).equals(clientId);
    }
}
