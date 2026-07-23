package com.daniela.creditflow.domain.exceptions;

public class CustomerHasOpenCreditsException extends BusinessRuleException {

    public CustomerHasOpenCreditsException() {

        super("Customer has open credits and cannot be deactivated.");
    }
}
