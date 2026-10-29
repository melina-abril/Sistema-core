package com.empresa.sistemacore.modules.autenticacion.domain;

import java.util.Optional;

public interface UsuarioRepository {

    Optional<Usuario> porUsernameParaActualizar(String username);

    Optional<Usuario> porIdParaActualizar(String id);

    Usuario guardar(Usuario usuario);
}
