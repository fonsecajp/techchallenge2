package br.com.fiap.techchallenge2.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.time.Instant;

@ControllerAdvice
public class ExceptionHandler {

    private StandardError error = new StandardError();

    @org.springframework.web.bind.annotation.ExceptionHandler(NotFoundException.class)
    public ResponseEntity<StandardError> entityNotFound(NotFoundException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        return getStandardErrorResponseEntity(request, status, e.getMessage(), e);
    }

    private ResponseEntity<StandardError> getStandardErrorResponseEntity(HttpServletRequest request, HttpStatus status, String message, Exception e) {
        error.setTimestamp(Instant.now());
        error.setStatus(status.value());
        error.setError(message);
        error.setMessage(message);
        error.setPath(request.getRequestURI());
        return ResponseEntity.status(status).body(this.error);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(ConflictException.class)
    public ResponseEntity<StandardError> conflict(ConflictException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.CONFLICT;
        return getStandardErrorResponseEntity(request, status, e.getMessage(), e);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandardError> validation(MethodArgumentNotValidException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ValidateError validateError = new ValidateError();

        validateError.setTimestamp(Instant.now());
        validateError.setStatus(status.value());
        validateError.setError("Erro de validação.");
        validateError.setMessage(e.getMessage());
        validateError.setPath(request.getRequestURI());

        for (FieldError f: e.getBindingResult().getFieldErrors()) {
            validateError.addMensagens(f.getField(), f.getDefaultMessage());
        };

        return ResponseEntity.status(status).body(validateError);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(OutdatedException.class)
    public ResponseEntity<StandardError> outdated(OutdatedException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return getStandardErrorResponseEntity(request, status, e.getMessage(), e);
    }

}
