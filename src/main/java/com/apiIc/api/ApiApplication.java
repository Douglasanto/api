package com.apiIc.api;

import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@Slf4j
@SpringBootApplication
public class ApiApplication {

    public static void main(String[] args) {
        log.info("Iniciando aplicação...");
        ApplicationContext ctx = SpringApplication.run(ApiApplication.class, args);
        log.info("Aplicação iniciada!");

        
    }

    @Bean
    @Profile("dev")
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {
            log.info("\n=== Endpoints Mapeados ===");
            RequestMappingHandlerMapping mapping = ctx.getBean(RequestMappingHandlerMapping.class);
            mapping.getHandlerMethods().forEach((key, value) -> {
                key.getPatternValues().forEach(pattern -> {
                    log.info("{} {} -> {}.{}()", 
                        key.getMethodsCondition().getMethods(),
                        pattern,
                        value.getBeanType().getName(),
                        value.getMethod().getName()
                    );
                });
            });
        };
    }
}