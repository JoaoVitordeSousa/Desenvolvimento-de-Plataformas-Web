package br.com.bibliotecaunifor.model;

import java.time.LocalDate;

import br.com.bibliotecaunifor.enums.StatusReserva;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity(name = "reservas")
public class Reserva {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "aluno_id", nullable = false)
    private Aluno aluno;

    @ManyToOne
    @JoinColumn(name = "livro_id", nullable = false)
    private Livro livro;

    @Column(nullable = false)
    private LocalDate dataReserva;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusReserva status;

    // Construtor padrão
    public Reserva() {}

    // Construtor com parâmetros
    public Reserva(Aluno aluno, Livro livro, LocalDate dataReserva, StatusReserva status) {
        this.aluno = aluno;
        this.livro = livro;
        this.dataReserva = dataReserva;
        this.status = status;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Aluno getAluno() { return aluno; }
    public void setAluno(Aluno aluno) { this.aluno = aluno; }

    public Livro getLivro() { return livro; }
    public void setLivro(Livro livro) { this.livro = livro; }

    public LocalDate getDataReserva() { return dataReserva; }
    public void setDataReserva(LocalDate dataReserva) { this.dataReserva = dataReserva; }

    public StatusReserva getStatus() { return status; }
    public void setStatus(StatusReserva status) { this.status = status; }
}
