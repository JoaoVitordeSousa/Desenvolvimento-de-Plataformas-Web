package br.com.bibliotecaunifor.dto.response;

import java.time.LocalDate;

import br.com.bibliotecaunifor.enums.StatusAluguel;

public class AluguelResponseDTO {
    private final Long id;
    private final LivroResumoDTO livro;
    private final StatusAluguel status;
    private final LocalDate dataInicio;
    private final LocalDate dataFim;

    public AluguelResponseDTO(Long id, LivroResumoDTO livro,
                              StatusAluguel status,
                              LocalDate dataInicio,
                              LocalDate dataFim) {
        this.id = id;
        this.livro = livro;
        this.status = status;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
    }

    public Long getId() {
        return id;
    }

    public LivroResumoDTO getLivro() {
        return livro;
    }

    public StatusAluguel getStatus() {
        return status;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    // DTO interno para simplificar dados do livro
    public static class LivroResumoDTO {
        private final Long id;
        private final String titulo;

        public LivroResumoDTO(Long id, String titulo) {
            this.id = id;
            this.titulo = titulo;
        }

        public Long getId() {
            return id;
        }

        public String getTitulo() {
            return titulo;
        }
    }
}