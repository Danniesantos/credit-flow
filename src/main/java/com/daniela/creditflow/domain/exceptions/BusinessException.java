package com.daniela.creditflow.domain.exceptions;

public abstract class BusinessException extends RuntimeException{

    protected BusinessException(String message) {
        super(message);
    }}
