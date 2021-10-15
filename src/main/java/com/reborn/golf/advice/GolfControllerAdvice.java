package com.reborn.golf.advice;

import com.reborn.golf.dto.exception.ErrorDto;
import com.reborn.golf.dto.exception.ErrorResponseDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


/*
 * Validation Exception을 처리하기 위한클래스 입니다. MethodArgumentNotValidException, ConstraintViolationException,
 * MissingServletRequestParameterException에 대한 처리 결과를 Client에게 보내고 있습니다.
 */
@Log4j2
@RestControllerAdvice(basePackages = "com.reborn.golf.controller")
public class GolfControllerAdvice {


    /*
     * Validation Exception을 제외한 모든 예외는 계속 유지시킵니다
     * */
    @ExceptionHandler
    public void exception(Exception e) throws Exception {
        log.info("<<<<<<<<<<<<<<<<<<<<GolfControllerAdvice>>>>>>>>>>>>>>>>>>>>>");
        e.printStackTrace();
        throw new Exception();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> methodArgumentNotValidException(MethodArgumentNotValidException e) {

        BindingResult bindingResult = e.getBindingResult();

        List<ErrorDto> errorDtoList = new ArrayList<>();

        bindingResult.getAllErrors().forEach(error -> {

            FieldError fieldError = (FieldError) error;

            errorDtoList.add(ErrorDto.builder()
                    .field(fieldError.getField())
                    .value(fieldError.getRejectedValue().toString())
                    .message(fieldError.getDefaultMessage())
                    .build());
        });

        ErrorResponseDto errorResponseDto = new ErrorResponseDto();
        errorResponseDto.setStatusCode(HttpStatus.BAD_REQUEST.toString());
        errorResponseDto.setResult("fail");
        errorResponseDto.setErrorList(errorDtoList);

        return new ResponseEntity<>(errorResponseDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponseDto> constraintViolationException(ConstraintViolationException e) {

        List<ErrorDto> errorDtoList = new ArrayList<>();

        e.getConstraintViolations().forEach(error -> {
            List<Path.Node> paths = StreamSupport.stream(error.getPropertyPath().spliterator(), false).collect(Collectors.toList());
            errorDtoList.add(ErrorDto.builder()
                    .field(paths.get(paths.size() - 1).getName())
                    .value(error.getInvalidValue().toString())
                    .message(error.getMessage())
                    .build());
        });

        ErrorResponseDto errorResponseDto = new ErrorResponseDto();
        errorResponseDto.setStatusCode(HttpStatus.BAD_REQUEST.toString());
        errorResponseDto.setResult("fail");
        errorResponseDto.setErrorList(errorDtoList);

        return new ResponseEntity<>(errorResponseDto, HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponseDto> missingServletRequestParameterException(MissingServletRequestParameterException e) {

        List<ErrorDto> errorDtoList = new ArrayList<>();

        errorDtoList.add(ErrorDto.builder()
                .field(e.getParameterName())
                .value(null)
                .message(e.getMessage())
                .build());

        ErrorResponseDto errorResponseDto = new ErrorResponseDto();
        errorResponseDto.setStatusCode(HttpStatus.BAD_REQUEST.toString());
        errorResponseDto.setResult("fail");
        errorResponseDto.setErrorList(errorDtoList);

        return new ResponseEntity<>(errorResponseDto, HttpStatus.BAD_REQUEST);
    }

}
