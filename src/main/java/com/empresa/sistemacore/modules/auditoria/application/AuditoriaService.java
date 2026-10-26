package com.empresa.sistemacore.modules.auditoria.application;

import com.empresa.sistemacore.modules.auditoria.domain.AuditoriaRepository;
import com.empresa.sistemacore.modules.auditoria.domain.RegistroAuditoria;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class AuditoriaService {
    private final AuditoriaRepository repository;

    public AuditoriaService(AuditoriaRepository repository) {
        this.repository = repository;
    }

    public List<AuditoriaDto> consultar() {
        return repository.findAll().stream()
                .map(registro -> new AuditoriaDto(
                        registro.id(), registro.entidad(), registro.accion(),
                        registro.valoresAnterior(), registro.valoresNuevo(),
                        registro.usuario(), registro.fechaHora()))
                .toList();
    }

    public void registrar(RegistroAuditoria registro) {
        repository.append(registro);
    }
}
