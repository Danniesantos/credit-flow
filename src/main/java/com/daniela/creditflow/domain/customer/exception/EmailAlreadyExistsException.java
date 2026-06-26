package com.daniela.creditflow.domain.customer.exception;

public class EmailAlreadyExistsException extends RuntimeException {

    public EmailAlreadyExistsException(String email) {
        super("Customer with the provided email already exists.");
    }
}
