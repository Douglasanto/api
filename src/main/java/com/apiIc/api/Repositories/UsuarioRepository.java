package com.apiIc.api.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.apiIc.api.entities.Usuario;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
    boolean existsByEmail(String email);
}
