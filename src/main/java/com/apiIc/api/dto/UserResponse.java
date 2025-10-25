package com.apiIc.api.dto;

public class UserResponse {
    private Long id;
    private String email;
    private String nome;
    private String token;
    private Double latitude;
    private Double longitude;

    // Construtores
    public UserResponse() {
    }

    public UserResponse(Long id, String email, String nome, String token) {
        this.id = id;
        this.email = email;
        this.nome = nome;
        this.token = token;
    }

    public UserResponse(Long id, String email, String nome, String token, Double latitude, Double longitude) {
        this.id = id;
        this.email = email;
        this.nome = nome;
        this.token = token;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
