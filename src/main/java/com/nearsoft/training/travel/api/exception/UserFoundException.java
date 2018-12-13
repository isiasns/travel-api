package com.nearsoft.training.travel.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserFoundException extends RuntimeException {
    public UserFoundException(String exception) {
        super(exception);
    }
}
