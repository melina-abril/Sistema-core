package com.empresa.sistemacore.modules.autenticacion.infrastructure;

import com.empresa.sistemacore.modules.autenticacion.domain.Usuario;
import com.empresa.sistemacore.modules.autenticacion.domain.UsuarioRepository;
import com.empresa.sistemacore.shared.security.Roles;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

/** Usuarios de demostración; reemplazar este adaptador al aprobar una base de datos. */
@Repository
public class UsuarioRepositoryAdapter implements UsuarioRepository {

    private final Map<String, Usuario> usuarios = new ConcurrentHashMap<>();

    public UsuarioRepositoryAdapter(PasswordEncoder passwordEncoder) {
        guardar(new Usuario("usr-1", "operador", "Operador de ejemplo",
                passwordEncoder.encode("Operador123!"), Roles.OPERADOR));
        guardar(new Usuario("usr-2", "supervisor", "Supervisor de ejemplo",
                passwordEncoder.encode("Supervisor123!"), Roles.SUPERVISOR));
    }

    @Override
    public Optional<Usuario> porId(String id) {
        return Optional.ofNullable(usuarios.get(id));
    }

    @Override
    public Optional<Usuario> porUsername(String username) {
        return usuarios.values().stream().filter(usuario -> usuario.getUsername().equals(username)).findFirst();
    }

    @Override
    public Usuario guardar(Usuario usuario) {
        usuarios.put(usuario.getId(), usuario);
        return usuario;
    }
}
