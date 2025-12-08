package br.com.bibliotecaunifor.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.bibliotecaunifor.model.Aluno;

import java.util.List;

public interface AlunoRepository extends JpaRepository<Aluno, Long> {
    //Consultas
    Optional<Aluno> findByMatricula(int matricula);

    Optional<Aluno> findByEmail(String email);
    
    List<Aluno> findByNomeCompletoContainingIgnoreCase(String nome);
}
