package com.apiIc.api.dto;

public class NearbyPlaceDTO {
    private String nome;
    private String endereco;
    private Double latitude;
    private Double longitude;

    public NearbyPlaceDTO() {}

    public NearbyPlaceDTO(String nome, String endereco, Double latitude, Double longitude) {
        this.nome = nome;
        this.endereco = endereco;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
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
