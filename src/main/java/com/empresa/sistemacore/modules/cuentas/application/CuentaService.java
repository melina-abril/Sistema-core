package com.empresa.sistemacore.modules.cuentas.application;

import com.empresa.sistemacore.modules.cuentas.domain.Cuenta;
import com.empresa.sistemacore.modules.cuentas.domain.CuentaRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CuentaService {
    private final CuentaRepository repository;

    public CuentaService(CuentaRepository repository) {
        this.repository = repository;
    }

    public List<CuentaDto.Account> listAccounts(Long customerId) {
        if (customerId == null || customerId <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "customerId debe ser positivo");
        }
        return repository.findByCustomerId(customerId).stream().map(this::toDto).toList();
    }

    public CuentaDto.Detail getAccount(Long id) {
        Cuenta account = findAccount(id);
        // Movimientos recientes dummy; reemplazar por un puerto de consulta de movimientos.
        List<CuentaDto.RecentMovement> movements = id.equals(901L)
                ? List.of(
                    new CuentaDto.RecentMovement(3001L, "CREDITO", new BigDecimal("300.00"), LocalDate.of(2026, 3, 19), "Transferencia recibida", new BigDecimal("1300.00")),
                    new CuentaDto.RecentMovement(3002L, "DEBITO", new BigDecimal("50.00"), LocalDate.of(2026, 3, 20), "Pago de servicio", new BigDecimal("1250.00")))
                : List.of();
        return new CuentaDto.Detail(toDto(account), movements);
    }

    public CuentaDto.Account openAccount(CuentaDto.OpenRequest request) {
        if (request.customerId() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "customerId debe ser positivo");
        }
        // Clientes activos dummy; reemplazar por un puerto del módulo clientes.
        if (!List.of(101L, 102L).contains(request.customerId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Solo se admiten clientes activos");
        }
        if (!List.of("CHECKING", "SAVINGS").contains(request.type()) || !List.of("ARS", "USD").contains(request.currency())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tipo o moneda no admitidos");
        }
        Cuenta account = new Cuenta(903L, request.customerId(), "000-000903", request.type(), request.currency(), BigDecimal.ZERO, Cuenta.Status.OPEN);
        return toDto(repository.save(account));
    }

    public CuentaDto.Account closeAccount(Long id) {
        Cuenta account = findAccount(id);
        try {
            return toDto(repository.save(account.close()));
        } catch (IllegalStateException exception) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, exception.getMessage(), exception);
        }
    }

    private Cuenta findAccount(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cuenta inexistente"));
    }

    private CuentaDto.Account toDto(Cuenta account) {
        return new CuentaDto.Account(account.id(), account.customerId(), account.number(), account.type(), account.currency(), account.balance(), account.status().name());
    }
}
