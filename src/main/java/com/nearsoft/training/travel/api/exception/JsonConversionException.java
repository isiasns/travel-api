package com.nearsoft.training.travel.api.exception;

public class JsonConversionException extends RuntimeException {
    public JsonConversionException(String exception) {
        super(exception);
    }
}
