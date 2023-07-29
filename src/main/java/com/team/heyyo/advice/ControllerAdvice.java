package com.team.heyyo.advice;

import com.team.heyyo.auth.exception.AuthorizationException;
import com.team.heyyo.auth.jwt.exception.TokenForgeryException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentException(BindingResult bindingResult) {
        String errorMessage = bindingResult.getFieldErrors()
                .get(0)
                .getDefaultMessage();
        return ResponseEntity.badRequest().body(ErrorResponse.of(errorMessage));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse.of(e.getMessage()));
    }

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedException(AuthorizationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorResponse.of(e.getMessage()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException e) {
        return ResponseEntity.internalServerError().body(ErrorResponse.of(e.getMessage()));
    }

    @ExceptionHandler(TokenForgeryException.class)
    public ResponseEntity<ErrorResponse> handleTokenForgeryException(TokenForgeryException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ErrorResponse.of(e.getMessage()));
    }

}