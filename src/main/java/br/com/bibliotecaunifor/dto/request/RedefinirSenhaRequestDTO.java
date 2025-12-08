package br.com.bibliotecaunifor.dto.request;

public class RedefinirSenhaRequestDTO {
    // Atributos
    private String nomeCompleto;
    private int matricula;
    private String email;
    private String novaSenha;

    // Construtor padrão
    public RedefinirSenhaRequestDTO() {
    }

    // Getters (imutabilidade)
    public String getNomeCompleto() { return nomeCompleto; }

    public int getMatricula() { return matricula; }

    public String getEmail() { return email; }

    public String getNovaSenha() { return novaSenha; }

}

