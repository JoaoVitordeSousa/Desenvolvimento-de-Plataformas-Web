package br.com.bibliotecaunifor.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.bibliotecaunifor.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>{
    // Consultas
    Optional<Usuario> findByEmail(String email);

    Optional<Usuario> findByMatricula(int matricula);

    boolean existsByEmail(String email);

    boolean existsByMatricula(int matricula);

}
