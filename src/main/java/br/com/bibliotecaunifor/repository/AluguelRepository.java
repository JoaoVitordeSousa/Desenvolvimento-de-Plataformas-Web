package br.com.bibliotecaunifor.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.bibliotecaunifor.enums.StatusAluguel;
import br.com.bibliotecaunifor.model.Aluguel;

public interface AluguelRepository extends JpaRepository<Aluguel, Long> {
    // Consultas
    List<Aluguel> findByAlunoId(Long alunoId);

    List<Aluguel> findByAlunoIdAndStatus(Long alunoId, StatusAluguel status);

    List<Aluguel> findByLivroId(Long livroId);

    boolean existsByLivroIdAndStatus(Long livroId, StatusAluguel status);

    Optional<Aluguel> findByAlunoIdAndLivroId(Long alunoId, Long livroId);
}
