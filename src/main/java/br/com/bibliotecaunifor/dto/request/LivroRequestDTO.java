package br.com.bibliotecaunifor.dto.request;

import br.com.bibliotecaunifor.enums.StatusLivro;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class LivroRequestDTO {
    // Atributos com validação
    @NotBlank(message = "Código interno é obrigatório")
    @Size(message = "O código interno deve ter no mínimo 3 dígitos e no máximo 15", min = 3, max = 15)
    private String codigoInterno;
    
    @NotBlank(message = "Título é obrigatório")
    private String titulo;

    @NotBlank(message = "Gênero é obrigatório")
    private String genero;

    @NotBlank(message = "Autor é obrigatório")
    private String autor;

    @NotBlank(message = "Sinopse é obrigatória")
    @Size(message = "Sinopse deve ter no máximo 400 caracteres", max = 400)
    private String sinopse;

    @NotBlank(message = "ISBN-10 é obrigatório")
    @Size(message = "O ISBN-10 deve ter 10 dígitos apenas", min = 10, max = 10)
    private String isbn10;

    @NotBlank(message = "ISBN-13 é obrigatório")
    @Size(message = "O ISBN-13 deve ter 13 dígitos apenas", min = 13, max = 13)
    private String isbn13;

    @NotNull(message = "Status é obrigatório")
    private StatusLivro status;

    // Construtor padrão
    public LivroRequestDTO(){
    }

    // Contrutor com parâmetros
    public LivroRequestDTO( 
        String codigoInterno, 
        String titulo, 
        String genero, 
        String autor, 
        String sinopse, 
        String isbn10, 
        String isbn13, 
        StatusLivro status
    ) {
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
    public String getCodigoInterno() { return codigoInterno; }

    public String getTitulo() { return titulo; }

    public String getGenero() { return genero; }

    public String getAutor() { return autor; }

    public String getSinopse() { return sinopse; }

    public String getIsbn10() { return isbn10; }

    public String getIsbn13() { return isbn13; }

    public StatusLivro getStatus() { return status; }
    
}
