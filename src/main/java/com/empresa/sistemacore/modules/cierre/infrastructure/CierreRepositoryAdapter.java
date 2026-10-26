package com.empresa.sistemacore.modules.cierre.infrastructure;

import com.empresa.sistemacore.modules.cierre.domain.CierreDiario;
import com.empresa.sistemacore.modules.cierre.domain.CierreRepository;
import com.empresa.sistemacore.modules.cierre.domain.EstadoCierre;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public class CierreRepositoryAdapter implements CierreRepository {
    // Datos dummy en memoria: reemplazar por persistencia Oracle al conectar el backend.
    private LocalDate fechaOperativa = LocalDate.of(2026, 5, 18);
    private final List<CierreDiario> cierres = new ArrayList<>(List.of(
            new CierreDiario(LocalDate.of(2026, 5, 15), EstadoCierre.COMPLETADO, 24,
                    new BigDecimal("82000.00"), new BigDecimal("96500.00"),
                    Map.of("CC-1001", new BigDecimal("700000.00"),
                           "CA-2002", new BigDecimal("138000.00")), null,
                    Instant.parse("2026-05-15T18:35:00Z")),
            new CierreDiario(LocalDate.of(2026, 5, 14), EstadoCierre.COMPLETADO, 19,
                    new BigDecimal("45000.00"), new BigDecimal("59000.00"),
                    Map.of("CC-1001", new BigDecimal("685500.00"),
                           "CA-2002", new BigDecimal("138000.00")), null,
                    Instant.parse("2026-05-14T18:32:00Z"))
    ));

    @Override
    public synchronized LocalDate fechaOperativa() {
        return fechaOperativa;
    }

    @Override
    public synchronized CierreDiario cierreActual() {
        return cierres.stream().filter(c -> c.fechaOperativa().equals(fechaOperativa))
                .findFirst().orElseGet(() -> new CierreDiario(fechaOperativa, EstadoCierre.PENDIENTE,
                        fechaOperativa.equals(LocalDate.of(2026, 5, 18)) ? 12 : 0,
                        null, null, Map.of(), null, null));
    }

    @Override
    public synchronized List<CierreDiario> historial() {
        return cierres.stream().filter(c -> c.fechaOperativa().isBefore(fechaOperativa))
                .sorted(Comparator.comparing(CierreDiario::fechaOperativa).reversed()).toList();
    }

    @Override
    public synchronized void guardar(CierreDiario cierre) {
        cierres.removeIf(c -> c.fechaOperativa().equals(cierre.fechaOperativa()));
        cierres.add(cierre);
    }

    @Override
    public synchronized void avanzarFechaOperativa(LocalDate fechaCerrada) {
        if (!fechaOperativa.equals(fechaCerrada)) {
            throw new IllegalStateException("La fecha operativa cambió durante el cierre");
        }
        fechaOperativa = fechaCerrada.plusDays(1);
        while (fechaOperativa.getDayOfWeek().getValue() >= 6) {
            fechaOperativa = fechaOperativa.plusDays(1);
        }
    }
}
