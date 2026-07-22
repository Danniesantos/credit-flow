package com.daniela.creditflow.domain.exceptions;

import com.daniela.creditflow.domain.installment.valueObject.InstallmentId;

public class InstallmentNotFoundException extends RuntimeException {
    public InstallmentNotFoundException(InstallmentId installmentId) {
        super("Installment with id %s not found"
                .formatted(installmentId.value()));
    }
}
