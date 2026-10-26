package com.empresa.sistemacore.modules.auditoria.domain;

import java.util.List;

public interface AuditoriaRepository {
    void append(RegistroAuditoria registro);

    List<RegistroAuditoria> findAll();
}
