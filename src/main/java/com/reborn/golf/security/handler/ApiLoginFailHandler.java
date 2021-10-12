package com.reborn.golf.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reborn.golf.dto.exception.ErrorDto;
import com.reborn.golf.dto.exception.ErrorResponseDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@Log4j2
public class ApiLoginFailHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        log.info("==============ApiLoginFailHandler===============");
        log.info(exception.getMessage());

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=utf-8");

        ErrorResponseDto errorResponseDto = new ErrorResponseDto();
        List<ErrorDto> errorDtoList = new ArrayList<>();
        errorDtoList.add(ErrorDto.builder().field("Email").message(exception.getMessage()).value(null).build());
        errorResponseDto.setStatusCode(HttpStatus.UNAUTHORIZED.toString());
        errorResponseDto.setResult("fail");
        errorResponseDto.setErrorList(errorDtoList);

        ObjectMapper objectMapper = new ObjectMapper();
        String errorResponse = objectMapper.writeValueAsString(errorResponseDto);
        log.info(errorResponse);

        PrintWriter out = response.getWriter();
        out.print(errorResponse);

    }
}
