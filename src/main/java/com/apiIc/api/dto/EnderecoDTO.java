package com.apiIc.api.dto;

public class EnderecoDTO {
    private Long id_endereco;
    private String tipo_endereco;
    private String logradouro;
    private String bairro;
    private String cidade;
    private String estado;
    private String cep;
    private String numero;
    private Double latitude;
    private Double longitude;

    public Long getId_endereco() {
        return id_endereco;
    }
    public void setId_endereco(Long id_endereco) {
        this.id_endereco = id_endereco;
    }
    public String getTipo_endereco() {
        return tipo_endereco;
    }
    public void setTipo_endereco(String tipo_endereco) {
        this.tipo_endereco = tipo_endereco;
    }
    public String getLogradouro() {
        return logradouro;
    }
    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }
    public String getBairro() {
        return bairro;
    }
    public void setBairro(String bairro) {
        this.bairro = bairro;
    }
    public String getCidade() {
        return cidade;
    }
    public void setCidade(String cidade) {
        this.cidade = cidade;
    }
    public String getEstado() {
        return estado;
    }
    public void setEstado(String estado) {
        this.estado = estado;
    }
    public String getCep() {
        return cep;
    }
    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
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
