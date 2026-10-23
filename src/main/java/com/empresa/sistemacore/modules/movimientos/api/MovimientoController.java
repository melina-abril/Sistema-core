package com.empresa.sistemacore.modules.movimientos.api;

import com.empresa.sistemacore.modules.movimientos.application.AjusteMovimientoService;
import com.empresa.sistemacore.modules.movimientos.application.MovimientoDto;
import com.empresa.sistemacore.modules.movimientos.application.RegistrarMovimientoService;
import jakarta.validation.Valid;
import java.security.Principal;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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
    private final RegistrarMovimientoService registrar;
    private final AjusteMovimientoService ajustes;

    public MovimientoController(RegistrarMovimientoService registrar, AjusteMovimientoService ajustes) {
        this.registrar = registrar;
        this.ajustes = ajustes;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('OPERADOR', 'SUPERVISOR')")
    public MovimientoDto.SearchResponse searchMovements(
            @RequestParam(required = false) Long cuentaId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {
        return registrar.searchMovements(cuentaId, desde, hasta);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('OPERADOR', 'SUPERVISOR')")
    public MovimientoDto.Response createMovement(@Valid @RequestBody MovimientoDto.Request request,
                                                  Principal principal) {
        return registrar.createMovement(request, principal.getName());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('OPERADOR', 'SUPERVISOR')")
    public MovimientoDto.Response updateOpenMovement(@PathVariable long id,
                                                      @Valid @RequestBody MovimientoDto.Request request,
                                                      Principal principal) {
        return registrar.updateOpenMovement(id, request, principal.getName());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('OPERADOR', 'SUPERVISOR')")
    public MovimientoDto.DeletionResponse deleteOpenMovement(@PathVariable long id, Principal principal) {
        return registrar.deleteOpenMovement(id, principal.getName());
    }

    @PostMapping("/{id}/ajustes")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('SUPERVISOR')")
    public MovimientoDto.Response createAdjustment(@PathVariable long id,
                                                     @Valid @RequestBody MovimientoDto.AdjustmentRequest request,
                                                     Principal principal) {
        return ajustes.createAdjustment(id, request, principal.getName());
    }
}
