package br.com.fatec.catalogo.services;

import br.com.fatec.catalogo.models.CategoriaModel;
import br.com.fatec.catalogo.repositories.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository repository;

    public List<CategoriaModel> listarTodas() {
        return repository.findAll();
    }

    public List<CategoriaModel> listarPorNome(String nome) {
        return repository.buscarPorNome(nome);
    }

    public CategoriaModel buscarPorId(long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Categoria inválida: " + id));
    }

    @Transactional
    public void excluir(long id) {
        repository.deleteById(id);
    }

    @Transactional
    public CategoriaModel salvar(CategoriaModel categoria) {
        if (categoria.getIdCategoria() != null && categoria.getIdCategoria() == 0) {
            categoria.setIdCategoria(null);
        }
        return repository.save(categoria);
    }
}