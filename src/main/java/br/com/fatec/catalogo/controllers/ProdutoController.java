package br.com.fatec.catalogo.controllers;

import br.com.fatec.catalogo.models.CategoriaModel;
import br.com.fatec.catalogo.models.ProdutoModel;
import br.com.fatec.catalogo.repositories.CategoriaRepository;
import br.com.fatec.catalogo.repositories.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ProdutoController {

    @Autowired
    private ProdutoRepository repository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    // 1. LISTAGEM COM FILTROS (NOME E CATEGORIA)
    @GetMapping("/produtos")
    public String listaProdutos(
            @RequestParam(value = "nome", required = false) String nome,
            @RequestParam(value = "categoriaId", required = false) Long categoriaId,
            Model model) {

        List<ProdutoModel> lista;

        // Lógica de Filtros Combinados
        if (nome != null && !nome.trim().isEmpty() && categoriaId != null) {
            // Busca por Nome E Categoria
            lista = repository.findByNomeContainingIgnoreCaseAndCategoriaIdCategoria(nome, categoriaId);
        } else if (nome != null && !nome.trim().isEmpty()) {
            // Busca apenas por Nome
            lista = repository.findByNomeContainingIgnoreCase(nome);
        } else if (categoriaId != null) {
            // Busca apenas por Categoria
            lista = repository.findByCategoriaIdCategoria(categoriaId);
        } else {
            // Sem filtros, traz tudo
            lista = repository.findAll();
        }

        // Adiciona dados ao Model para renderizar na página
        model.addAttribute("produtos", lista);
        model.addAttribute("categorias", categoriaRepository.findAll()); // Para preencher o select de filtro
        model.addAttribute("categoriaSelecionada", categoriaId); // Para manter a seleção no select após a busca

        return "lista-produtos";
    }

    // 2. EXIBIR FORMULÁRIO DE CADASTRO
    @GetMapping("/produtos/novo")
    public String exibirFormulario(Model model) {
        model.addAttribute("produto", new ProdutoModel());
        model.addAttribute("categorias", categoriaRepository.findAll());
        return "cadastro-produto";
    }

    // 3. SALVAR PRODUTO
    @PostMapping("/produtos/novo")
    public String salvarProduto(ProdutoModel produto, Model model) {
        try {
            // Trata ID zero como nulo para criação de novo registro
            if (produto.getIdProduto() != null && produto.getIdProduto() == 0) {
                produto.setIdProduto(null);
            }
            repository.save(produto);
            return "redirect:/produtos";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("produto", produto);
            model.addAttribute("categorias", categoriaRepository.findAll());
            return "cadastro-produto";
        }
    }

    // 4. EXIBIR FORMULÁRIO DE EDIÇÃO
    @GetMapping("/produtos/editar/{id}")
    public String exibirFormularioEdicao(@PathVariable("id") long id, Model model) {
        ProdutoModel produto = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Produto inválido:" + id));

        model.addAttribute("produto", produto);
        model.addAttribute("categorias", categoriaRepository.findAll());
        return "cadastro-produto";
    }

    // 5. DELETAR PRODUTO
    @GetMapping("/produtos/deletar/{id}")
    public String deletarProduto(@PathVariable("id") long id) {
        repository.deleteById(id);
        return "redirect:/produtos";
    }
}