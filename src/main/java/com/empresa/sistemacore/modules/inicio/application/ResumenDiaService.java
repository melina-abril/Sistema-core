package com.empresa.sistemacore.modules.inicio.application;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ResumenDiaService {

    public ResumenDiaDto getDailySummary(String userName, String role) {
        List<ResumenDiaDto.Access> accesses = new ArrayList<>(List.of(
                new ResumenDiaDto.Access("Clientes", "/api/clientes"),
                new ResumenDiaDto.Access("Cuentas", "/api/cuentas"),
                new ResumenDiaDto.Access("Movimientos", "/api/movimientos")
        ));
        if ("SUPERVISOR".equals(role)) {
            accesses.add(new ResumenDiaDto.Access("Cierre diario", "/api/cierre-diario"));
        }

        // Respuesta dummy fija; reemplazar por consultas a la fecha operativa, movimientos y cierre vigentes.
        return new ResumenDiaDto(
                userName,
                role,
                LocalDate.of(2026, 5, 18),
                42,
                "Pendiente",
                List.copyOf(accesses)
        );
    }
}
