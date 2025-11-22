package com.apiIc.api.dto;

import java.util.List;

public class RegisterFullResponse {
    private UserResponse user;
    private List<EnderecoDTO> enderecos;

    public RegisterFullResponse() {}

    public RegisterFullResponse(UserResponse user, List<EnderecoDTO> enderecos) {
        this.user = user;
        this.enderecos = enderecos;
    }

    public UserResponse getUser() { return user; }
    public void setUser(UserResponse user) { this.user = user; }

    public List<EnderecoDTO> getEnderecos() { return enderecos; }
    public void setEnderecos(List<EnderecoDTO> enderecos) { this.enderecos = enderecos; }
}
