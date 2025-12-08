package br.com.bibliotecaunifor.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.bibliotecaunifor.dto.request.AluguelRequestDTO;
import br.com.bibliotecaunifor.dto.response.AluguelResponseDTO;
import br.com.bibliotecaunifor.enums.StatusAluguel;
import br.com.bibliotecaunifor.model.Usuario;
import br.com.bibliotecaunifor.repository.UsuarioRepository;
import br.com.bibliotecaunifor.service.AluguelService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/alugueis")
@Tag(name = "Aluguéis", description = "Endpoints para gerenciamento de aluguéis de livros")
public class AluguelController {

    private final AluguelService aluguelService;
    private final UsuarioRepository usuarioRepository;

    public AluguelController(AluguelService aluguelService, UsuarioRepository usuarioRepository) {
        this.aluguelService = aluguelService;
        this.usuarioRepository = usuarioRepository;
    }

    @Operation(summary = "Criar novo aluguel", description = "Cria um novo aluguel para um aluno e um livro específico.")
    @PostMapping
    public ResponseEntity<AluguelResponseDTO> criar(
            @RequestBody AluguelRequestDTO dto) {
        AluguelResponseDTO response = aluguelService.criar(dto);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Buscar aluguel por ID", description = "Retorna os detalhes de um aluguel específico pelo seu ID.")
    @GetMapping("/{id}")
    public ResponseEntity<AluguelResponseDTO> buscarPorId(
            @Parameter(description = "ID do aluguel", example = "1") @PathVariable Long id) {
        AluguelResponseDTO response = aluguelService.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Listar aluguéis ativos do usuário logado",
               description = "Retorna todos os aluguéis ativos do usuário autenticado.")
    @GetMapping("/meus")
    public ResponseEntity<List<AluguelResponseDTO>> listarMeus(
            @Parameter(description = "Email do usuário autenticado", example = "aluno@edu.unifor.br") @RequestParam String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        List<AluguelResponseDTO> response = aluguelService.listarAtivosPorAluno(usuario.getId());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Finalizar aluguel (devolução)",
               description = "Finaliza um aluguel, marcando como devolvido e calculando dias de atraso se houver.")
    @PatchMapping("/{id}/devolucao")
    public ResponseEntity<AluguelResponseDTO> finalizar(
            @Parameter(description = "ID do aluguel", example = "1") @PathVariable Long id,
            @Parameter(description = "Data da devolução", example = "2025-12-15") @RequestParam LocalDate dataDevolucao) {
        AluguelResponseDTO response = aluguelService.finalizar(id, dataDevolucao);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Atualizar status do aluguel",
               description = "Permite que administradores ou bibliotecários atualizem o status de um aluguel.")
    @PatchMapping("/{id}/status")
    public ResponseEntity<AluguelResponseDTO> atualizarStatus(
            @Parameter(description = "ID do aluguel", example = "1") @PathVariable Long id,
            @Parameter(description = "Novo status do aluguel", example = "DEVOLVIDO") @RequestParam StatusAluguel novoStatus) {
        AluguelResponseDTO response = aluguelService.atualizarStatus(id, novoStatus);
        return ResponseEntity.ok(response);
    }
}
