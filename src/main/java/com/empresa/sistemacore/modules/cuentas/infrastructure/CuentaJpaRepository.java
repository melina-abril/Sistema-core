package com.empresa.sistemacore.modules.cuentas.infrastructure;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

// Puerto JPA reservado para una implementación futura; las respuestas actuales son dummy.
public interface CuentaJpaRepository extends JpaRepository<CuentaJpaEntity, Long> {
    List<CuentaJpaEntity> findByCustomerId(Long customerId);
}
