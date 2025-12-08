package br.com.bibliotecaunifor.dto.request;

import java.time.LocalDate;

public class AluguelRequestDTO {
    private Long alunoId;
    private Long livroId;
    private LocalDate dataInicio;
    private LocalDate dataFim;

    // Construtor vazio
    public AluguelRequestDTO() {}

    // Construtor completo
    public AluguelRequestDTO(Long alunoId, Long livroId, LocalDate dataInicio, LocalDate dataFim) {
        this.alunoId = alunoId;
        this.livroId = livroId;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
    }

    // Getters e Setters
    public Long getAlunoId() {
        return alunoId;
    }

    public void setAlunoId(Long alunoId) {
        this.alunoId = alunoId;
    }

    public Long getLivroId() {
        return livroId;
    }

    public void setLivroId(Long livroId) {
        this.livroId = livroId;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDate dataFim) {
        this.dataFim = dataFim;
    }
}