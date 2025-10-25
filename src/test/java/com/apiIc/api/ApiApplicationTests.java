package com.apiIc.api;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(
    properties = {
        "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration,org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration",
        "spring.main.allow-bean-definition-overriding=true"
    }
)
@ActiveProfiles("test")
class ApiApplicationTests {

    @Test
    void contextLoads() {
        // This test will pass if the application context loads successfully
    }
}
