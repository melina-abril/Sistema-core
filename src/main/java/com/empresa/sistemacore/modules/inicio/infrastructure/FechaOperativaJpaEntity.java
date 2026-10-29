package com.empresa.sistemacore.modules.inicio.infrastructure;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "fecha_operativa_vigente")
public class FechaOperativaJpaEntity {

    @Id
    @Column(name = "id", nullable = false)
    private Short id;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    protected FechaOperativaJpaEntity() {
    }

    public LocalDate getFecha() {
        return fecha;
    }
}
