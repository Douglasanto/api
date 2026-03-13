package com.apiIc.api.controllers.exceptions;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.apiIc.api.dto.ApiResponse;
import com.apiIc.api.services.execptions.ResourceNotFoundException;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ResourceExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Map<String, Object>>> resourceNotFound(ResourceNotFoundException e, HttpServletRequest request) {
        return buildError(HttpStatus.NOT_FOUND, e.getMessage(), request);
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, Object>>> validation(MethodArgumentNotValidException e, HttpServletRequest request) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError x : e.getBindingResult().getFieldErrors()) {
            errors.put(x.getField(), x.getDefaultMessage());
        }

        ApiResponse<Map<String, Object>> r = ApiResponse.error("Validation error");
        r.setMessage("Validation error");

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("timestamp", Instant.now().toString());
        data.put("path", request.getRequestURI());
        data.put("errors", errors);
        r.setData(data);

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(r);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<Map<String, Object>>> dataIntegrity(DataIntegrityViolationException e, HttpServletRequest request) {
        String msg = "Violação de integridade de dados";
        return buildError(HttpStatus.CONFLICT, msg, request);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Map<String, Object>>> accessDenied(AccessDeniedException e, HttpServletRequest request) {
        String msg = "Acesso negado";
        return buildError(HttpStatus.FORBIDDEN, msg, request);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse<Map<String, Object>>> authentication(AuthenticationException e, HttpServletRequest request) {
        String msg = "Não autenticado";
        return buildError(HttpStatus.UNAUTHORIZED, msg, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Map<String, Object>>> fallback(Exception e, HttpServletRequest request) {
        String msg = "Erro interno do servidor";
        return buildError(HttpStatus.INTERNAL_SERVER_ERROR, msg, request);
    }

    private ResponseEntity<ApiResponse<Map<String, Object>>> buildError(HttpStatus status, String msg, HttpServletRequest request) {
        ApiResponse<Map<String, Object>> r = ApiResponse.error(msg);
        r.setMessage(msg);

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("timestamp", Instant.now().toString());
        data.put("path", request.getRequestURI());
        r.setData(data);

        return ResponseEntity.status(status).body(r);
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
