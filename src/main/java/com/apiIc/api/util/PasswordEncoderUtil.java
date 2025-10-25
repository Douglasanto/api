package com.apiIc.api.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordEncoderUtil {
    
    private static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    public static String encode(String password) {
        return PASSWORD_ENCODER.encode(password);
    }

    public static boolean matches(String password, String encodedPassword) {
        return PASSWORD_ENCODER.matches(password, encodedPassword);
    }
}
