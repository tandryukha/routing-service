package com.transporeon.routing.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NoAirportException extends RuntimeException {

    public NoAirportException(String message) {
        super(message);
    }
}
