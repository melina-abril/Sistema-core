package com.empresa.sistemacore.modules.autenticacion.infrastructure;

import com.empresa.sistemacore.modules.autenticacion.domain.Usuario;
import com.empresa.sistemacore.shared.security.Roles;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "usuarios")
public class UsuarioJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "username", nullable = false, length = 50)
    private String username;

    @Column(name = "nombre", nullable = false, length = 120)
    private String nombre;

    @Column(name = "password_hash", nullable = false, length = 100)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(name = "rol", nullable = false, length = 20)
    private Roles rol;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private Usuario.Estado estado;

    @Column(name = "intentos_fallidos", nullable = false)
    private short intentosFallidos;

    @Column(name = "bloqueado_en")
    private Instant bloqueadoEn;

    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false, insertable = false)
    private Instant updatedAt;

    protected UsuarioJpaEntity() {
    }

    public Usuario toDomain() {
        return new Usuario(id.toString(), username, nombre, passwordHash, rol, estado,
                intentosFallidos, bloqueadoEn);
    }

    public void updateFrom(Usuario usuario) {
        this.id = UUID.fromString(usuario.getId());
        this.username = usuario.getUsername();
        this.nombre = usuario.getNombre();
        this.passwordHash = usuario.getPasswordHash();
        this.rol = usuario.getRol();
        this.estado = usuario.getEstado();
        this.intentosFallidos = (short) usuario.getIntentosFallidos();
        this.bloqueadoEn = usuario.getBloqueadoEn();
        this.updatedAt = Instant.now();
    }
}
