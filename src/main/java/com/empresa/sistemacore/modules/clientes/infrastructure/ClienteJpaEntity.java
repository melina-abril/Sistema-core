package com.empresa.sistemacore.modules.clientes.infrastructure;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "CLIENTES")
public class ClienteJpaEntity {
    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "NUMERO")
    private String number;

    @Column(name = "NOMBRE")
    private String name;

    @Column(name = "DOCUMENTO")
    private String document;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "TELEFONO")
    private String phone;

    @Column(name = "DIRECCION")
    private String address;

    @Column(name = "ACTIVO")
    private boolean active;

    protected ClienteJpaEntity() {
    }
}
