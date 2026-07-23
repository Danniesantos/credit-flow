package com.daniela.creditflow.domain.exceptions;

public class CustomerAlreadyInactiveException extends BusinessRuleException {

    public CustomerAlreadyInactiveException() {
        super("Customer is already inactive.");
    }
}
