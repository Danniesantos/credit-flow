package com.daniela.creditflow.domain.exceptions;

public abstract class ResourceNotFoundException extends BusinessException {

    protected ResourceNotFoundException(String message) {
        super(message);
    }
}
