package com.reborn.golf.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ErrorResponseDto {
    private String statusCode;
    private String result;
    private List<ErrorDto> errorList;

}
