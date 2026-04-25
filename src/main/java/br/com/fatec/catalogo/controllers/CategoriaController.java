package br.com.fatec.catalogo.controllers;

import br.com.fatec.catalogo.models.CategoriaModel;
import br.com.fatec.catalogo.repositories.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class CategoriaController {

    @Autowired
    private CategoriaRepository repository;

    // 1. LISTAGEM (COM BUSCA)
    @GetMapping("/categorias")
    public String listaCategorias(@RequestParam(value = "nome", required = false) String nome, Model model) {
        List<CategoriaModel> lista;

        // Se o nome foi enviado na busca, filtra. Senão, traz tudo.
        if (nome != null && !nome.trim().isEmpty()) {
            lista = repository.buscarPorNome(nome);
        } else {
            lista = repository.findAll();
        }

        model.addAttribute("categorias", lista);
        return "lista-categoria"; // Certifique-se de que o arquivo é lista-categorias.html
    }

    // 2. EXIBIR FORMULÁRIO DE CADASTRO
    @GetMapping("/categorias/novo")
    public String exibirFormulario(Model model) {
        model.addAttribute("categoria", new CategoriaModel());
        return "cadastro-categoria";
    }

    // 3. SALVAR (Novo e Edição)
    @PostMapping("/categorias/novo")
    public String salvarCategoria(CategoriaModel categoria) {
        if (categoria.getIdCategoria() != null && categoria.getIdCategoria() == 0) {
            categoria.setIdCategoria(null);
        }

        repository.save(categoria);
        return "redirect:/categorias";
    }

    // 4. FORMULÁRIO DE EDIÇÃO
    @GetMapping("/categorias/editar/{id}")
    public String exibirFormularioEdicao(@PathVariable("id") long id, Model model) {
        CategoriaModel categoria = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Categoria inválida:" + id));

        model.addAttribute("categoria", categoria);
        return "cadastro-categoria";
    }

    // 5. DELETAR
    @GetMapping("/categorias/deletar/{id}")
    public String deletarCategoria(@PathVariable("id") long id) {
        try {
            repository.deleteById(id);
        } catch (Exception e) {
            return "redirect:/categorias?error=vinculo";
        }
        return "redirect:/categorias";
    }
}