package com.empresa.sistemacore.modules.auditoria.infrastructure;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import org.hibernate.annotations.Immutable;

// Mapeo preparado para la persistencia append-only; el adaptador dummy no accede a Oracle.
@Entity
@Immutable
@Table(name = "AUDITORIA")
public class AuditoriaJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ENTIDAD", nullable = false)
    private String entidad;

    @Column(name = "ACCION", nullable = false)
    private String accion;

    @Column(name = "VALORES_ANTERIOR", length = 4000)
    private String valoresAnterior;

    @Column(name = "VALORES_NUEVO", length = 4000)
    private String valoresNuevo;

    @Column(name = "USUARIO", nullable = false)
    private String usuario;

    @Column(name = "FECHA_HORA", nullable = false)
    private LocalDateTime fechaHora;

    protected AuditoriaJpaEntity() {
    }
}
