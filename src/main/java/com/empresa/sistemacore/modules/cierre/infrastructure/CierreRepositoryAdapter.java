package com.empresa.sistemacore.modules.cierre.infrastructure;

import com.empresa.sistemacore.modules.cierre.domain.CierreDiario;
import com.empresa.sistemacore.modules.cierre.domain.CierreRepository;
import com.empresa.sistemacore.modules.cierre.domain.EstadoCierre;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class CierreRepositoryAdapter implements CierreRepository {
    // Datos dummy en memoria para demostrar el circuito; reemplazar por Oracle.
    private static final LocalDate FECHA_OPERATIVA = LocalDate.of(2026, 6, 15);
    private final Map<LocalDate, CierreDiario> cierres = new ConcurrentHashMap<>();

    public CierreRepositoryAdapter() {
        LocalDate anterior = FECHA_OPERATIVA.minusDays(1);
        cierres.put(anterior, new CierreDiario(anterior, 12, EstadoCierre.COMPLETADO,
                new BigDecimal("31500.00"), new BigDecimal("46200.00"),
                Map.of("ARS", new BigDecimal("275000.00")), null));
    }

    @Override
    public LocalDate fechaOperativaVigente() {
        return FECHA_OPERATIVA;
    }

    @Override
    public Optional<CierreDiario> findByDate(LocalDate fecha) {
        return Optional.ofNullable(cierres.get(fecha));
    }

    @Override
    public List<CierreDiario> findAll() {
        return cierres.values().stream()
                .sorted(Comparator.comparing(CierreDiario::fechaOperativa).reversed()).toList();
    }

    @Override
    public CierreDiario save(CierreDiario cierre) {
        cierres.put(cierre.fechaOperativa(), cierre);
        return cierre;
    }
}
