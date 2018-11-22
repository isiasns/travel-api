package com.nearsoft.training.travel.api.exception;

import java.io.IOException;

public class JsonConvetionException extends RuntimeException {
    public JsonConvetionException(String exception) {
        super(exception);
    }
}
