package com.nearsoft.training.travel.api.exception;

public class NoBookingFoundException extends RuntimeException {
    public NoBookingFoundException(String exception) {
        super(exception);
    }
}
