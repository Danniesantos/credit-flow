package com.daniela.creditflow.domain.customer.exception;

import com.daniela.creditflow.domain.customer.valueObject.CustomerId;

public class CustomerNotFoundException extends RuntimeException {
    public CustomerNotFoundException(CustomerId customerId) {
        super("Customer with id %s not found"
                .formatted(customerId.value()));
    }
}
