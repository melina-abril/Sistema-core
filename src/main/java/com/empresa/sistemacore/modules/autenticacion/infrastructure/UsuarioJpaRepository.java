package com.empresa.sistemacore.modules.autenticacion.infrastructure;

import jakarta.persistence.LockModeType;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UsuarioJpaRepository extends JpaRepository<UsuarioJpaEntity, UUID> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select u from UsuarioJpaEntity u where u.username = :username")
    Optional<UsuarioJpaEntity> findByUsernameForUpdate(@Param("username") String username);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select u from UsuarioJpaEntity u where u.id = :id")
    Optional<UsuarioJpaEntity> findByIdForUpdate(@Param("id") UUID id);
}
