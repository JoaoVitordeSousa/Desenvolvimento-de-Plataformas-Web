package br.com.bibliotecaunifor.dto.request;

import jakarta.validation.constraints.NotNull;

public class ReservaRequestDTO {

    @NotNull(message = "O ID do livro é obrigatório")
    private Long livroId;

    public ReservaRequestDTO() {}

    public ReservaRequestDTO(Long livroId) {
        this.livroId = livroId;
    }

    public Long getLivroId() {
        return livroId;
    }

    public void setLivroId(Long livroId) {
        this.livroId = livroId;
    }
}

