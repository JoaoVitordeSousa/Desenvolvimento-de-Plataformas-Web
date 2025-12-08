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
                // Endpoints de usuários
                String usuariosBasePath = "/api/v1/usuarios";
                String[] usuariosEndpoints = {
                        "/cadastro",
                        "/login",
                        "/{matricula}",
                        "/redefinir-senha"
                };

                for (String endpoint : usuariosEndpoints) {
                    registry.addMapping(usuariosBasePath + endpoint)
                            .allowedOrigins("http://127.0.0.1:5500")
                            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                            .allowedHeaders("Content-Type", "Authorization")
                            .exposedHeaders("Authorization")
                            .allowCredentials(true);
                }

                // Endpoints de livros
                String livrosBasePath = "/api/v1/livros";
                String[] livrosEndpoints = {
                        "",
                        "/",
                        "/{codigoInterno}"
                };

                for (String endpoint : livrosEndpoints) {
                    registry.addMapping(livrosBasePath + endpoint)
                            .allowedOrigins("http://127.0.0.1:5500")
                            .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                            .allowedHeaders("Content-Type", "Authorization")
                            .exposedHeaders("Authorization")
                            .allowCredentials(true);
                }

                // Endpoints de aluguéis
                String alugueisBasePath = "/api/v1/alugueis";
                String[] alugueisEndpoints = {
                        "",
                        "/{id}",
                        "/meus",
                        "/{id}/devolucao",
                        "/{id}/status"
                };

                for (String endpoint : alugueisEndpoints) {
                    registry.addMapping(alugueisBasePath + endpoint)
                            .allowedOrigins("http://127.0.0.1:5500")
                            .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                            .allowedHeaders("Content-Type", "Authorization")
                            .exposedHeaders("Authorization")
                            .allowCredentials(true);
                }
            }
        };
    }

}