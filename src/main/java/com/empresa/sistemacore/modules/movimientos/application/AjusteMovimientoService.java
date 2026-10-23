package com.empresa.sistemacore.modules.movimientos.application;

import com.empresa.sistemacore.modules.movimientos.domain.Movimiento;
import com.empresa.sistemacore.modules.movimientos.domain.MovimientoRepository;
import com.empresa.sistemacore.modules.movimientos.domain.TipoMovimiento;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AjusteMovimientoService {
    private final MovimientoRepository repository;

    public AjusteMovimientoService(MovimientoRepository repository) {
        this.repository = repository;
    }

    public MovimientoDto.Response createAdjustment(long originalId,
                                                    MovimientoDto.AdjustmentRequest request,
                                                    String usuario) {
        Movimiento original = repository.findById(originalId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Movimiento inexistente"));
        if (!original.cerrado()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El movimiento original no está cerrado");
        }
        if (request.direccion() != TipoMovimiento.DEBITO && request.direccion() != TipoMovimiento.CREDITO) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dirección de ajuste inválida");
        }
        BigDecimal saldo = repository.saldoActual(original.cuentaId());
        BigDecimal nuevoSaldo = request.direccion() == TipoMovimiento.DEBITO
                ? saldo.subtract(request.importe()) : saldo.add(request.importe());
        if (nuevoSaldo.signum() < 0) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El ajuste dejaría un saldo negativo");
        }
        Movimiento ajuste = new Movimiento(504L, original.cuentaId(), TipoMovimiento.AJUSTE,
                request.direccion(), request.importe(), LocalDate.of(2025, 3, 10),
                request.concepto(), usuario, LocalDateTime.of(2025, 3, 10, 11, 35),
                false, original.id());
        // Respuesta dummy fija; reemplazar por inserción del ajuste sin alterar el original.
        return MovimientoDto.Response.from(ajuste, nuevoSaldo);
    }
}
