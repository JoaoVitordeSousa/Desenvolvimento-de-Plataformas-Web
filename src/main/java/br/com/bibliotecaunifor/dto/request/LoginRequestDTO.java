package br.com.bibliotecaunifor.dto.request;

public class LoginRequestDTO {
    
    private int matricula;
    private String senha;

    public int getMatricula() { return matricula; }
    public void setMatricula(int matricula) { this.matricula = matricula; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

}
