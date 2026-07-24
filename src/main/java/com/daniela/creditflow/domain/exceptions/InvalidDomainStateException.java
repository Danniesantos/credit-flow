package com.daniela.creditflow.domain.exceptions;

public class InvalidDomainStateException extends BusinessRuleException{

    public InvalidDomainStateException(String message) {
        super(message);
    }
}
