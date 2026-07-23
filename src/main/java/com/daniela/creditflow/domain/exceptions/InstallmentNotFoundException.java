package com.daniela.creditflow.domain.exceptions;

public class InstallmentNotFoundException extends ResourceNotFoundException {

    public InstallmentNotFoundException() {
        super("Installment not found");
    }
}
