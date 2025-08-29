package com.apiIc.api.services;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.apiIc.api.Repositories.UsuarioRepository;
import com.apiIc.api.entities.Beneficio;
import com.apiIc.api.entities.Deficiencia;
import com.apiIc.api.entities.Endereco;
import com.apiIc.api.entities.Profissao;
import com.apiIc.api.entities.Usuario;
import com.apiIc.api.services.execptions.ResourceNotFoundException;

@Service
public class UsuarioService {
	
	@Autowired
	private UsuarioRepository repository;
	
	public List<Usuario> findAll(){
		return repository.findAll();
				
	}
	
	public Usuario findByiD(Long id) {
		Optional<Usuario> obj = repository.findById(id);
		return obj.orElseThrow(() -> new ResourceNotFoundException(id));
	}
	
	
@Transactional
public Usuario insert(Usuario obj) {
    Usuario savedUsuario = repository.save(obj);


    if (savedUsuario.getEnd() != null && !savedUsuario.getEnd().isEmpty()) {
        Set<Endereco> enderecos = new HashSet<>(savedUsuario.getEnd());
        savedUsuario.getEnd().clear();
        for (Endereco endereco : enderecos) {
            savedUsuario.adicionarEndereco(endereco);
        }
    }

    if (savedUsuario.getDefs() != null && !savedUsuario.getDefs().isEmpty()) {
        Set<Deficiencia> deficiencias = new HashSet<>(savedUsuario.getDefs());
        savedUsuario.getDefs().clear();
        for (Deficiencia deficiencia : deficiencias) {
            deficiencia.adicionarUsuario(savedUsuario);
        }
    }

    if (savedUsuario.getBens() != null && !savedUsuario.getBens().isEmpty()) {
        Set<Beneficio> beneficios = new HashSet<>(savedUsuario.getBens());
        savedUsuario.getBens().clear();
        for (Beneficio beneficio : beneficios) {
            beneficio.adicionarUsuario(savedUsuario);
        }
    }

    if (savedUsuario.getProfs() != null && !savedUsuario.getProfs().isEmpty()) {
        Set<Profissao> profissoes = new HashSet<>(savedUsuario.getProfs());
        savedUsuario.getProfs().clear();
        for (Profissao profissao : profissoes) {
            profissao.adicionarUsuario(savedUsuario);
        }
    }

    return repository.save(savedUsuario);
}
	public void delete(Long id) {
		repository.deleteById(id);
	}
	
	public Usuario update(Long id, Usuario obj) {
		Usuario entity = repository.getReferenceById(id);
		updateData(entity,obj);
		return repository.save(entity);
	}

	private void updateData(Usuario entity, Usuario obj) {
		
		entity.setNome(obj.getNome());
		
	}

}
