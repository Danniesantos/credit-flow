package com.daniela.creditflow.domain.customer.exception;

import com.daniela.creditflow.domain.customer.valueObject.CustomerId;

public class CustomerHasOpenCreditsException extends RuntimeException {
    public CustomerHasOpenCreditsException(CustomerId customerId) {
        super("Customer with id %s has open credits and cannot be deactivated."
                .formatted(customerId.value()));
    }
}
