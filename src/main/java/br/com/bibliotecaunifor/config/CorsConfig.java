package br.com.bibliotecaunifor.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                String basePath = "/api/v1/usuarios";
                String[] endpoints = {
                        "/cadastro",
                        "/login"
                };

                for (String endpoint : endpoints) {
                    registry.addMapping(basePath + endpoint)
                            .allowedOrigins("http://127.0.0.1:5500")
                            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                            .allowedHeaders("Content-Type", "Authorization") // apenas os headers usados
                            .exposedHeaders("Authorization") // permite que o front leia o header Authorization da resposta
                            .allowCredentials(true); // necessário se usar cookies ou tokens
                }
            }
        };
    }

}