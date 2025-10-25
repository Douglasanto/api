package com.apiIc.api.controllers;

import com.apiIc.api.dto.LoginRequest;
import com.apiIc.api.dto.LoginResponse;
import com.apiIc.api.dto.UsuarioDTO;
import com.apiIc.api.entities.Usuario;
import com.apiIc.api.services.UsuarioService;
import com.apiIc.api.util.JWTUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private UsuarioService usuarioService;

    @MockBean
    private JWTUtil jwtUtil;
    
    private Usuario testUser;
    private UsuarioDTO testUserDTO;
    
    @BeforeEach
    void setUp() {
        testUser = new Usuario();
        testUser.setId_usuario(1L);
        testUser.setEmail("test@example.com");
        testUser.setSenha("$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBpwTTq6YdYUaO"); // Encoded password
        testUser.setNome("Test User");
        testUser.setCpf("123.456.789-09");
        
        testUserDTO = new UsuarioDTO();
        testUserDTO.setNome("Test User");
        testUserDTO.setEmail("test@example.com");
        testUserDTO.setSenha("password123");
        testUserDTO.setCpf("123.456.789-09");
    }

    @Test
    public void testLoginSuccess() throws Exception {
        // Arrange
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password123");

        // Create authentication with the actual user object
        Authentication auth = new UsernamePasswordAuthenticationToken(
            testUser,
            null,
            testUser.getAuthorities()
        );
        
        when(authenticationManager.authenticate(any(Authentication.class)))
            .thenReturn(auth);
            
        when(jwtUtil.generateToken(anyString()))
            .thenReturn("test-jwt-token");

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Login bem-sucedido"))
                .andExpect(jsonPath("$.error").doesNotExist())
                .andExpect(jsonPath("$.user.id").value(testUser.getId_usuario()))
                .andExpect(jsonPath("$.user.email").value(testUser.getEmail()))
                .andExpect(jsonPath("$.user.nome").value(testUser.getNome()))
                .andExpect(jsonPath("$.user.token").exists());
                
        verify(authenticationManager, times(1)).authenticate(any(Authentication.class));
        verify(jwtUtil, times(1)).generateToken(anyString());
    }

    @Test
    public void testRegisterSuccess() throws Exception {
        // Arrange
        when(usuarioService.existsByEmail(anyString())).thenReturn(false);
        when(usuarioService.insert(any(UsuarioDTO.class))).thenReturn(testUser);
        when(jwtUtil.generateToken(anyString())).thenReturn("test-jwt-token");
        
        // Mock authentication with the actual user object
        Authentication auth = new UsernamePasswordAuthenticationToken(
            testUser,
            null,
            testUser.getAuthorities()
        );
        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(auth);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/auth/registro")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUserDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Registro realizado com sucesso"))
                .andExpect(jsonPath("$.error").doesNotExist())
                .andExpect(jsonPath("$.user.id").value(testUser.getId_usuario()))
                .andExpect(jsonPath("$.user.email").value(testUser.getEmail()))
                .andExpect(jsonPath("$.user.nome").value(testUser.getNome()))
                .andExpect(jsonPath("$.user.token").exists());
                
        verify(usuarioService, times(1)).existsByEmail(anyString());
        verify(usuarioService, times(1)).insert(any(UsuarioDTO.class));
        verify(jwtUtil, times(1)).generateToken(anyString());
    }
}
