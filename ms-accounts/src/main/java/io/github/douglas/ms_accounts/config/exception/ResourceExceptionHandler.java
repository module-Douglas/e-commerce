package io.github.douglas.ms_accounts.config.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.kafka.common.errors.AuthenticationException;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class ResourceExceptionHandler {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<?> handleValidationException(ValidationException e, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new StandardError(
                        request.getRequestURI(),
                        HttpStatus.BAD_REQUEST.value(),
                        e.getMessage(),
                        LocalDateTime.now()
                ));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleDataIntegrityViolationException(DataIntegrityViolationException e, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(new StandardError(
                        request.getRequestURI(),
                        HttpStatus.UNPROCESSABLE_ENTITY.value(),
                        e.getMessage(),
                        LocalDateTime.now()
                ));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException e, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new StandardError(
                        request.getRequestURI(),
                        HttpStatus.NOT_FOUND.value(),
                        e.getMessage(),
                        LocalDateTime.now()
                ));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<?> handleAuthenticationException(AuthenticationException e, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new StandardError(
                        request.getRequestURI(),
                        HttpStatus.BAD_REQUEST.value(),
                        e.getMessage(),
                        LocalDateTime.now()
                ));
    }

}
