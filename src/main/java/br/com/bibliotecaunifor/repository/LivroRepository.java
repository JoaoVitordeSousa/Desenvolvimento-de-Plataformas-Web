package br.com.bibliotecaunifor.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.bibliotecaunifor.model.Livro;
import br.com.bibliotecaunifor.enums.StatusLivro;


public interface LivroRepository extends JpaRepository<Livro, Long> {
    // Consultas
    Optional<Livro> findById(Long id);
    
    Optional<Livro> findByCodigoInterno(String codigoInterno);

    List<Livro> findByTitulo(String titulo);

    List<Livro> findByAutor(String autor);

    List<Livro> findByGenero(String genero);

    List<Livro> findByIsbn10(String isbn10);

    List<Livro> findByIsbn13(String isbn13);

    List<Livro> findByStatus(StatusLivro status);

}
