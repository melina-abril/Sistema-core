package com.empresa.sistemacore.modules.cierre.application;

import com.empresa.sistemacore.modules.cierre.domain.CierreDiario;
import com.empresa.sistemacore.modules.cierre.domain.CierreRepository;
import com.empresa.sistemacore.modules.cierre.domain.EstadoCierre;
import com.empresa.sistemacore.modules.cierre.infrastructure.CierreAlertasNotifier;
import com.empresa.sistemacore.modules.cierre.infrastructure.ConciliacionPlSqlAdapter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class EjecutarCierreService {
    private final CierreRepository repository;
    private final ConciliacionPlSqlAdapter conciliacion;
    private final CierreAlertasNotifier alertas;

    public EjecutarCierreService(CierreRepository repository,
            ConciliacionPlSqlAdapter conciliacion, CierreAlertasNotifier alertas) {
        this.repository = repository;
        this.conciliacion = conciliacion;
        this.alertas = alertas;
    }

    public CierreDto consultar() {
        LocalDate fecha = repository.fechaOperativaVigente();
        return CierreDto.from(repository.findByDate(fecha).orElseGet(() -> pendiente(fecha)));
    }

    public List<CierreDto> historial() {
        return repository.findAll().stream()
                .filter(cierre -> cierre.estado() == EstadoCierre.COMPLETADO)
                .map(CierreDto::from).toList();
    }

    public synchronized CierreDto ejecutar() {
        LocalDate fecha = repository.fechaOperativaVigente();
        CierreDiario actual = repository.findByDate(fecha).orElseGet(() -> pendiente(fecha));
        if (actual.estado() != EstadoCierre.PENDIENTE) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "El cierre ya se inició o requiere reproceso para esta fecha operativa");
        }
        return procesar(actual);
    }

    CierreDto procesar(CierreDiario actual) {
        repository.save(new CierreDiario(actual.fechaOperativa(), actual.cantidadMovimientos(),
                EstadoCierre.EN_CURSO, BigDecimal.ZERO, BigDecimal.ZERO, Map.of(), null));
        try {
            var resultado = conciliacion.conciliar(actual.fechaOperativa());
            return CierreDto.from(repository.save(new CierreDiario(actual.fechaOperativa(),
                    resultado.cantidadMovimientos(), EstadoCierre.COMPLETADO,
                    resultado.totalDebitos(), resultado.totalCreditos(),
                    resultado.saldosCierre(), null)));
        } catch (RuntimeException error) {
            String motivo = "No se pudo conciliar la fecha operativa";
            CierreDiario fallido = repository.save(new CierreDiario(actual.fechaOperativa(),
                    actual.cantidadMovimientos(), EstadoCierre.CON_ERROR,
                    BigDecimal.ZERO, BigDecimal.ZERO, Map.of(), motivo));
            alertas.notificarFalla(actual.fechaOperativa(), motivo);
            return CierreDto.from(fallido);
        }
    }

    private CierreDiario pendiente(LocalDate fecha) {
        // Datos dummy: la cantidad se reemplazará por una consulta al módulo de movimientos.
        return new CierreDiario(fecha, 18, EstadoCierre.PENDIENTE,
                BigDecimal.ZERO, BigDecimal.ZERO, Map.of(), null);
    }
}
