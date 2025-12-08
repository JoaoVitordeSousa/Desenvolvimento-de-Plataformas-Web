package br.com.bibliotecaunifor.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.bibliotecaunifor.dto.request.ReservaRequestDTO;
import br.com.bibliotecaunifor.dto.response.ReservaResponseDTO;
import br.com.bibliotecaunifor.dto.response.ReservaResponseDTO.LivroResumoDTO;
import br.com.bibliotecaunifor.enums.StatusAluguel;
import br.com.bibliotecaunifor.enums.StatusLivro;
import br.com.bibliotecaunifor.enums.StatusReserva;
import br.com.bibliotecaunifor.model.Aluguel;
import br.com.bibliotecaunifor.model.Aluno;
import br.com.bibliotecaunifor.model.Livro;
import br.com.bibliotecaunifor.model.Reserva;
import br.com.bibliotecaunifor.repository.AluguelRepository;
import br.com.bibliotecaunifor.repository.AlunoRepository;
import br.com.bibliotecaunifor.repository.LivroRepository;
import br.com.bibliotecaunifor.repository.ReservaRepository;

@Service
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final AlunoRepository alunoRepository;
    private final LivroRepository livroRepository;
    private final AluguelRepository aluguelRepository;

    public ReservaService(ReservaRepository reservaRepository,
            AlunoRepository alunoRepository,
            LivroRepository livroRepository,
            AluguelRepository aluguelRepository) {
        this.reservaRepository = reservaRepository;
        this.alunoRepository = alunoRepository;
        this.livroRepository = livroRepository;
        this.aluguelRepository = aluguelRepository;
    }

    @Transactional
    public ReservaResponseDTO confirmarReserva(Long alunoId, ReservaRequestDTO dto) {
        Aluno aluno = alunoRepository.findById(alunoId)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));
        Livro livro = livroRepository.findById(dto.getLivroId())
                .orElseThrow(() -> new RuntimeException("Livro não encontrado"));

        if (reservaRepository.existsByLivroIdAndStatus(livro.getId(), StatusReserva.ATIVA)) {
            throw new RuntimeException("Livro já está reservado");
        }

        Reserva reserva = new Reserva(aluno, livro, LocalDate.now(), StatusReserva.ATIVA);
        Reserva salva = reservaRepository.save(reserva);

        livro.setStatus(StatusLivro.RESERVADO);
        livroRepository.save(livro);

        Aluguel aluguel = new Aluguel();
        aluguel.setAluno(aluno);
        aluguel.setLivro(livro);
        aluguel.setDataInicio(LocalDate.now());
        aluguel.setDataFim(LocalDate.now().plusDays(30));
        aluguel.setStatus(StatusAluguel.ATIVO);
        aluguel.setDiasAtraso(0);
        aluguelRepository.save(aluguel);

        return toResponseDTO(salva);
    }

    public List<ReservaResponseDTO> listarReservasPorAluno(Long alunoId) {
        return reservaRepository.findByAlunoId(alunoId)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<ReservaResponseDTO> listarReservasPorMatricula(int matricula) {
        Aluno aluno = alunoRepository.findByMatricula(matricula)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));

        return reservaRepository.findByAlunoId(aluno.getId())
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    private ReservaResponseDTO toResponseDTO(Reserva reserva) {
        return new ReservaResponseDTO(
                reserva.getId(),
                new LivroResumoDTO(reserva.getLivro().getId(), reserva.getLivro().getTitulo()),
                reserva.getStatus(),
                reserva.getDataReserva());
    }
}
