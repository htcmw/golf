package com.reborn.golf.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class NotExistsTokenInfoException extends RuntimeException  {
    public NotExistsTokenInfoException(String message) {
        super(message);
    }
}
