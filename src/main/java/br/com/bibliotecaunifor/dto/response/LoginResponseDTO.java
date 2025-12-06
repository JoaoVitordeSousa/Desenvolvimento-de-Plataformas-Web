package br.com.bibliotecaunifor.dto.response;

public class LoginResponseDTO {
    
    private String mensagem;
    private UsuarioResponseDTO usuario;

    public LoginResponseDTO(String mensagem, UsuarioResponseDTO usuario) {
        this.mensagem = mensagem;
        this.usuario = usuario;
    }

    public String getMensagem() { return mensagem; }
    public UsuarioResponseDTO getUsuario() { return usuario; }

}
