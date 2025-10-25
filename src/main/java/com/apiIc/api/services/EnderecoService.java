package com.apiIc.api.services;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.apiIc.api.Repositories.EnderecoRepository;
import com.apiIc.api.entities.Endereco;
import com.apiIc.api.dto.LocalizacaoDTO;

@Service
public class EnderecoService {

	
	@Autowired
	private EnderecoRepository repository;

    @Autowired
    private GoogleMapsService googleMapsService;
	
	public List<Endereco> findAll(){
		return repository.findAll();			
	}
	
	public Endereco findByiD(Long id) {
		Optional<Endereco> obj = repository.findById(id);
		return obj.get();
	}
	
	public Endereco insert(Endereco obj) {
        String enderecoCompleto = String.format("%s, %s, %s - %s, %s",
                safe(obj.getLogradouro()), safe(obj.getNumero()), safe(obj.getCidade()), safe(obj.getEstado()), safe(obj.getCep()));
        try {
            LocalizacaoDTO loc = googleMapsService.getCoordinatesFromAddress(enderecoCompleto);
            obj.setLatitude(loc.getLatitude());
            obj.setLongitude(loc.getLongitude());
        } catch (Exception e) {
            obj.setLatitude(null);
            obj.setLongitude(null);
        }
        return repository.save(obj);
    }
	
	public void delete(Long id) {
		repository.deleteById(id);
	}
	
	public Endereco update(Long id, Endereco obj) {
		Endereco entity = repository.getReferenceById(id);
		updateData(entity,obj);
		return repository.save(entity);
	}
	
	private void updateData(Endereco entity, Endereco obj) {
		
		entity.setCidade(obj.getCidade());
		
	}

    private String safe(String s) { return s == null ? "" : s.trim(); }
}
