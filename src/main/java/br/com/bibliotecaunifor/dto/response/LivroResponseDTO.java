package br.com.bibliotecaunifor.dto.response;

import br.com.bibliotecaunifor.enums.StatusLivro;

public class LivroResponseDTO {
    // Atributos
    private Long id;
    private String codigoInterno;
    private String titulo;
    private String genero;
    private String autor;
    private String sinopse;
    private String isbn10;
    private String isbn13;
    private StatusLivro status;

    // Construtor
    public LivroResponseDTO(){
    }

    // Construtor com parâmetros
    public LivroResponseDTO(
        Long id,
        String codigoInterno,
        String titulo,
        String genero,
        String autor,
        String sinopse,
        String isbn10,
        String isbn13,
        StatusLivro status
    ) {
        this.id = id;
        this.codigoInterno = codigoInterno;
        this.titulo = titulo;
        this.genero = genero;
        this.autor = autor;
        this.sinopse = sinopse;
        this.isbn10 = isbn10;
        this.isbn13 = isbn13;
        this.status = status;
    }

    // Getters (imutabilidade)
    public Long getId() { return id; }

    public String getCodigoInterno() { return codigoInterno; }

    public String getTitulo() { return titulo; }

    public String getGenero() { return genero; }

    public String getAutor() { return autor; }

    public String getSinopse() { return sinopse; }

    public String getIsbn10() { return isbn10; }

    public String getIsbn13() { return isbn13; }

    public StatusLivro getStatus() { return status; }
    
}
