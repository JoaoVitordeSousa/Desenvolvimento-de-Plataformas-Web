package br.com.bibliotecaunifor.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import br.com.bibliotecaunifor.dto.request.LivroRequestDTO;
import br.com.bibliotecaunifor.dto.response.LivroResponseDTO;
import br.com.bibliotecaunifor.service.LivroService;

@RestController
@RequestMapping("/api/v1/livros")
public class LivroController {

    private final LivroService livroService;

    public LivroController(LivroService livroService) {
        this.livroService = livroService;
    }

    @Operation(summary = "Cadastrar um novo livro", description = "Cria um novo registro de livro na biblioteca")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Livro cadastrado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Erro de validação ou código interno já existente")
    })
    @PostMapping
    public ResponseEntity<LivroResponseDTO> cadastrarLivro(@RequestBody LivroRequestDTO dto) {
        LivroResponseDTO response = livroService.cadastrarLivro(dto);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Editar livro", description = "Atualiza todos os dados de um livro existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Livro atualizado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Livro não encontrado")
    })
    @PutMapping("/{codigoInterno}")
    public ResponseEntity<LivroResponseDTO> editarLivro(
            @PathVariable String codigoInterno,
            @RequestBody LivroRequestDTO dto) {
        Optional<LivroResponseDTO> response = livroService.editarLivro(codigoInterno, dto);
        return response.map(ResponseEntity::ok)
                       .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Editar parcialmente livro", description = "Atualiza apenas os campos informados de um livro existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Livro atualizado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Livro não encontrado")
    })
    @PatchMapping("/{codigoInterno}")
    public ResponseEntity<LivroResponseDTO> editarParcialLivro(
            @PathVariable String codigoInterno,
            @RequestBody LivroRequestDTO dto) {
        Optional<LivroResponseDTO> response = livroService.editarParcialLivro(codigoInterno, dto);
        return response.map(ResponseEntity::ok)
                       .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Excluir livro", description = "Remove um livro da biblioteca pelo código interno")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Livro excluído com sucesso"),
        @ApiResponse(responseCode = "404", description = "Livro não encontrado")
    })
    @DeleteMapping("/{codigoInterno}")
    public ResponseEntity<Void> excluirLivro(@PathVariable String codigoInterno) {
        boolean excluido = livroService.excluirLivro(codigoInterno);
        return excluido ? ResponseEntity.noContent().build()
                        : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Pesquisar livros", description = "Pesquisa livros por campo e valor informado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pesquisa realizada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Campo de pesquisa inválido")
    })
    @GetMapping
    public ResponseEntity<List<LivroResponseDTO>> pesquisarLivros(
            @RequestParam String query,
            @RequestParam String field) {
        List<LivroResponseDTO> resultados = livroService.pesquisarLivros(query, field);
        return ResponseEntity.ok(resultados);
    }

    @Operation(summary = "Buscar livro por código interno", description = "Retorna os dados de um livro específico pelo código interno")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Livro encontrado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Livro não encontrado")
    })
    @GetMapping("/{codigoInterno}")
    public ResponseEntity<LivroResponseDTO> buscarLivroPorCodigoInterno(@PathVariable String codigoInterno) {
        Optional<LivroResponseDTO> response = livroService.buscarPorCodigoInterno(codigoInterno);
        return response.map(ResponseEntity::ok)
                       .orElse(ResponseEntity.notFound().build());
    }
}
