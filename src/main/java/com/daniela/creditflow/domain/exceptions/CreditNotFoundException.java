package com.daniela.creditflow.domain.exceptions;

import com.daniela.creditflow.domain.credit.valueObject.CreditId;

public class CreditNotFoundException extends RuntimeException {
    public CreditNotFoundException(CreditId creditId) {

        super("Credit with id %s not found".formatted(creditId.value()));
    }
}
