package com.apiIc.api.dto;

public class LoginResponse {
    private boolean success;
    private String message;
    private String error;
    private UserResponse user;

    // Construtores
    public LoginResponse() {
    }

    public LoginResponse(boolean success, String message, String error, UserResponse user) {
        this.success = success;
        this.message = message;
        this.error = error;
        this.user = user;
    }

    // Getters e Setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public UserResponse getUser() {
        return user;
    }

    public void setUser(UserResponse user) {
        this.user = user;
    }
}
