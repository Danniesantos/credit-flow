package com.daniela.creditflow.domain.customer.exception;

import com.daniela.creditflow.domain.customer.valueObject.CustomerId;

public class CustomerAlreadyInactiveException extends RuntimeException {

    public CustomerAlreadyInactiveException(CustomerId customerId) {
        super("Customer with id %s is already inactive."
                .formatted(customerId.value()));
    }
}
