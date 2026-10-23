package com.empresa.sistemacore.modules.cierre.infrastructure;

import java.time.LocalDate;
import org.springframework.data.jpa.repository.JpaRepository;

// Contrato reservado para Oracle; las respuestas actuales usan únicamente el adaptador dummy.
public interface CierreJpaRepository extends JpaRepository<CierreJpaEntity, LocalDate> {
}
