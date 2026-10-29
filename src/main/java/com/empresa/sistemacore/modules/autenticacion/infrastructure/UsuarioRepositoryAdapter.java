package com.empresa.sistemacore.modules.autenticacion.infrastructure;

import com.empresa.sistemacore.modules.autenticacion.domain.Usuario;
import com.empresa.sistemacore.modules.autenticacion.domain.UsuarioRepository;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public class UsuarioRepositoryAdapter implements UsuarioRepository {

    private final UsuarioJpaRepository repository;

    public UsuarioRepositoryAdapter(UsuarioJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Usuario> porUsernameParaActualizar(String username) {
        return repository.findByUsernameForUpdate(username).map(UsuarioJpaEntity::toDomain);
    }

    @Override
    public Optional<Usuario> porIdParaActualizar(String id) {
        return repository.findByIdForUpdate(UUID.fromString(id)).map(UsuarioJpaEntity::toDomain);
    }

    @Override
    public Usuario guardar(Usuario usuario) {
        UUID id = UUID.fromString(usuario.getId());
        UsuarioJpaEntity entity = repository.findById(id).orElseGet(UsuarioJpaEntity::new);
        entity.updateFrom(usuario);
        return repository.saveAndFlush(entity).toDomain();
    }
}
