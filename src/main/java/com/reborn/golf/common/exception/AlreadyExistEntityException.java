package com.reborn.golf.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AlreadyExistEntityException extends RuntimeException  {
    public AlreadyExistEntityException(String message) {
        super(message);
    }
}
