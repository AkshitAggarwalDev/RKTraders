package RKTraders.web.Exceptions;

import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex) {

        ErrorResponse error = new ErrorResponse();

        error.setSuccess(false);
        error.setMessage(ex.getMessage());
        error.setStatus(HttpStatus.NOT_FOUND.value());
        error.setTimestamp(LocalDateTime.now());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(BadRequestException ex) {

        ErrorResponse error = new ErrorResponse();

        error.setSuccess(false);
        error.setMessage(ex.getMessage());
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setTimestamp(LocalDateTime.now());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateResource(DuplicateResourceException ex) {

        ErrorResponse error = new ErrorResponse();

        error.setSuccess(false);
        error.setMessage(ex.getMessage());
        error.setStatus(HttpStatus.CONFLICT.value());
        error.setTimestamp(LocalDateTime.now());

        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorized(UnauthorizedException ex) {

        ErrorResponse error = new ErrorResponse();

        error.setSuccess(false);
        error.setMessage(ex.getMessage());
        error.setStatus(HttpStatus.UNAUTHORIZED.value());
        error.setTimestamp(LocalDateTime.now());

        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorResponse> handleForbidden(ForbiddenException ex) {

        ErrorResponse error = new ErrorResponse();

        error.setSuccess(false);
        error.setMessage(ex.getMessage());
        error.setStatus(HttpStatus.FORBIDDEN.value());
        error.setTimestamp(LocalDateTime.now());

        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }
}
