package com.empresa.sistemacore.modules.cierre.infrastructure;

import java.time.LocalDate;
import org.springframework.data.jpa.repository.JpaRepository;

// Puerto Spring Data reservado para la futura persistencia; no se consulta en modo dummy.
public interface CierreJpaRepository extends JpaRepository<CierreJpaEntity, LocalDate> {
}
