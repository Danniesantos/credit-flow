package com.daniela.creditflow.domain.exceptions;

public abstract class ConflictException extends BusinessException{

    protected ConflictException(String message) {
        super(message);
    }
}
