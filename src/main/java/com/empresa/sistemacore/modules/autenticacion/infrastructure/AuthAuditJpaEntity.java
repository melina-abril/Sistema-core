package com.empresa.sistemacore.modules.autenticacion.infrastructure;

import com.empresa.sistemacore.modules.autenticacion.domain.AuthEvent;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.Map;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "auditoria")
public class AuthAuditJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "entidad", nullable = false, length = 30)
    private String entidad;

    @Column(name = "entidad_id", nullable = false, length = 50)
    private String entidadId;

    @Column(name = "accion", nullable = false, length = 30)
    private String accion;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "valores_anteriores", columnDefinition = "jsonb")
    private Map<String, String> valoresAnteriores;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "valores_nuevos", columnDefinition = "jsonb")
    private Map<String, String> valoresNuevos;

    @Column(name = "usuario", nullable = false, length = 50)
    private String usuario;

    @Column(name = "fecha", nullable = false)
    private Instant fecha;

    protected AuthAuditJpaEntity() {
    }

    public AuthAuditJpaEntity(AuthEvent event) {
        this.entidad = "USUARIO";
        this.entidadId = event.entityId();
        this.accion = event.action();
        this.valoresAnteriores = null;
        this.valoresNuevos = Map.of("username", event.username());
        this.usuario = event.actor();
        this.fecha = event.fecha();
    }
}
