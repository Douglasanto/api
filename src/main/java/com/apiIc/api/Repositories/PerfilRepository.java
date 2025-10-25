package com.apiIc.api.Repositories;

import com.apiIc.api.entities.Perfil;
import com.apiIc.api.entities.enums.PerfilNome;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PerfilRepository extends JpaRepository<Perfil, Long> {
    Optional<Perfil> findByNome(PerfilNome nome);
}
