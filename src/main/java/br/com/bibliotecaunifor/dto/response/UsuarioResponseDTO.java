package br.com.bibliotecaunifor.dto.response;

public class UsuarioResponseDTO {
    // Atributos
    private Long id;
    private String nomeCompleto;
    private int matricula;
    private String email;
    private String tipoUsuario;

    // Construtor com parâmetros
    public UsuarioResponseDTO(Long id, String nomeCompleto, int matricula, String email, String tipoUsuario) {
        this.id = id;
        this.nomeCompleto = nomeCompleto;
        this.matricula = matricula;
        this.email = email;
        this.tipoUsuario = tipoUsuario;
    }

    // Getters (imutabilidade)
    public Long getId() { return id; }
    public String getNomeCompleto() { return nomeCompleto; }
    public int getMatricula() { return matricula; }
    public String getEmail() { return email; }
    public String getTipoUsuario() { return tipoUsuario; }

}
