package br.com.bibliotecaunifor.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UsuarioRequestDTO {
    // Atributos com validação
    @NotBlank(message = "Nome completo é obrigatório")
    @Size(min = 3, max = 200, message = "Nome deve ter entre 3 e 200 caracteres")
    private final String nomeCompleto;

    @NotNull(message = "Matrícula é obrigatória")
    private final Integer matricula;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Formato de email inválido")
    private final String email;

    @NotBlank(message = "Senha é obrigatória")
    @Size(min = 8, message = "Senha deve no minimo 8 caracteres")
    @Pattern(
        regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&]).+$",
        message = "Senha deve conter letra maiúscula, minúscula, número e caractere especial"
    )
    private final String senha;

    // Construtor com parâmetros
    public UsuarioRequestDTO(String nomeCompleto, Integer matricula, String email, String senha) {
        this.nomeCompleto = nomeCompleto;
        this.matricula = matricula;
        this.email = email;
        this.senha = senha;
    }

    // Getters (imutabilidade)
    public String getNomeCompleto() { return nomeCompleto; }
    public Integer getMatricula() { return matricula; }
    public String getEmail() { return email; }
    public String getSenha() { return senha; }

}
