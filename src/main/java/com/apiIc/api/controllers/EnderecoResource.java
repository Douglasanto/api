package com.apiIc.api.controllers;

import java.net.URI;
import java.util.List;

 import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
import com.apiIc.api.entities.Endereco;
import com.apiIc.api.services.EnderecoService;


@RestController
@RequestMapping(value = "/endereco")
public class EnderecoResource {
	
	@Autowired
	private EnderecoService service;
	
	@GetMapping
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<ApiResponse<List<Endereco>>> findAll() {

		List<Endereco> list = service.findAll();
		return ResponseEntity.ok().body(ApiResponse.success(list));
	}
	
	@GetMapping(value = "/{id}")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<ApiResponse<Endereco>> findById(@PathVariable Long id){
	Endereco obj = service.findByiD(id);
	return ResponseEntity.ok().body(ApiResponse.success(obj));
	}
	
	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<Endereco>> insert(@RequestBody Endereco obj){
		obj = service.insert(obj);
		 URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId_endereco()).toUri();
		 return ResponseEntity.created(uri).body(ApiResponse.success(obj));
	}
	
	@DeleteMapping(value = "/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id){
		service.delete(id);
		return ResponseEntity.ok().body(ApiResponse.success(null));
	}
	
	@PutMapping(value = "/{id}")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<ApiResponse<Endereco>> update(@PathVariable Long id, @RequestBody Endereco obj) {
		
		obj = service.update(id, obj);
		return ResponseEntity.ok().body(ApiResponse.success(obj));
	}

}
