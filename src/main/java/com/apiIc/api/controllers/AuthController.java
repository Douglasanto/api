package com.apiIc.api.controllers;

import com.apiIc.api.entities.Usuario;
import com.apiIc.api.services.UsuarioService;
import com.apiIc.api.util.JWTUtil;

import lombok.extern.slf4j.Slf4j;

import com.apiIc.api.dto.LoginRequest;
import com.apiIc.api.dto.LoginResponse;
import com.apiIc.api.dto.UserResponse;
import com.apiIc.api.dto.UsuarioDTO;
import com.apiIc.api.dto.UsuarioFullDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTUtil jwtUtil;
    
    @Autowired
    private UsuarioService usuarioService;

    public AuthController() {
        log.info("AuthController inicializado!");
        log.info("Endpoints disponíveis: POST /api/auth/login, POST /api/auth/registro, POST /api/auth/register-full");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            // Autentica o usuário
            var usernamePassword = new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(), 
                loginRequest.getPassword()
            );
            
            // Tenta autenticar
            var auth = authenticationManager.authenticate(usernamePassword);
            
            // Se chegou aqui, a autenticação foi bem-sucedida
            var user = (Usuario) auth.getPrincipal();
            
            // Gera o token JWT
            String token = jwtUtil.generateToken(user.getEmail());

            return ResponseEntity.ok(new LoginResponse(
                    true,
                    "Login bem-sucedido",
                    null,
                    new UserResponse(
                            user.getId_usuario(),
                            user.getEmail(),
                            user.getNome(),
                            token,
                            user.getLatitude(),
                            user.getLongitude()
                    )
            ));

        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponse(false, null, "Email ou senha inválidos", null));
        }
    }
    
    @PostMapping("/registro")
    public ResponseEntity<?> register(@RequestBody UsuarioDTO usuarioDTO) {
        try {
            // Verifica se o email já está em uso
            if (usuarioService.existsByEmail(usuarioDTO.getEmail())) {
                return ResponseEntity
                    .badRequest()
                    .body(new LoginResponse(false, null, "Email já está em uso", null));
            }
            
            // Cria e salva o novo usuário
            Usuario usuario = usuarioService.insert(usuarioDTO);
            
            // Autentica o usuário automaticamente após o registro
            var usernamePassword = new UsernamePasswordAuthenticationToken(
                usuario.getEmail(), 
                usuarioDTO.getSenha()
            );
            
            var auth = authenticationManager.authenticate(usernamePassword);
            var user = (Usuario) auth.getPrincipal();
            
            // Gera o token JWT
            String token = jwtUtil.generateToken(user.getEmail());
            
            return ResponseEntity.ok(new LoginResponse(
                true,
                "Registro realizado com sucesso",
                null,
                new UserResponse(
                    user.getId_usuario(),
                    user.getEmail(),
                    user.getNome(),
                    token,
                    user.getLatitude(),
                    user.getLongitude()
                )
            ));
            
        } catch (Exception e) {
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new LoginResponse(false, null, "Erro ao registrar usuário: " + e.getMessage(), null));
        }
    }

    @PostMapping("/register-full")
    public ResponseEntity<?> registerFull(@RequestBody UsuarioFullDTO usuarioDTO) {
        try {
            if (usuarioService.existsByEmail(usuarioDTO.getEmail())) {
                return ResponseEntity
                    .badRequest()
                    .body(new LoginResponse(false, null, "Email já está em uso", null));
            }

            Usuario usuario = usuarioService.insertFull(usuarioDTO);

            var usernamePassword = new UsernamePasswordAuthenticationToken(
                usuario.getEmail(),
                usuarioDTO.getSenha()
            );

            var auth = authenticationManager.authenticate(usernamePassword);
            var user = (Usuario) auth.getPrincipal();

            String token = jwtUtil.generateToken(user.getEmail());

            return ResponseEntity.ok(new LoginResponse(
                true,
                "Registro completo realizado com sucesso",
                null,
                new UserResponse(
                    user.getId_usuario(),
                    user.getEmail(),
                    user.getNome(),
                    token,
                    user.getLatitude(),
                    user.getLongitude()
                )
            ));
        } catch (Exception e) {
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new LoginResponse(false, null, "Erro ao registrar usuário: " + e.getMessage(), null));
        }
    }
}