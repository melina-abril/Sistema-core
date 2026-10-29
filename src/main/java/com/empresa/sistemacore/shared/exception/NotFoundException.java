package com.empresa.sistemacore.shared.exception;

/** Recurso inexistente; GlobalExceptionHandler lo traduce a 404. */
public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }
}
