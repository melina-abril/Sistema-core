package com.empresa.sistemacore.modules.cierre.infrastructure;

import com.empresa.sistemacore.modules.cierre.domain.EstadoCierre;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "CIERRES_DIARIOS")
public class CierreJpaEntity {
    @Id
    @Column(name = "FECHA_OPERATIVA", nullable = false)
    private LocalDate fechaOperativa;

    @Enumerated(EnumType.STRING)
    @Column(name = "ESTADO", nullable = false)
    private EstadoCierre estado;

    protected CierreJpaEntity() {
    }

    public LocalDate getFechaOperativa() {
        return fechaOperativa;
    }

    public EstadoCierre getEstado() {
        return estado;
    }
}
