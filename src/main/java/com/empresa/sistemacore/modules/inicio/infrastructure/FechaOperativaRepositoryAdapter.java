package com.empresa.sistemacore.modules.inicio.infrastructure;

import com.empresa.sistemacore.modules.inicio.domain.FechaOperativaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public class FechaOperativaRepositoryAdapter implements FechaOperativaRepository {

    private final FechaOperativaJpaRepository fechas;

    public FechaOperativaRepositoryAdapter(FechaOperativaJpaRepository fechas) {
        this.fechas = fechas;
    }

    @Override
    public Optional<LocalDate> vigente() {
        return fechas.findById((short) 1).map(FechaOperativaJpaEntity::getFecha);
    }
}
