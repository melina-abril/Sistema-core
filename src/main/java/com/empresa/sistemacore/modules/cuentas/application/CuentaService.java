package com.empresa.sistemacore.modules.cuentas.application;

import com.empresa.sistemacore.modules.cuentas.domain.ClienteActivoPort;
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
    private final ClienteActivoPort clienteActivoPort;

    public CuentaService(CuentaRepository repository, ClienteActivoPort clienteActivoPort) {
        this.repository = repository;
        this.clienteActivoPort = clienteActivoPort;
    }

    public List<CuentaDto.Summary> listarCuentas(Long clienteId) {
        return repository.findByClienteId(clienteId).stream().map(CuentaDto.Summary::from).toList();
    }

    public CuentaDto.Detail obtenerCuenta(Long id) {
        Cuenta cuenta = find(id);
        // Movimientos dummy; reemplazar al integrar el módulo de movimientos.
        List<CuentaDto.RecentMovement> movements = cuenta.id() == 1001L
                ? List.of(
                    new CuentaDto.RecentMovement(5002L, LocalDate.of(2025, 6, 12), "DEBITO", "Pago de servicio", new BigDecimal("50.00"), new BigDecimal("1850.75")),
                    new CuentaDto.RecentMovement(5001L, LocalDate.of(2025, 6, 11), "CREDITO", "Depósito", new BigDecimal("300.00"), new BigDecimal("1900.75")))
                : List.of();
        return CuentaDto.Detail.from(cuenta, movements);
    }

    public CuentaDto.Summary abrirCuenta(CuentaDto.OpenRequest request) {
        if (!clienteActivoPort.isActive(request.clienteId())) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Solo se pueden abrir cuentas para clientes activos");
        }
        // Respuesta dummy; reemplazar por la apertura persistida.
        return CuentaDto.Summary.from(new Cuenta(1003L, request.clienteId(), "000-003", request.tipo(),
                request.moneda(), BigDecimal.ZERO, Cuenta.Estado.ACTIVA));
    }

    public CuentaDto.Summary cerrarCuenta(Long id) {
        Cuenta cuenta = find(id);
        if (cuenta.estado() != Cuenta.Estado.ACTIVA || cuenta.saldo().compareTo(BigDecimal.ZERO) != 0) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "La cuenta debe estar activa y tener saldo cero para cerrarse");
        }
        // Respuesta dummy; reemplazar por el cierre persistido.
        return CuentaDto.Summary.from(cuenta.cerrar());
    }

    private Cuenta find(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cuenta no encontrada"));
    }
}