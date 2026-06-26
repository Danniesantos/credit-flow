package com.daniela.creditflow.application.exceptions;

import com.daniela.creditflow.domain.customer.valueObject.CustomerId;

public class CustomerAlreadyInactiveException extends RuntimeException {

    public CustomerAlreadyInactiveException(CustomerId customerId) {
        super("Customer with id " + customerId.value() + " is already inactive.");
    }
}
