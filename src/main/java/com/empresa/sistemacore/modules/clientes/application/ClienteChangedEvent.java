package com.empresa.sistemacore.modules.clientes.application;

import java.time.OffsetDateTime;

public record ClienteChangedEvent(Long clientId, String action, String previousValue,
                                  String newValue, String user, OffsetDateTime occurredAt) {
}
