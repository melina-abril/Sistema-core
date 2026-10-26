package com.empresa.sistemacore.modules.movimientos.application;

import com.empresa.sistemacore.modules.movimientos.domain.Movimiento;
import com.empresa.sistemacore.modules.movimientos.domain.MovimientoRepository;
import com.empresa.sistemacore.modules.movimientos.domain.TipoMovimiento;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Comparator;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class RegistrarMovimientoService {
    // Fecha y saldos dummy; reemplazar por los puertos de cierre diario y cuentas.
    private static final LocalDate FECHA_OPERATIVA = LocalDate.of(2026, 5, 1);
    private static final BigDecimal SALDO_CUENTA = new BigDecimal("1425.00");
    private final MovimientoRepository repository;

    public RegistrarMovimientoService(MovimientoRepository repository) {
        this.repository = repository;
    }

    public MovimientoDto.ConsultaResponse consultarMovimientos(Long cuentaId, LocalDate desde, LocalDate hasta) {
        if (desde != null && hasta != null && desde.isAfter(hasta)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El rango de fechas es inválido");
        }
        List<MovimientoDto.Response> movimientos = repository.findAll().stream()
                .filter(m -> cuentaId == null || cuentaId.equals(m.cuentaId()))
                .filter(m -> desde == null || !m.fechaOperativa().isBefore(desde))
                .filter(m -> hasta == null || !m.fechaOperativa().isAfter(hasta))
                .sorted(Comparator.comparing(Movimiento::registradoEn))
                .map(MovimientoDto.Response::from)
                .toList();
        BigDecimal saldo = movimientos.isEmpty() ? BigDecimal.ZERO
                : movimientos.get(movimientos.size() - 1).saldoResultante();
        return new MovimientoDto.ConsultaResponse(movimientos, saldo);
    }

    public MovimientoDto.Response registrarMovimiento(MovimientoDto.Request request, String usuario) {
        validar(request);
        return respuestaDummy(4L, request, usuario);
    }

    public MovimientoDto.Response modificarMovimiento(Long id, MovimientoDto.Request request, String usuario) {
        Movimiento original = buscar(id);
        validarAbierto(original);
        validar(request);
        if (!original.cuentaId().equals(request.cuentaId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se puede cambiar la cuenta del movimiento");
        }
        return respuestaDummy(id, request, usuario);
    }

    public MovimientoDto.BajaResponse eliminarMovimiento(Long id, String usuario) {
        validarAbierto(buscar(id));
        // Baja dummy: no altera los datos de ejemplo; reemplazar por baja y auditoría transaccionales.
        return new MovimientoDto.BajaResponse(id, "ELIMINADO", usuario,
                OffsetDateTime.parse("2026-05-01T12:00:00-03:00"));
    }

    private MovimientoDto.Response respuestaDummy(Long id, MovimientoDto.Request request, String usuario) {
        BigDecimal saldo = request.tipo() == TipoMovimiento.DEBITO
                ? SALDO_CUENTA.subtract(request.importe()) : SALDO_CUENTA.add(request.importe());
        // Respuesta dummy; reemplazar por registro, cálculo de saldo y auditoría reales.
        return new MovimientoDto.Response(id, request.cuentaId(), request.tipo(), request.importe(),
                request.fechaOperativa(), request.concepto(), usuario,
                OffsetDateTime.parse("2026-05-01T12:00:00-03:00"), false, saldo, null);
    }

    private void validar(MovimientoDto.Request request) {
        if (request.tipo() == null || request.tipo() == TipoMovimiento.AJUSTE) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Seleccione débito o crédito");
        }
        if (request.importe() == null || request.importe().signum() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El importe debe ser mayor que cero");
        }
        if (!FECHA_OPERATIVA.equals(request.fechaOperativa())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "La fecha operativa no está abierta");
        }
        if (request.tipo() == TipoMovimiento.DEBITO && request.importe().compareTo(SALDO_CUENTA) > 0) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El débito dejaría un saldo negativo");
        }
    }

    private Movimiento buscar(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Movimiento no encontrado"));
    }

    private void validarAbierto(Movimiento movimiento) {
        if (movimiento.cerrado() || !FECHA_OPERATIVA.equals(movimiento.fechaOperativa())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "BR-01: un movimiento cerrado solo se corrige mediante un ajuste");
        }
    }
}
