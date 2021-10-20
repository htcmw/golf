package com.reborn.golf.dto.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class IamPortException extends RuntimeException  {
    public IamPortException(String message) {
        super(message);
    }
}
