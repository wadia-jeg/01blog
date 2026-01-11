package com.zone._blog.exceptions;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.HttpRequestMethodNotSupportedException;

import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Not Found Errors
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException exception) {
        ErrorResponse errorResponse = new ErrorResponse(
                exception.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                exception.getMessage(),
                exception.getCause()
        );

        System.out.println(errorResponse.toString());

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    // Method Not Allowed
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handelHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException exception) {
        ErrorResponse errorResponse = new ErrorResponse(
                exception.getMessage(),
                HttpStatus.METHOD_NOT_ALLOWED.value(),
                exception.getMessage(),
                exception.getCause()
        );

        System.out.println(errorResponse.toString());

        return new ResponseEntity<>(errorResponse, HttpStatus.METHOD_NOT_ALLOWED);
    }

    // Bad Resquest Errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handelValidationException(MethodArgumentNotValidException exception) {
        ErrorResponse errorResponse = new ErrorResponse(
                exception.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                exception.getBindingResult()
                        .getFieldErrors()
                        .stream()
                        .map(err -> Map.of("field", err.getField(), "message", err.getDefaultMessage()))
                        .toList(),
                exception.getCause()
        );

        System.out.println(errorResponse.toString());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> constraintViolationException(ConstraintViolationException exception) {
        ErrorResponse errorResponse = new ErrorResponse(
                exception.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                exception.getConstraintViolations()
                        .stream()
                        .map(err -> Map.of("feild", err.getPropertyPath(), "message", err.getMessage()))
                        .toList(),
                exception.getCause()
        );

        System.out.println(errorResponse.toString());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // Internal Server Errors
    @ExceptionHandler(Exception.class)

    public ResponseEntity<ErrorResponse> handelGeneralException(Exception exception) {
        ErrorResponse errorResponse = new ErrorResponse(
                "Sorry, something went wrong",
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                exception.getMessage(),
                exception.getCause()
        );

        System.out.println(errorResponse.toString());

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
