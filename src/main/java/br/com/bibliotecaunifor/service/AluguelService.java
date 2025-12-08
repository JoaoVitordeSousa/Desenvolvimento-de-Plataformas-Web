package br.com.bibliotecaunifor.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.bibliotecaunifor.dto.request.AluguelRequestDTO;
import br.com.bibliotecaunifor.dto.response.AluguelResponseDTO;
import br.com.bibliotecaunifor.dto.response.AluguelResponseDTO.LivroResumoDTO;
import br.com.bibliotecaunifor.enums.StatusAluguel;
import br.com.bibliotecaunifor.model.Aluguel;
import br.com.bibliotecaunifor.model.Aluno;
import br.com.bibliotecaunifor.model.Livro;
import br.com.bibliotecaunifor.repository.AluguelRepository;
import br.com.bibliotecaunifor.repository.AlunoRepository;
import br.com.bibliotecaunifor.repository.LivroRepository;

@Service
public class AluguelService {

    private final AluguelRepository aluguelRepository;
    private final AlunoRepository alunoRepository;
    private final LivroRepository livroRepository;

    public AluguelService(AluguelRepository aluguelRepository,
                          AlunoRepository alunoRepository,
                          LivroRepository livroRepository) {
        this.aluguelRepository = aluguelRepository;
        this.alunoRepository = alunoRepository;
        this.livroRepository = livroRepository;
    }

    @Transactional
    public AluguelResponseDTO criar(AluguelRequestDTO dto) {
        Aluno aluno = alunoRepository.findById(dto.getAlunoId())
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));
        Livro livro = livroRepository.findById(dto.getLivroId())
                .orElseThrow(() -> new RuntimeException("Livro não encontrado"));

        if (aluguelRepository.existsByLivroIdAndStatus(livro.getId(), StatusAluguel.ATIVO)) {
            throw new RuntimeException("Livro já está alugado");
        }

        Aluguel aluguel = new Aluguel();
        aluguel.setAluno(aluno);
        aluguel.setLivro(livro);
        aluguel.setDataInicio(dto.getDataInicio());
        aluguel.setDataFim(dto.getDataFim());
        aluguel.setStatus(StatusAluguel.ATIVO);
        aluguel.setDiasAtraso(0);

        Aluguel salvo = aluguelRepository.save(aluguel);
        return toResponseDTO(salvo);
    }

    public AluguelResponseDTO buscarPorId(Long id) {
        Aluguel aluguel = aluguelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aluguel não encontrado"));
        return toResponseDTO(aluguel);
    }

    public List<AluguelResponseDTO> listarAtivosPorAluno(Long alunoId) {
        return aluguelRepository.findByAlunoIdAndStatus(alunoId, StatusAluguel.ATIVO)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public AluguelResponseDTO finalizar(Long id, LocalDate dataDevolucao) {
        Aluguel aluguel = aluguelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aluguel não encontrado"));

        aluguel.setStatus(StatusAluguel.DEVOLVIDO);

        if (dataDevolucao.isAfter(aluguel.getDataFim())) {
            int atraso = (int) ChronoUnit.DAYS.between(aluguel.getDataFim(), dataDevolucao);
            aluguel.setDiasAtraso(atraso);
        } else {
            aluguel.setDiasAtraso(0);
        }

        aluguel.setDataFim(dataDevolucao);
        Aluguel atualizado = aluguelRepository.save(aluguel);
        return toResponseDTO(atualizado);
    }

    @Transactional
    public AluguelResponseDTO atualizarStatus(Long id, StatusAluguel novoStatus) {
        Aluguel aluguel = aluguelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aluguel não encontrado"));

        aluguel.setStatus(novoStatus);
        Aluguel atualizado = aluguelRepository.save(aluguel);
        return toResponseDTO(atualizado);
    }

    private AluguelResponseDTO toResponseDTO(Aluguel aluguel) {
        return new AluguelResponseDTO(
                aluguel.getId(),
                new LivroResumoDTO(aluguel.getLivro().getId(), aluguel.getLivro().getTitulo()),
                aluguel.getStatus(),
                aluguel.getDataInicio(),
                aluguel.getDataFim()
        );
    }
}