package br.com.fatec.catalogo.controllers;

import br.com.fatec.catalogo.repositories.CategoriaRepository;
import br.com.fatec.catalogo.repositories.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @GetMapping("/")
    public String dashboard(Model model) {
        long totalProdutos = produtoRepository.count();
        long totalCategorias = categoriaRepository.count();

        model.addAttribute("totalProdutos", totalProdutos);
        model.addAttribute("totalCategorias", totalCategorias);

        return "dashboard";
    }
}