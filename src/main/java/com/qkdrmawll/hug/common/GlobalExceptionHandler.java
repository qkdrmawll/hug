package com.qkdrmawll.hug.common;

import com.qkdrmawll.hug.common.dto.CommonErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.EntityNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<CommonErrorDto> EntityNotFoundExceptionHandler (EntityNotFoundException e) {
        e.printStackTrace();
        return new ResponseEntity<>(new CommonErrorDto(HttpStatus.BAD_REQUEST, e.getMessage()),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<CommonErrorDto> IllegalArgumentExceptionHandler (IllegalArgumentException e) {
        e.printStackTrace();
        return new ResponseEntity<>(new CommonErrorDto(HttpStatus.BAD_REQUEST, e.getMessage()),HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CommonErrorDto> MethodArgumentNotValidExceptionHandler (MethodArgumentNotValidException e) {
        e.printStackTrace();
        return new ResponseEntity<>(new CommonErrorDto(HttpStatus.BAD_REQUEST, e.getMessage()),HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<CommonErrorDto> exceptionHandler (Exception e) {
        e.printStackTrace();
        return new ResponseEntity<>(new CommonErrorDto(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
