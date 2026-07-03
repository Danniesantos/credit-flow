package com.daniela.creditflow.domain.exceptions;

public class InstallmentAlreadyPaidException extends RuntimeException {
    public InstallmentAlreadyPaidException() {
        super("Installment is already paid");
    }
}
