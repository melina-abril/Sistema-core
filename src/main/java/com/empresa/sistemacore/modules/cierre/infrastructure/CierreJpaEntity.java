package com.empresa.sistemacore.modules.cierre.infrastructure;

import com.empresa.sistemacore.modules.cierre.domain.EstadoCierre;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "CIERRES_DIARIOS")
public class CierreJpaEntity {
    // Mapeo reservado para el adaptador Oracle; los servicios usan datos dummy.
    @Id
    @Column(name = "FECHA_OPERATIVA", nullable = false)
    private LocalDate fechaOperativa;

    @Enumerated(EnumType.STRING)
    @Column(name = "ESTADO", nullable = false)
    private EstadoCierre estado;

    @Column(name = "CANTIDAD_MOVIMIENTOS", nullable = false)
    private int cantidadMovimientos;

    @Column(name = "TOTAL_DEBITOS", precision = 19, scale = 2)
    private BigDecimal totalDebitos;

    @Column(name = "TOTAL_CREDITOS", precision = 19, scale = 2)
    private BigDecimal totalCreditos;

    @Column(name = "MOTIVO_ERROR")
    private String motivoError;

    @Column(name = "CERRADO_EN")
    private Instant cerradoEn;

    protected CierreJpaEntity() {
    }
}
