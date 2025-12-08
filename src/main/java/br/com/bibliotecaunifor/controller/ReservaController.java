package br.com.bibliotecaunifor.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.bibliotecaunifor.dto.request.ReservaRequestDTO;
import br.com.bibliotecaunifor.dto.response.ReservaResponseDTO;
import br.com.bibliotecaunifor.model.Usuario;
import br.com.bibliotecaunifor.repository.UsuarioRepository;
import br.com.bibliotecaunifor.service.ReservaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/reservas")
@Tag(name = "Reservas", description = "Endpoints para gerenciamento de reservas de livros")
public class ReservaController {

    private final ReservaService reservaService;
    private final UsuarioRepository usuarioRepository;

    public ReservaController(ReservaService reservaService, UsuarioRepository usuarioRepository) {
        this.reservaService = reservaService;
        this.usuarioRepository = usuarioRepository;
    }

    @Operation(summary = "Confirmar reserva", description = "Cria uma nova reserva para o aluno logado, altera o status do livro para 'Reservado' e gera automaticamente um aluguel com prazo de 30 dias.")
    @PostMapping
    public ResponseEntity<ReservaResponseDTO> confirmarReserva(
            @Parameter(description = "Email do usuário autenticado", example = "aluno@edu.unifor.br") @RequestParam String email,
            @RequestBody ReservaRequestDTO dto) {

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        ReservaResponseDTO response = reservaService.confirmarReserva(usuario.getId(), dto);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Listar reservas do usuário logado", description = "Retorna todas as reservas (ativas e inativas) do aluno autenticado.")
    @GetMapping("/minhas")
    public ResponseEntity<List<ReservaResponseDTO>> listarMinhasReservas(
            @Parameter(description = "Email do usuário autenticado", example = "aluno@edu.unifor.br") @RequestParam String email) {

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        List<ReservaResponseDTO> response = reservaService.listarReservasPorAluno(usuario.getId());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Listar reservas por matrícula", description = "Retorna todas as reservas de um aluno pela matrícula.")
    @GetMapping
    public ResponseEntity<List<ReservaResponseDTO>> listarPorMatricula(
            @Parameter(description = "Matrícula do aluno", example = "202300123") @RequestParam int matricula) {
        List<ReservaResponseDTO> response = reservaService.listarReservasPorMatricula(matricula);
        return ResponseEntity.ok(response);
    }
}
