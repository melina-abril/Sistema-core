package com.empresa.sistemacore.modules.autenticacion.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthAuditJpaRepository extends JpaRepository<AuthAuditJpaEntity, Long> {
}
