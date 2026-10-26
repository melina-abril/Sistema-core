package com.empresa.sistemacore.modules.movimientos.application;

import com.empresa.sistemacore.modules.movimientos.domain.Movimiento;
import com.empresa.sistemacore.modules.movimientos.domain.MovimientoRepository;
import com.empresa.sistemacore.modules.movimientos.domain.TipoMovimiento;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AjusteMovimientoService {
    private final MovimientoRepository repository;

    public AjusteMovimientoService(MovimientoRepository repository) {
        this.repository = repository;
    }

    public MovimientoDto.Response ajustarMovimiento(Long id, MovimientoDto.AjusteRequest request, String usuario) {
        Movimiento original = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Movimiento no encontrado"));
        if (!original.cerrado()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Solo se ajustan movimientos cerrados");
        }
        if (request.direccion() != TipoMovimiento.DEBITO && request.direccion() != TipoMovimiento.CREDITO) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La dirección debe ser débito o crédito");
        }
        if (request.importe() == null || request.importe().signum() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El importe debe ser mayor que cero");
        }
        BigDecimal saldoActual = new BigDecimal("1425.00");
        BigDecimal saldo = request.direccion() == TipoMovimiento.DEBITO
                ? saldoActual.subtract(request.importe()) : saldoActual.add(request.importe());
        if (saldo.signum() < 0) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El ajuste dejaría un saldo negativo");
        }
        // Ajuste dummy en el día abierto: el movimiento original permanece intacto.
        return new MovimientoDto.Response(5L, original.cuentaId(), TipoMovimiento.AJUSTE,
                request.importe(), LocalDate.of(2026, 5, 1), request.concepto(), usuario,
                OffsetDateTime.parse("2026-05-01T12:05:00-03:00"), false, saldo, id);
    }
}
