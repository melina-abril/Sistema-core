package com.empresa.sistemacore.modules.autenticacion.domain;

import java.util.Optional;

/** Puerto de usuarios; su implementación actual es en memoria. */
public interface UsuarioRepository {

    Optional<Usuario> porId(String id);

    Optional<Usuario> porUsername(String username);

    Usuario guardar(Usuario usuario);
}
