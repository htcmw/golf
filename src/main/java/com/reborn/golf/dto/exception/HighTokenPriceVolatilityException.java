package com.reborn.golf.dto.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class HighTokenPriceVolatilityException extends RuntimeException  {
    public HighTokenPriceVolatilityException(String message) {
        super(message);
    }
}
