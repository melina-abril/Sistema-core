package com.empresa.sistemacore.modules.cuentas.infrastructure;

import com.empresa.sistemacore.modules.cuentas.domain.ClienteActivoPort;
import com.empresa.sistemacore.modules.cuentas.domain.Cuenta;
import com.empresa.sistemacore.modules.cuentas.domain.CuentaRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class CuentaRepositoryAdapter implements CuentaRepository, ClienteActivoPort {
    // Datos dummy; reemplazar por adaptadores de persistencia y consulta al módulo de clientes.
    private static final List<Cuenta> CUENTAS = List.of(
            new Cuenta(1001L, 1L, "000-001", Cuenta.Tipo.CORRIENTE, Cuenta.Moneda.ARS,
                    new BigDecimal("1850.75"), Cuenta.Estado.ACTIVA),
            new Cuenta(1002L, 1L, "000-002", Cuenta.Tipo.AHORRO, Cuenta.Moneda.USD,
                    BigDecimal.ZERO, Cuenta.Estado.ACTIVA));

    @Override
    public List<Cuenta> findByClienteId(Long clienteId) {
        return CUENTAS.stream().filter(cuenta -> cuenta.clienteId().equals(clienteId)).toList();
    }

    @Override
    public Optional<Cuenta> findById(Long id) {
        return CUENTAS.stream().filter(cuenta -> cuenta.id().equals(id)).findFirst();
    }

    @Override
    public boolean isActive(Long clienteId) {
        return Long.valueOf(1L).equals(clienteId) || Long.valueOf(3L).equals(clienteId);
    }
}