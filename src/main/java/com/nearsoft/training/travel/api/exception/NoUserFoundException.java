package com.nearsoft.training.travel.api.exception;

public class NoUserFoundException extends RuntimeException {
    public NoUserFoundException(String exception) {
        super(exception);
    }
}
