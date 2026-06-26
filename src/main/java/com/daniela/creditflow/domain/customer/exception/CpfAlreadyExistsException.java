package com.daniela.creditflow.domain.customer.exception;

public class CpfAlreadyExistsException extends RuntimeException {

    public CpfAlreadyExistsException(String cpf) {
        super("A customer with the provided CPF already exists.");
    }
}
