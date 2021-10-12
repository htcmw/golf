package com.reborn.golf.dto.exception;

import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ErrorDto {
    private String field;
    private String value;
    private String message;

}
