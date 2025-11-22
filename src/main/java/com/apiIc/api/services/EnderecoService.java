package com.apiIc.api.services;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.apiIc.api.Repositories.EnderecoRepository;
import com.apiIc.api.entities.Endereco;
import com.apiIc.api.dto.LocalizacaoDTO;
import com.apiIc.api.dto.EnderecoDTO;

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
        geocodeAndFill(obj);
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

    // Centralização de geocodificação
    public void geocodeAndFill(Endereco end) {
        String enderecoCompleto = String.format("%s, %s, %s - %s, %s",
                safe(end.getLogradouro()), safe(end.getNumero()), safe(end.getCidade()), safe(end.getEstado()), safe(end.getCep()));
        try {
            LocalizacaoDTO loc = googleMapsService.getCoordinatesFromAddress(enderecoCompleto);
            end.setLatitude(loc.getLatitude());
            end.setLongitude(loc.getLongitude());
        } catch (Exception e) {
            end.setLatitude(null);
            end.setLongitude(null);
        }
    }

    public Endereco geocodeAndBuildFromDTO(EnderecoDTO e) {
        Endereco end = new Endereco();
        end.setTipo_endereco(e.getTipo_endereco());
        end.setLogradouro(e.getLogradouro());
        end.setBairro(e.getBairro());
        end.setCidade(e.getCidade());
        end.setEstado(e.getEstado());
        end.setCep(e.getCep());
        end.setNumero(e.getNumero());
        geocodeAndFill(end);
        return end;
    }

    // Versões strict: não engolem exceções, deixam propagar para o chamador decidir (400 x 502)
    public void geocodeAndFillStrict(Endereco end) {
        String enderecoCompleto = String.format("%s, %s, %s - %s, %s",
                safe(end.getLogradouro()), safe(end.getNumero()), safe(end.getCidade()), safe(end.getEstado()), safe(end.getCep()));
        LocalizacaoDTO loc = googleMapsService.getCoordinatesFromAddress(enderecoCompleto);
        end.setLatitude(loc.getLatitude());
        end.setLongitude(loc.getLongitude());
    }

    public Endereco geocodeAndBuildFromDTOStrict(EnderecoDTO e) {
        Endereco end = new Endereco();
        end.setTipo_endereco(e.getTipo_endereco());
        end.setLogradouro(e.getLogradouro());
        end.setBairro(e.getBairro());
        end.setCidade(e.getCidade());
        end.setEstado(e.getEstado());
        end.setCep(e.getCep());
        end.setNumero(e.getNumero());
        geocodeAndFillStrict(end);
        return end;
    }
}
