package com.empresa.sistemacore.modules.movimientos.api;

import com.empresa.sistemacore.modules.movimientos.application.AjusteMovimientoService;
import com.empresa.sistemacore.modules.movimientos.application.MovimientoDto;
import com.empresa.sistemacore.modules.movimientos.application.RegistrarMovimientoService;
import jakarta.validation.Valid;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/movimientos")
public class MovimientoController {
    private final RegistrarMovimientoService movimientos;
    private final AjusteMovimientoService ajustes;

    public MovimientoController(RegistrarMovimientoService movimientos, AjusteMovimientoService ajustes) {
        this.movimientos = movimientos;
        this.ajustes = ajustes;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('OPERADOR', 'SUPERVISOR')")
    public MovimientoDto.ConsultaResponse consultarMovimientos(
            @RequestParam(required = false) Long cuentaId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {
        return movimientos.consultarMovimientos(cuentaId, desde, hasta);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('OPERADOR', 'SUPERVISOR')")
    public MovimientoDto.Response registrarMovimiento(@Valid @RequestBody MovimientoDto.Request request,
                                                       Authentication authentication) {
        return movimientos.registrarMovimiento(request, authentication.getName());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('OPERADOR', 'SUPERVISOR')")
    public MovimientoDto.Response modificarMovimiento(@PathVariable Long id,
                                                       @Valid @RequestBody MovimientoDto.Request request,
                                                       Authentication authentication) {
        return movimientos.modificarMovimiento(id, request, authentication.getName());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('OPERADOR', 'SUPERVISOR')")
    public MovimientoDto.BajaResponse eliminarMovimiento(@PathVariable Long id,
                                                         Authentication authentication) {
        return movimientos.eliminarMovimiento(id, authentication.getName());
    }

    @PostMapping("/{id}/ajustes")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('SUPERVISOR')")
    public MovimientoDto.Response ajustarMovimiento(@PathVariable Long id,
                                                     @Valid @RequestBody MovimientoDto.AjusteRequest request,
                                                     Authentication authentication) {
        return ajustes.ajustarMovimiento(id, request, authentication.getName());
    }
}
