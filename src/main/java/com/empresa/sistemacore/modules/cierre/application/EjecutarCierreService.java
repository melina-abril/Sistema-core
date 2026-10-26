package com.empresa.sistemacore.modules.cierre.application;

import com.empresa.sistemacore.modules.cierre.domain.CierreDiario;
import com.empresa.sistemacore.modules.cierre.domain.CierreRepository;
import com.empresa.sistemacore.modules.cierre.domain.EstadoCierre;
import com.empresa.sistemacore.modules.cierre.infrastructure.CierreAlertasNotifier;
import com.empresa.sistemacore.modules.cierre.infrastructure.ConciliacionPlSqlAdapter;
import java.math.BigDecimal;
import java.time.Instant;
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

    public EjecutarCierreService(CierreRepository repository, ConciliacionPlSqlAdapter conciliacion,
                                 CierreAlertasNotifier alertas) {
        this.repository = repository;
        this.conciliacion = conciliacion;
        this.alertas = alertas;
    }

    public CierreDto obtenerCierreDiario() {
        return CierreDto.desde(repository.cierreActual());
    }

    public List<CierreDto> listarCierres() {
        return repository.historial().stream().map(CierreDto::desde).toList();
    }

    public CierreDto ejecutar() {
        return procesar(false);
    }

    CierreDto reprocesar() {
        return procesar(true);
    }

    private CierreDto procesar(boolean reproceso) {
        // El adaptador dummy comparte el bloqueo para impedir dos cierres de la misma fecha.
        synchronized (repository) {
            CierreDiario actual = repository.cierreActual();
            EstadoCierre esperado = reproceso ? EstadoCierre.CON_ERROR : EstadoCierre.PENDIENTE;
            if (actual.estado() != esperado) {
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        reproceso ? "Solo se puede reprocesar un cierre fallido"
                                  : "La fecha operativa ya tiene un cierre iniciado o completado");
            }
            repository.guardar(new CierreDiario(actual.fechaOperativa(), EstadoCierre.EN_CURSO,
                    actual.cantidadMovimientos(), null, null, Map.of(), null, null));
            try {
                ConciliacionPlSqlAdapter.Resultado resultado = conciliacion.conciliar(actual);
                CierreDiario completado = new CierreDiario(actual.fechaOperativa(), EstadoCierre.COMPLETADO,
                        actual.cantidadMovimientos(), resultado.totalDebitos(), resultado.totalCreditos(),
                        resultado.saldosCierre(), null, Instant.parse("2026-05-18T18:30:00Z"));
                repository.guardar(completado);
                repository.avanzarFechaOperativa(actual.fechaOperativa());
                return CierreDto.desde(completado);
            } catch (RuntimeException ex) {
                String motivo = "No se pudo conciliar la fecha operativa";
                repository.guardar(new CierreDiario(actual.fechaOperativa(), EstadoCierre.CON_ERROR,
                        actual.cantidadMovimientos(), BigDecimal.ZERO, BigDecimal.ZERO,
                        Map.of(), motivo, null));
                alertas.notificarFalla(actual.fechaOperativa(), motivo);
                throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                        motivo + "; el día permanece abierto para reprocesar", ex);
            }
        }
    }
}
