package com.apiIc.api.config;

import com.apiIc.api.entities.Perfil;
import com.apiIc.api.entities.enums.PerfilNome;
import com.apiIc.api.Repositories.PerfilRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initDatabase(PerfilRepository perfilRepository) {
        return args -> {
            // Cria os perfis padrão se não existirem
            for (PerfilNome perfilNome : PerfilNome.values()) {
                if (!perfilRepository.findByNome(perfilNome).isPresent()) {
                    Perfil perfil = new Perfil(perfilNome);
                    perfilRepository.save(perfil);
                }
            }
        };
    }
}
