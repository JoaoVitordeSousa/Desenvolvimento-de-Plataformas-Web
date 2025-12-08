package br.com.bibliotecaunifor.dto.response;

public class LoginResponseDTO {
    
    private String mensagem;
    private String token;
    private UsuarioResponseDTO usuario;

    public LoginResponseDTO(String mensagem, String token, UsuarioResponseDTO usuario) {
        this.mensagem = mensagem;
        this.token = token;
        this.usuario = usuario;
    }

    public String getMensagem() { return mensagem; }
    public String getToken() { return token; }
    public UsuarioResponseDTO getUsuario() { return usuario; }
}
