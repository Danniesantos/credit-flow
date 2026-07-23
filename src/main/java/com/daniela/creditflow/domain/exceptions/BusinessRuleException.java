package com.daniela.creditflow.domain.exceptions;

public abstract class BusinessRuleException extends BusinessException {

    protected BusinessRuleException(String message) {
        super(message);
    }
}
