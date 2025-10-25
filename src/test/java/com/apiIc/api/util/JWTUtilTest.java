package com.apiIc.api.util;

import io.jsonwebtoken.ExpiredJwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JWTUtilTest {

    @InjectMocks
    private JWTUtil jwtUtil;

    private final String secret = "test-secret-1234567890-1234567890-1234567890-1234567890-1234567890";
    private final long expiration = 3600; // 1 hour

    @BeforeEach
    public void setup() {
        ReflectionTestUtils.setField(jwtUtil, "secret", secret);
        ReflectionTestUtils.setField(jwtUtil, "expiration", expiration);
    }

    @Test
    void testGenerateAndValidateToken() {
        // Arrange
        String username = "testuser@example.com";

        // Act
        String token = jwtUtil.generateToken(username);
        
        // Assert
        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertEquals(username, jwtUtil.getUsernameFromToken(token));
        assertTrue(jwtUtil.validateToken(token));
    }

    @Test
    void testValidateToken_InvalidToken() {
        // Arrange
        String invalidToken = "invalid.token.here";

        // Act & Assert
        assertFalse(jwtUtil.validateToken(invalidToken));
    }

    @Test
    void testGetUsernameFromToken() {
        // Arrange
        String username = "testuser@example.com";
        String token = jwtUtil.generateToken(username);

        // Act
        String extractedUsername = jwtUtil.getUsernameFromToken(token);

        // Assert
        assertEquals(username, extractedUsername);
    }

    @Test
    void testTokenExpiration() throws InterruptedException {
        // Arrange
        String username = "testuser@example.com";
        
        // Set a very short expiration (1 second) for testing
        ReflectionTestUtils.setField(jwtUtil, "expiration", 1L);
        String token = jwtUtil.generateToken(username);
        
        // Wait for token to expire
        Thread.sleep(2000);
        
        // Act & Assert
        assertThrows(ExpiredJwtException.class, () -> jwtUtil.getUsernameFromToken(token));
        assertFalse(jwtUtil.validateToken(token));
    }
}
