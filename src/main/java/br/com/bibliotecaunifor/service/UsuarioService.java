package br.com.bibliotecaunifor.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.bibliotecaunifor.config.JwtUtil;
import br.com.bibliotecaunifor.dto.request.UsuarioRequestDTO;
import br.com.bibliotecaunifor.dto.response.LoginResponseDTO;
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
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UsuarioService(UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            JwtUtil jwtUtil) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    public UsuarioResponseDTO cadastrar(UsuarioRequestDTO dto) {

        Usuario usuario = criarUsuarioPorEmail(dto);

        usuario.setNomeCompleto(dto.getNomeCompleto());
        usuario.setMatricula(dto.getMatricula());
        usuario.setEmail(dto.getEmail());
        usuario.setSenha(passwordEncoder.encode(dto.getSenha()));

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
    public LoginResponseDTO login(int matricula, String senha) {
        Usuario usuario = usuarioRepository.findByMatricula(matricula)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // Validação com BCrypt
        if (!passwordEncoder.matches(senha, usuario.getSenha())) {
            throw new RuntimeException("Senha inválida");
        }

        // Gera token JWT
        String token = jwtUtil.generateToken(usuario.getEmail());

        // Monta DTO do usuário
        UsuarioResponseDTO usuarioDTO = toResponseDTO(usuario);

        // Retorna LoginResponseDTO com token + dados do usuário
        return new LoginResponseDTO("Login realizado com sucesso", token, usuarioDTO);
    }

    @Transactional
    public UsuarioResponseDTO redefinirSenha(String nomeCompleto, int matricula, String email, String novaSenha) {
        Usuario usuario = usuarioRepository.findByMatricula(matricula)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!usuario.getNomeCompleto().equalsIgnoreCase(nomeCompleto) ||
                !usuario.getEmail().equalsIgnoreCase(email)) {
            throw new RuntimeException("Dados fornecidos não correspondem ao mesmo usuário");
        }

        usuario.setSenha(novaSenha); // sem criptografia por enquanto
        Usuario atualizado = usuarioRepository.save(usuario);

        String tipo;
        if (atualizado instanceof Aluno)
            tipo = "ALUNO";
        else if (atualizado instanceof Administrador)
            tipo = "ADMINISTRADOR";
        else if (atualizado instanceof Bibliotecario)
            tipo = "BIBLIOTECARIO";
        else
            tipo = "DESCONHECIDO";

        return new UsuarioResponseDTO(
                atualizado.getId(),
                atualizado.getNomeCompleto(),
                atualizado.getMatricula(),
                atualizado.getEmail(),
                tipo);
    }

    private UsuarioResponseDTO toResponseDTO(Usuario usuario) {
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
