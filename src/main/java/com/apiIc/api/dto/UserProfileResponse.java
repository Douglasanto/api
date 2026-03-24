package com.apiIc.api.dto;

import java.util.List;

public class UserProfileResponse {
    private Long id;
    private String email;
    private String nome;
    private String cpf;
    private String nascimento;
    private String sexo;
    private String raca;
    private String telefone;
    private String endereco;
    private Double latitude;
    private Double longitude;
    private List<EnderecoDTO> enderecos;

    public UserProfileResponse() {}

    public UserProfileResponse(Long id, String email, String nome, String cpf, String nascimento, String sexo, String raca,
                               String telefone, String endereco, Double latitude, Double longitude, List<EnderecoDTO> enderecos) {
        this.id = id;
        this.email = email;
        this.nome = nome;
        this.cpf = cpf;
        this.nascimento = nascimento;
        this.sexo = sexo;
        this.raca = raca;
        this.telefone = telefone;
        this.endereco = endereco;
        this.latitude = latitude;
        this.longitude = longitude;
        this.enderecos = enderecos;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public String getNascimento() { return nascimento; }
    public void setNascimento(String nascimento) { this.nascimento = nascimento; }
    public String getSexo() { return sexo; }
    public void setSexo(String sexo) { this.sexo = sexo; }
    public String getRaca() { return raca; }
    public void setRaca(String raca) { this.raca = raca; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }
    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
    public List<EnderecoDTO> getEnderecos() { return enderecos; }
    public void setEnderecos(List<EnderecoDTO> enderecos) { this.enderecos = enderecos; }
}
