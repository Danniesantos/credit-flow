package com.daniela.creditflow.domain.exceptions;

public class CreditNotFoundException extends ResourceNotFoundException {

    public CreditNotFoundException() {
        super("Credit not found");
    }
}
