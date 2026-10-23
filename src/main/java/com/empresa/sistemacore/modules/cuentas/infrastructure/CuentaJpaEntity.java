package com.empresa.sistemacore.modules.cuentas.infrastructure;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;

// Mapeo reservado para sustituir el adaptador dummy; no se consulta la base de datos.
@Entity
@Table(name = "CUENTAS")
public class CuentaJpaEntity {
    @Id
    private Long id;
    @Column(name = "CLIENTE_ID")
    private Long customerId;
    @Column(name = "NUMERO")
    private String number;
    @Column(name = "TIPO")
    private String type;
    @Column(name = "MONEDA")
    private String currency;
    @Column(name = "SALDO")
    private BigDecimal balance;
    @Column(name = "ESTADO")
    private String status;

    protected CuentaJpaEntity() {}
}
