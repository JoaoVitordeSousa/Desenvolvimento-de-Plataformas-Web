package br.com.bibliotecaunifor.service;

import org.springframework.stereotype.Service;

import br.com.bibliotecaunifor.dto.request.UsuarioRequestDTO;
import br.com.bibliotecaunifor.dto.response.UsuarioResponseDTO;
import br.com.bibliotecaunifor.model.Administrador;
import br.com.bibliotecaunifor.model.Aluno;
import br.com.bibliotecaunifor.model.Bibliotecario;
import br.com.bibliotecaunifor.model.Usuario;
import br.com.bibliotecaunifor.repository.UsuarioRepository;
import jakarta.transaction.Transactional;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public UsuarioResponseDTO cadastrar(UsuarioRequestDTO dto) {

        Usuario usuario = criarUsuarioPorEmail(dto);

        usuario.setNomeCompleto(dto.getNomeCompleto());
        usuario.setMatricula(dto.getMatricula());
        usuario.setEmail(dto.getEmail());
        usuario.setSenha(dto.getSenha()); // sem criptografia por enquanto

        Usuario salvo = usuarioRepository.save(usuario);

        String tipo;
        if (salvo instanceof Aluno)
            tipo = "ALUNO";
        else if (salvo instanceof Administrador)
            tipo = "ADMINISTRADOR";
        else if (salvo instanceof Bibliotecario)
            tipo = "BIBLIOTECARIO";
        else
            tipo = "DESCONHECIDO";

        return new UsuarioResponseDTO(
                salvo.getId(),
                salvo.getNomeCompleto(),
                salvo.getMatricula(),
                salvo.getEmail(),
                tipo);
    }

    private Usuario criarUsuarioPorEmail(UsuarioRequestDTO dto) {
        String email = dto.getEmail();
        if (email.endsWith("@edu.unifor.br")) {
            return new Aluno();
        } else if (email.endsWith("@admin.unifor.br")) {
            return new Administrador();
        } else if (email.endsWith("@unifor.br")) {
            return new Bibliotecario();
        } else {
            throw new IllegalArgumentException("Domínio de email inválido para cadastro de usuário");
        }
    }

    public UsuarioResponseDTO buscarPorEmail(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        String tipo;
        if (usuario instanceof Aluno)
            tipo = "ALUNO";
        else if (usuario instanceof Administrador)
            tipo = "ADMINISTRADOR";
        else if (usuario instanceof Bibliotecario)
            tipo = "BIBLIOTECARIO";
        else
            tipo = "DESCONHECIDO";

        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getNomeCompleto(),
                usuario.getMatricula(),
                usuario.getEmail(),
                tipo);
    }

    public UsuarioResponseDTO buscarPorMatricula(int matricula) {
        Usuario usuario = usuarioRepository.findByMatricula(matricula)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        String tipo;
        if (usuario instanceof Aluno)
            tipo = "ALUNO";
        else if (usuario instanceof Administrador)
            tipo = "ADMINISTRADOR";
        else if (usuario instanceof Bibliotecario)
            tipo = "BIBLIOTECARIO";
        else
            tipo = "DESCONHECIDO";

        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getNomeCompleto(),
                usuario.getMatricula(),
                usuario.getEmail(),
                tipo);
    }

    @Transactional
    public UsuarioResponseDTO login(int matricula, String senha) {
        Usuario usuario = usuarioRepository.findByMatricula(matricula)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // Validação simples (sem criptografia ainda)
        if (!usuario.getSenha().equals(senha)) {
            throw new RuntimeException("Senha inválida");
        }

        // Determina o tipo pelo instanceof
        String tipo;
        if (usuario instanceof Aluno)
            tipo = "ALUNO";
        else if (usuario instanceof Administrador)
            tipo = "ADMINISTRADOR";
        else if (usuario instanceof Bibliotecario)
            tipo = "BIBLIOTECARIO";
        else
            tipo = "DESCONHECIDO";

        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getNomeCompleto(),
                usuario.getMatricula(),
                usuario.getEmail(),
                tipo);
    }
}
