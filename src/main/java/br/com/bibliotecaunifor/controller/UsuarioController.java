package br.com.bibliotecaunifor.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.bibliotecaunifor.dto.request.LoginRequestDTO;
import br.com.bibliotecaunifor.dto.request.RedefinirSenhaRequestDTO;
import br.com.bibliotecaunifor.dto.request.UsuarioRequestDTO;
import br.com.bibliotecaunifor.dto.response.LoginResponseDTO;
import br.com.bibliotecaunifor.dto.response.UsuarioResponseDTO;
import br.com.bibliotecaunifor.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/usuarios")
@Tag(name = "Usuários", description = "Endpoints relacionados ao gerenciamento de usuários")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/cadastro")
    @Operation(summary = "Cadastrar usuário", description = "Cria um novo usuário no sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Usuário cadastrado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Erro de validação nos dados enviados")
    })
    public ResponseEntity<UsuarioResponseDTO> cadastrar(@RequestBody UsuarioRequestDTO dto) {
        UsuarioResponseDTO response = usuarioService.cadastrar(dto);
        return ResponseEntity.status(201).body(response);
    }

    @PostMapping("/login")
    @Operation(summary = "Login de usuário", description = "Realiza login por matrícula e senha")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Login realizado com sucesso"),
        @ApiResponse(responseCode = "401", description = "Credenciais inválidas")
    })
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO dto) {
        try {
            UsuarioResponseDTO usuario = usuarioService.login(dto.getMatricula(), dto.getSenha());
            return ResponseEntity.ok(new LoginResponseDTO("Login realizado com sucesso", usuario));
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(new LoginResponseDTO(e.getMessage(), null));
        }
    }

    @Operation(summary = "Buscar usuário por matrícula", description = "Retorna os dados de um usuário específico pela matrícula")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuário encontrado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @GetMapping("/{matricula}")
    public ResponseEntity<UsuarioResponseDTO> buscarPorMatricula(@PathVariable int matricula) {
        try {
            UsuarioResponseDTO response = usuarioService.buscarPorMatricula(matricula);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Redefinir senha", description = "Permite redefinir a senha após validar nome completo, matrícula e email")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Senha redefinida com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados fornecidos não correspondem ao mesmo usuário"),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @PatchMapping("/redefinir-senha")
    public ResponseEntity<UsuarioResponseDTO> redefinirSenha(@RequestBody RedefinirSenhaRequestDTO dto) {
        try {
            UsuarioResponseDTO response = usuarioService.redefinirSenha(
                    dto.getNomeCompleto(),
                    dto.getMatricula(),
                    dto.getEmail(),
                    dto.getNovaSenha());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("não encontrado")) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.badRequest().build();
        }
    }
}
