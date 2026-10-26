package com.empresa.sistemacore.modules.clientes.application;

import com.empresa.sistemacore.modules.clientes.domain.Cliente;
import java.time.Instant;

public record ClienteChangedEvent(String action, Cliente previous, Cliente current, String username, Instant occurredAt) { }
