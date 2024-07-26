package io.github.douglas.api_gateway.config.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ServerWebExchange;

import java.time.LocalDateTime;

@ControllerAdvice
public class ResourceExceptionHandler {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<?> handleValidationException(ValidationException e, ServerWebExchange request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new StandardError(
                        request.getRequest().getPath().toString(),
                        HttpStatus.BAD_REQUEST.value(),
                        e.getMessage(),
                        LocalDateTime.now()
                ));
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<?> handleUnauthorizedException(UnauthorizedException e, ServerWebExchange request) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new StandardError(
                        request.getRequest().getPath().toString(),
                        HttpStatus.UNAUTHORIZED.value(),
                        e.getMessage(),
                        LocalDateTime.now()
                ));
    }

}
