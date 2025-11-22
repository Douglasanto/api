package com.apiIc.api.controllers;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.apiIc.api.dto.ApiResponse;
import com.apiIc.api.dto.LocalizacaoDTO;
import com.apiIc.api.dto.UsuarioDTO;
import com.apiIc.api.dto.UserResponse;
import com.apiIc.api.entities.Usuario;
import com.apiIc.api.services.UsuarioService;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping(value = "/usuarios")
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class UsuarioResource {

	@Autowired
	private UsuarioService service;

    private static final Logger log = LoggerFactory.getLogger(UsuarioResource.class);
	
	@GetMapping
	public ResponseEntity<List<Usuario>> findAll() {

		List<Usuario> list = service.findAll();
		return ResponseEntity.ok().body(list);
	}
	
	     @Deprecated
     @GetMapping(value = "/api/localizacoes")
    public ResponseEntity<List<LocalizacaoDTO>> obterLocalizacoes() {
        log.warn("[DEPRECATED] Rota /usuarios/api/localizacoes será movida. Utilize /usuarios/localizacoes em GeolocationController.");
        List<Usuario> usuarios = service.findAll();
        List<LocalizacaoDTO> localizacoes = usuarios.stream()
                .map(usuario -> new LocalizacaoDTO(usuario.getLatitude(), usuario.getLongitude()))
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(localizacoes);
    }

	
	@GetMapping(value = "/{id}")
	public ResponseEntity<ApiResponse<UserResponse>> findById(@PathVariable Long id){
        Usuario obj = service.findByiD(id);
        UserResponse r = new UserResponse(
            obj.getId_usuario(),
            obj.getEmail(),
            obj.getNome(),
            null, // token não é retornado aqui
            obj.getLatitude(),
            obj.getLongitude()
        );
        return ResponseEntity.ok().body(ApiResponse.success(r));
    }
	
	    @Deprecated
    @PostMapping
    public ResponseEntity<Usuario> insert(@Valid @RequestBody UsuarioDTO objDTO){
         log.warn("[DEPRECATED] Endpoint POST /usuarios será descontinuado. Utilize POST /api/auth/registro.");
		 Usuario obj = service.insert(objDTO);
		 URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId_usuario()).toUri();
		 return ResponseEntity.created(uri).body(obj);
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id){
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	@PutMapping(value = "/{id}")
	public ResponseEntity<Usuario> update(@PathVariable Long id, @RequestBody UsuarioDTO objDTO) {
		
		Usuario obj = service.update(id, objDTO);
		return ResponseEntity.ok().body(obj);
	}
	
}
