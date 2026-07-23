package com.daniela.creditflow.domain.exceptions;

public class CustomerNotFoundException extends ResourceNotFoundException {

    public CustomerNotFoundException() {
        super("Customer not found");
    }
}
