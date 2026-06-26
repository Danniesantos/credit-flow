package com.daniela.creditflow.application.exceptions;

import com.daniela.creditflow.domain.customer.valueObject.CustomerId;

public class CustomerHasOpenCreditsException extends RuntimeException {
    public CustomerHasOpenCreditsException(CustomerId customerId) {
        super("Customer with id " + customerId.value() + " has open credits and cannot be deactivated.");
    }
}
