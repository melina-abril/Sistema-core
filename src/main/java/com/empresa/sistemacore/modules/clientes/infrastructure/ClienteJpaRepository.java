package com.empresa.sistemacore.modules.clientes.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

// Puerto JPA reservado para sustituir el adaptador dummy; no se instancia ni consulta por ahora.
@NoRepositoryBean
public interface ClienteJpaRepository extends JpaRepository<ClienteJpaEntity, Long> {
}
