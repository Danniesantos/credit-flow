package com.daniela.creditflow.domain.exceptions;

public class CpfAlreadyExistsException extends ConflictException {

    public CpfAlreadyExistsException() {
        super("A customer with the provided CPF already exists.");
    }
}
