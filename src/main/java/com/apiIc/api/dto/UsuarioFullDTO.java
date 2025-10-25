package com.apiIc.api.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonAlias;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UsuarioFullDTO {
    // Básico
    private String nome;
    private String nome_social;
    private String cpf;            // esperado formatado: 000.000.000-00
    private String data_nascimento; // esperado: dd/MM/yyyy
    private String email;
    private String senha;
    private Long telefone;
    private Long telefone_contato;
    private Double latitude;
    private Double longitude;
    private String plusCode;

    // Enums (usar nomes conforme enums da API)
    private String cor;             // Cor
    private String escolaridade;    // Escolaridade
    private String renda_mensal;    // Renda_mensal
    private String sexo;            // Sexo
    private String tipo_moradia;    // Tipo_Moradia
    private String estado_civil;    // Estado_civil

    // Associações
    // Deficiências por IDs (mantido)
    @JsonAlias({"defs","deficiencias","deficienciaIds"})
    private List<Long> deficienciaIds;
    // Benefícios e Profissões como objetos aninhados
    private List<BeneficioDTO> bens;
    private List<ProfissaoDTO> profs;

    // Endereços embutidos
    private List<EnderecoDTO> enderecos;

    // Suporte a payloads aninhados do frontend
    private SocioEconomicScreen socioEconomicScreen;

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getNome_social() { return nome_social; }
    public void setNome_social(String nome_social) { this.nome_social = nome_social; }
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public String getData_nascimento() { return data_nascimento; }
    public void setData_nascimento(String data_nascimento) { this.data_nascimento = data_nascimento; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
    public Long getTelefone() { return telefone; }
    public void setTelefone(Long telefone) { this.telefone = telefone; }
    public Long getTelefone_contato() { return telefone_contato; }
    public void setTelefone_contato(Long telefone_contato) { this.telefone_contato = telefone_contato; }
    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
    public String getPlusCode() { return plusCode; }
    public void setPlusCode(String plusCode) { this.plusCode = plusCode; }
    public String getCor() { return cor; }
    public void setCor(String cor) { this.cor = cor; }
    public String getEscolaridade() { return escolaridade; }
    public void setEscolaridade(String escolaridade) { this.escolaridade = escolaridade; }
    public String getRenda_mensal() { return renda_mensal; }
    public void setRenda_mensal(String renda_mensal) { this.renda_mensal = renda_mensal; }
    public String getSexo() { return sexo; }
    public void setSexo(String sexo) { this.sexo = sexo; }
    public String getTipo_moradia() { return tipo_moradia; }
    public void setTipo_moradia(String tipo_moradia) { this.tipo_moradia = tipo_moradia; }
    public String getEstado_civil() { return estado_civil; }
    public void setEstado_civil(String estado_civil) { this.estado_civil = estado_civil; }
    public List<Long> getDeficienciaIds() { return deficienciaIds; }
    public void setDeficienciaIds(List<Long> deficienciaIds) { this.deficienciaIds = deficienciaIds; }
    public List<BeneficioDTO> getBens() { return bens; }
    public void setBens(List<BeneficioDTO> bens) { this.bens = bens; }
    public List<ProfissaoDTO> getProfs() { return profs; }
    public void setProfs(List<ProfissaoDTO> profs) { this.profs = profs; }
    public List<EnderecoDTO> getEnderecos() { return enderecos; }
    public void setEnderecos(List<EnderecoDTO> enderecos) { this.enderecos = enderecos; }
    public SocioEconomicScreen getSocioEconomicScreen() { return socioEconomicScreen; }
    public void setSocioEconomicScreen(SocioEconomicScreen socioEconomicScreen) { this.socioEconomicScreen = socioEconomicScreen; }

    // DTO aninhado para compatibilidade com o frontend
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SocioEconomicScreen {
        private String cor;
        private String escolaridade;
        private String renda_mensal;
        private String sexo;
        private String tipo_moradia;
        private String estado_civil;
        @JsonAlias({"defs","deficiencias","deficienciaIds"})
        private List<Long> deficienciaIds;

        public String getCor() { return cor; }
        public void setCor(String cor) { this.cor = cor; }
        public String getEscolaridade() { return escolaridade; }
        public void setEscolaridade(String escolaridade) { this.escolaridade = escolaridade; }
        public String getRenda_mensal() { return renda_mensal; }
        public void setRenda_mensal(String renda_mensal) { this.renda_mensal = renda_mensal; }
        public String getSexo() { return sexo; }
        public void setSexo(String sexo) { this.sexo = sexo; }
        public String getTipo_moradia() { return tipo_moradia; }
        public String getEstado_civil() { return estado_civil; }
        public void setEstado_civil(String estado_civil) { this.estado_civil = estado_civil; }
        public List<Long> getDeficienciaIds() { return deficienciaIds; }
        public void setDeficienciaIds(List<Long> deficienciaIds) { this.deficienciaIds = deficienciaIds; }
    }
}
