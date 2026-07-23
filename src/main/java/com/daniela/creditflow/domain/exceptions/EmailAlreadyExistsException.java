package com.daniela.creditflow.domain.exceptions;

public class EmailAlreadyExistsException extends ConflictException {

    public EmailAlreadyExistsException() {
        super("Customer with the provided email already exists.");
    }
}
