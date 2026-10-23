package com.empresa.sistemacore.modules.movimientos.application;

import com.empresa.sistemacore.modules.movimientos.domain.Movimiento;
import com.empresa.sistemacore.modules.movimientos.domain.MovimientoRepository;
import com.empresa.sistemacore.modules.movimientos.domain.TipoMovimiento;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class RegistrarMovimientoService {
    private static final LocalDate FECHA_OPERATIVA = LocalDate.of(2025, 3, 10);
    private static final LocalDateTime HORA_EJEMPLO = LocalDateTime.of(2025, 3, 10, 11, 30);
    private final MovimientoRepository repository;

    public RegistrarMovimientoService(MovimientoRepository repository) {
        this.repository = repository;
    }

    public MovimientoDto.SearchResponse searchMovements(Long cuentaId, LocalDate desde, LocalDate hasta) {
        if (desde != null && hasta != null && desde.isAfter(hasta)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El rango de fechas es inválido");
        }
        List<MovimientoDto.Response> resultados = new ArrayList<>();
        for (Movimiento movimiento : repository.findAll()) {
            if ((cuentaId == null || movimiento.cuentaId() == cuentaId)
                    && (desde == null || !movimiento.fechaOperativa().isBefore(desde))
                    && (hasta == null || !movimiento.fechaOperativa().isAfter(hasta))) {
                resultados.add(MovimientoDto.Response.from(movimiento, saldoEjemplo(movimiento.id())));
            }
        }
        // Respuesta dummy fija; reemplazar por consultas y cálculo transaccional de saldos.
        BigDecimal saldo = cuentaId == null ? repository.saldoActual(1001L) : repository.saldoActual(cuentaId);
        return new MovimientoDto.SearchResponse(resultados, saldo);
    }

    public MovimientoDto.Response createMovement(MovimientoDto.Request request, String usuario) {
        validateRequest(request);
        requireOpenDate(request.fechaOperativa());
        BigDecimal saldo = apply(repository.saldoActual(request.cuentaId()), request.tipo(), request.importe());
        Movimiento movimiento = new Movimiento(503L, request.cuentaId(), request.tipo(), request.tipo(),
                request.importe(), request.fechaOperativa(), request.concepto(), usuario,
                HORA_EJEMPLO, false, null);
        // Respuesta dummy fija; reemplazar por inserción y auditoría transaccionales.
        return MovimientoDto.Response.from(movimiento, saldo);
    }

    public MovimientoDto.Response updateOpenMovement(long id, MovimientoDto.Request request, String usuario) {
        Movimiento anterior = find(id);
        requireOpen(anterior);
        validateRequest(request);
        requireOpenDate(request.fechaOperativa());
        if (request.cuentaId() != anterior.cuentaId()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se puede cambiar la cuenta del movimiento");
        }
        BigDecimal saldoSinAnterior = apply(repository.saldoActual(anterior.cuentaId()),
                opposite(anterior.direccion()), anterior.importe());
        BigDecimal saldo = apply(saldoSinAnterior, request.tipo(), request.importe());
        Movimiento actualizado = new Movimiento(id, anterior.cuentaId(), request.tipo(), request.tipo(),
                request.importe(), request.fechaOperativa(), request.concepto(), usuario,
                HORA_EJEMPLO, false, null);
        // Respuesta dummy fija; reemplazar por actualización y auditoría transaccionales.
        return MovimientoDto.Response.from(actualizado, saldo);
    }

    public MovimientoDto.DeletionResponse deleteOpenMovement(long id, String usuario) {
        Movimiento anterior = find(id);
        requireOpen(anterior);
        BigDecimal saldo = apply(repository.saldoActual(anterior.cuentaId()),
                opposite(anterior.direccion()), anterior.importe());
        // Constancia dummy; reemplazar por baja y auditoría transaccionales.
        return new MovimientoDto.DeletionResponse(id, "ELIMINADO", usuario, HORA_EJEMPLO, saldo);
    }

    private void validateRequest(MovimientoDto.Request request) {
        if (request.tipo() != TipoMovimiento.DEBITO && request.tipo() != TipoMovimiento.CREDITO) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Solo se admiten débitos y créditos");
        }
    }

    private void requireOpen(Movimiento movimiento) {
        if (movimiento.cerrado() || movimiento.fechaOperativa().isBefore(FECHA_OPERATIVA)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "BR-01: un movimiento cerrado solo se corrige mediante un ajuste");
        }
    }

    private void requireOpenDate(LocalDate fecha) {
        if (!FECHA_OPERATIVA.equals(fecha)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "La fecha operativa no está abierta");
        }
    }

    private Movimiento find(long id) {
        return repository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Movimiento inexistente"));
    }

    private TipoMovimiento opposite(TipoMovimiento tipo) {
        return tipo == TipoMovimiento.DEBITO ? TipoMovimiento.CREDITO : TipoMovimiento.DEBITO;
    }

    private BigDecimal apply(BigDecimal saldo, TipoMovimiento direccion, BigDecimal importe) {
        BigDecimal resultado = direccion == TipoMovimiento.DEBITO
                ? saldo.subtract(importe) : saldo.add(importe);
        if (resultado.signum() < 0) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El débito dejaría un saldo negativo");
        }
        return resultado;
    }

    private BigDecimal saldoEjemplo(long id) {
        return switch ((int) id) {
            case 500 -> new BigDecimal("1000.00");
            case 501 -> new BigDecimal("1300.00");
            default -> new BigDecimal("1250.00");
        };
    }
}
