package br.com.bibliotecaunifor.dto.response;

import java.time.LocalDate;

import br.com.bibliotecaunifor.enums.StatusReserva;

public class ReservaResponseDTO {

    private Long id;
    private LivroResumoDTO livro;
    private StatusReserva status;
    private LocalDate dataReserva;

    public ReservaResponseDTO(Long id, LivroResumoDTO livro, StatusReserva status, LocalDate dataReserva) {
        this.id = id;
        this.livro = livro;
        this.status = status;
        this.dataReserva = dataReserva;
    }

    public Long getId() {
        return id;
    }

    public LivroResumoDTO getLivro() {
        return livro;
    }

    public StatusReserva getStatus() {
        return status;
    }

    public LocalDate getDataReserva() {
        return dataReserva;
    }

    // 🔹 DTO interno para resumo do livro
    public static class LivroResumoDTO {
        private Long id;
        private String titulo;

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
