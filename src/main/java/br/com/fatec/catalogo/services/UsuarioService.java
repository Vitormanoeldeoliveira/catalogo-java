package br.com.fatec.catalogo.services;

import br.com.fatec.catalogo.models.UsuarioModel;
import br.com.fatec.catalogo.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository repository;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public List<UsuarioModel> listarTodos() {
        return repository.findAll();
    }

    public List<UsuarioModel> listarPorLogin(String login) {
        return repository.findByLoginContainingIgnoreCase(login);
    }

    public Optional<UsuarioModel> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public Optional<UsuarioModel> buscarPorLoginPuro(String login) {
        return repository.findByLogin(login);
    }

    @Transactional
    public void excluir(Long id) {
        repository.deleteById(id);
    }

    @Transactional
    public UsuarioModel salvar(UsuarioModel usuarioModel) {
        if (usuarioModel.getIdUsuario() == null) {
            // Regra para Novo Usuário
            if (buscarPorLoginPuro(usuarioModel.getLogin()).isPresent()) {
                throw new IllegalArgumentException("Este login já está em uso!");
            }
            usuarioModel.setSenha(encoder.encode(usuarioModel.getSenha()));
        } else {
            // Regra para Edição de Usuário Existente
            UsuarioModel usuarioBanco = repository.findById(usuarioModel.getIdUsuario())
                    .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

            if (usuarioModel.getSenha() == null || usuarioModel.getSenha().isEmpty()) {
                usuarioModel.setSenha(usuarioBanco.getSenha());
            } else {
                usuarioModel.setSenha(encoder.encode(usuarioModel.getSenha()));
            }
        }
        return repository.save(usuarioModel);
    }
}