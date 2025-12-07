package br.com.bibliotecaunifor.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.bibliotecaunifor.dto.request.LivroRequestDTO;
import br.com.bibliotecaunifor.dto.response.LivroResponseDTO;
import br.com.bibliotecaunifor.model.Livro;
import br.com.bibliotecaunifor.repository.LivroRepository;

@Service
public class LivroService {
    private final LivroRepository livroRepository;

    public LivroService(LivroRepository livroRepository) {
        this.livroRepository = livroRepository;
    }

    @Transactional
    public LivroResponseDTO cadastrarLivro(LivroRequestDTO dto) {
        if (livroRepository.findByCodigoInterno(dto.getCodigoInterno()).isPresent()) {
            throw new IllegalArgumentException("Código interno já está em uso por outro livro.");
        }

        Livro livro = new Livro();
        livro.setCodigoInterno(dto.getCodigoInterno());
        livro.setTitulo(dto.getTitulo());
        livro.setGenero(dto.getGenero());
        livro.setAutor(dto.getAutor());
        livro.setSinopse(dto.getSinopse());
        livro.setIsbn10(dto.getIsbn10());
        livro.setIsbn13(dto.getIsbn13());
        livro.setStatus(dto.getStatus());

        Livro salvo = livroRepository.save(livro);
        return toResponseDTO(salvo);
    }

    @Transactional
    public Optional<LivroResponseDTO> editarLivro(String codigoInterno, LivroRequestDTO dto) {
        return livroRepository.findByCodigoInterno(codigoInterno)
                .map(livro -> {
                    if (!livro.getCodigoInterno().equals(dto.getCodigoInterno()) &&
                            livroRepository.findByCodigoInterno(dto.getCodigoInterno()).isPresent()) {
                        throw new IllegalArgumentException("Código interno já está em uso por outro livro.");
                    }

                    livro.setCodigoInterno(dto.getCodigoInterno());
                    livro.setTitulo(dto.getTitulo());
                    livro.setGenero(dto.getGenero());
                    livro.setAutor(dto.getAutor());
                    livro.setSinopse(dto.getSinopse());
                    livro.setIsbn10(dto.getIsbn10());
                    livro.setIsbn13(dto.getIsbn13());
                    livro.setStatus(dto.getStatus());

                    Livro atualizado = livroRepository.save(livro);
                    return toResponseDTO(atualizado);
                });
    }

    @Transactional
    public Optional<LivroResponseDTO> editarParcialLivro(String codigoInterno, LivroRequestDTO dto) {
        return livroRepository.findByCodigoInterno(codigoInterno)
                .map(livro -> {
                    if (dto.getCodigoInterno() != null &&
                            !livro.getCodigoInterno().equals(dto.getCodigoInterno()) &&
                            livroRepository.findByCodigoInterno(dto.getCodigoInterno()).isPresent()) {
                        throw new IllegalArgumentException("Código interno já está em uso por outro livro.");
                    }

                    if (dto.getCodigoInterno() != null)
                        livro.setCodigoInterno(dto.getCodigoInterno());
                    if (dto.getTitulo() != null)
                        livro.setTitulo(dto.getTitulo());
                    if (dto.getGenero() != null)
                        livro.setGenero(dto.getGenero());
                    if (dto.getAutor() != null)
                        livro.setAutor(dto.getAutor());
                    if (dto.getSinopse() != null)
                        livro.setSinopse(dto.getSinopse());
                    if (dto.getIsbn10() != null)
                        livro.setIsbn10(dto.getIsbn10());
                    if (dto.getIsbn13() != null)
                        livro.setIsbn13(dto.getIsbn13());
                    if (dto.getStatus() != null)
                        livro.setStatus(dto.getStatus());

                    Livro atualizado = livroRepository.save(livro);
                    return toResponseDTO(atualizado);
                });
    }

    @Transactional
    public boolean excluirLivro(String codigoInterno) {
        return livroRepository.findByCodigoInterno(codigoInterno)
                .map(livro -> {
                    livroRepository.delete(livro);
                    return true;
                })
                .orElse(false);
    }

    @Transactional(readOnly = true)
    public List<LivroResponseDTO> pesquisarLivros(String query, String field) {
        List<Livro> resultados;

        switch (field.toLowerCase()) {
            case "codigointerno":
                resultados = livroRepository.findByCodigoInterno(query)
                        .map(List::of)
                        .orElse(List.of());
                break;
            case "titulo":
                resultados = livroRepository.findByTitulo(query);
                break;
            case "autor":
                resultados = livroRepository.findByAutor(query);
                break;
            case "genero":
                resultados = livroRepository.findByGenero(query);
                break;
            case "isbn10":
                resultados = livroRepository.findByIsbn10(query);
                break;
            case "isbn13":
                resultados = livroRepository.findByIsbn13(query);
                break;
            default:
                throw new IllegalArgumentException("Campo de pesquisa inválido: " + field);
        }

        return resultados.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<LivroResponseDTO> buscarPorCodigoInterno(String codigoInterno) {
        return livroRepository.findByCodigoInterno(codigoInterno)
                .map(this::toResponseDTO);
    }

    private LivroResponseDTO toResponseDTO(Livro livro) {
        return new LivroResponseDTO(
                livro.getId(),
                livro.getCodigoInterno(),
                livro.getTitulo(),
                livro.getGenero(),
                livro.getAutor(),
                livro.getSinopse(),
                livro.getIsbn10(),
                livro.getIsbn13(),
                livro.getStatus());
    }
}