package com.apiIc.api.entities;

import com.apiIc.api.entities.enums.PerfilNome;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "perfis")
public class Perfil {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Enumerated(EnumType.STRING)
    private PerfilNome nome;
    
    @JsonIgnore
    @ManyToMany(mappedBy = "perfis")
    private Set<Usuario> usuarios = new HashSet<>();
    
    public Perfil() {
    }
    
    public Perfil(PerfilNome nome) {
        this.nome = nome;
    }
    
    // Getters e Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public PerfilNome getNome() {
        return nome;
    }
    
    public void setNome(PerfilNome nome) {
        this.nome = nome;
    }
    
    public Set<Usuario> getUsuarios() {
        return usuarios;
    }
    
    public void setUsuarios(Set<Usuario> usuarios) {
        this.usuarios = usuarios;
    }
}
