package br.com.bibliotecaunifor.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.bibliotecaunifor.enums.StatusReserva;
import br.com.bibliotecaunifor.model.Reserva;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    // Consultas
    List<Reserva> findByAlunoId(Long alunoId);

    List<Reserva> findByAlunoIdAndStatus(Long alunoId, StatusReserva status);

    List<Reserva> findByLivroId(Long livroId);

    boolean existsByLivroIdAndStatus(Long livroId, StatusReserva status);

    Optional<Reserva> findByAlunoIdAndLivroId(Long alunoId, Long livroId);
    
}
