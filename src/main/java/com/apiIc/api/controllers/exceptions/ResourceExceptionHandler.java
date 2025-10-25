package com.apiIc.api.controllers.exceptions;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.apiIc.api.services.execptions.ResourceNotFoundException;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ResourceExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<StandardError> resourceNotFound(ResourceNotFoundException e, HttpServletRequest request) {
        String error = "Resource not found";
        HttpStatus status = HttpStatus.NOT_FOUND;
        StandardError err = new StandardError(Instant.now(), status.value(), error, e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationError> validation(MethodArgumentNotValidException e, HttpServletRequest request) {
        String error = "Validation error";
        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
        ValidationError err = new ValidationError(Instant.now(), status.value(), error, error, request.getRequestURI());
        
        for (FieldError x : e.getBindingResult().getFieldErrors()) {
            err.addError(x.getField(), x.getDefaultMessage());
        }
        
        return ResponseEntity.status(status).body(err);
    }
    
    public static class StandardError {
        private Instant timestamp;
        private Integer status;
        private String error;
        private String message;
        private String path;
        
        public StandardError(Instant timestamp, Integer status, String error, String message, String path) {
            this.timestamp = timestamp;
            this.status = status;
            this.error = error;
            this.message = message;
            this.path = path;
        }
        
        // Getters e Setters
        public Instant getTimestamp() {
            return timestamp;
        }
        
        public void setTimestamp(Instant timestamp) {
            this.timestamp = timestamp;
        }
        
        public Integer getStatus() {
            return status;
        }
        
        public void setStatus(Integer status) {
            this.status = status;
        }
        
        public String getError() {
            return error;
        }
        
        public void setError(String error) {
            this.error = error;
        }
        
        public String getMessage() {
            return message;
        }
        
        public void setMessage(String message) {
            this.message = message;
        }
        
        public String getPath() {
            return path;
        }
        
        public void setPath(String path) {
            this.path = path;
        }
    }
    
    public static class ValidationError extends StandardError {
        private static final long serialVersionUID = 1L;
        
        private Map<String, String> errors = new HashMap<>();
        
        public ValidationError(Instant timestamp, Integer status, String error, String message, String path) {
            super(timestamp, status, error, message, path);
        }
        
        public Map<String, String> getErrors() {
            return errors;
        }
        
        public void addError(String fieldName, String message) {
            errors.put(fieldName, message);
        }
    }
}
