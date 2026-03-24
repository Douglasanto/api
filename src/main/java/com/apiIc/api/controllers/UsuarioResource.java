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
import com.apiIc.api.dto.UserProfileResponse;
import com.apiIc.api.dto.EnderecoDTO;
import com.apiIc.api.entities.Usuario;
import com.apiIc.api.entities.Endereco;
import com.apiIc.api.services.UsuarioService;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping(value = "/usuarios")
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class UsuarioResource {

	@Autowired
	private UsuarioService service;

    private static final Logger log = LoggerFactory.getLogger(UsuarioResource.class);
	
	@GetMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<List<Usuario>>> findAll() {

		List<Usuario> list = service.findAll();
		return ResponseEntity.ok().body(ApiResponse.success(list));
	}
	
	     @Deprecated
     @GetMapping(value = "/api/localizacoes")
	    public ResponseEntity<ApiResponse<List<LocalizacaoDTO>>> obterLocalizacoes() {
        log.warn("[DEPRECATED] Rota /usuarios/api/localizacoes será movida. Utilize /usuarios/localizacoes em GeolocationController.");
        List<Usuario> usuarios = service.findAll();
        List<LocalizacaoDTO> localizacoes = usuarios.stream()
                .map(usuario -> new LocalizacaoDTO(usuario.getLatitude(), usuario.getLongitude()))
                .collect(Collectors.toList());

	    	return ResponseEntity.ok().body(ApiResponse.success(localizacoes));
    }

	
	@GetMapping(value = "/{id}")
	@PreAuthorize("#id == authentication.principal.id_usuario or hasRole('ADMIN')")
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

	@GetMapping(value = "/{id}/perfil")
	@PreAuthorize("#id == authentication.principal.id_usuario or hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<UserProfileResponse>> getPerfilCompleto(@PathVariable Long id) {
		Usuario obj = service.findByiD(id);

		String nascimento = obj.getData_nascimento() != null ? obj.getData_nascimento().toString() : null;
		String sexo = obj.getSexo() != null ? obj.getSexo().name() : null;
		String raca = obj.getCor() != null ? obj.getCor().name() : null;
		String telefone = obj.getTelefone() != 0L ? String.valueOf(obj.getTelefone()) : null;

		java.util.List<EnderecoDTO> enderecos = new java.util.ArrayList<>();
		String enderecoResumo = null;
		if (obj.getEnd() != null && !obj.getEnd().isEmpty()) {
			for (Endereco e : obj.getEnd()) {
				EnderecoDTO ed = new EnderecoDTO();
				ed.setId_endereco(e.getId_endereco());
				ed.setTipo_endereco(e.getTipo_endereco());
				ed.setLogradouro(e.getLogradouro());
				ed.setBairro(e.getBairro());
				ed.setCidade(e.getCidade());
				ed.setEstado(e.getEstado());
				ed.setCep(e.getCep());
				ed.setNumero(e.getNumero());
				ed.setLatitude(e.getLatitude());
				ed.setLongitude(e.getLongitude());
				enderecos.add(ed);
			}

			Endereco first = obj.getEnd().iterator().next();
			enderecoResumo = String.format("%s, %s - %s/%s", 
				first.getLogradouro() != null ? first.getLogradouro() : "",
				first.getNumero() != null ? first.getNumero() : "",
				first.getCidade() != null ? first.getCidade() : "",
				first.getEstado() != null ? first.getEstado() : ""
			).trim();
		}

		UserProfileResponse resp = new UserProfileResponse(
			obj.getId_usuario(),
			obj.getEmail(),
			obj.getNome(),
			obj.getCpf(),
			nascimento,
			sexo,
			raca,
			telefone,
			enderecoResumo,
			obj.getLatitude(),
			obj.getLongitude(),
			enderecos
		);

		return ResponseEntity.ok().body(ApiResponse.success(resp));
	}
	
	    @Deprecated
    @PostMapping
    public ResponseEntity<ApiResponse<Usuario>> insert(@Valid @RequestBody UsuarioDTO objDTO){
         log.warn("[DEPRECATED] Endpoint POST /usuarios será descontinuado. Utilize POST /api/auth/registro.");
		 Usuario obj = service.insert(objDTO);
		 URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId_usuario()).toUri();
		 return ResponseEntity.created(uri).body(ApiResponse.success(obj));
	}
	
	@DeleteMapping(value = "/{id}")
	@PreAuthorize("#id == authentication.principal.id_usuario or hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id){
		service.delete(id);
		return ResponseEntity.ok().body(ApiResponse.success(null));
	}
	
	@PutMapping(value = "/{id}")
	@PreAuthorize("#id == authentication.principal.id_usuario or hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<Usuario>> update(@PathVariable Long id, @RequestBody UsuarioDTO objDTO) {
		
		Usuario obj = service.update(id, objDTO);
		return ResponseEntity.ok().body(ApiResponse.success(obj));
	}
	
}
