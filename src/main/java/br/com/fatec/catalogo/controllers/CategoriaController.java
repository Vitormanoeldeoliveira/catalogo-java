package br.com.fatec.catalogo.controllers;

import br.com.fatec.catalogo.models.CategoriaModel;
import br.com.fatec.catalogo.services.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
public class CategoriaController {

    @Autowired
    private CategoriaService service;

    @GetMapping("/categorias")
    public String listaCategorias(@RequestParam(value = "nome", required = false) String nome, Model model) {
        List<CategoriaModel> lista;

        if (nome != null && !nome.trim().isEmpty()) {
            lista = service.listarPorNome(nome);
        } else {
            lista = service.listarTodas();
        }

        model.addAttribute("categorias", lista);
        return "lista-categoria";
    }

    @GetMapping("/categorias/novo")
    public String exibirFormulario(Model model) {
        model.addAttribute("categoria", new CategoriaModel());
        return "cadastro-categoria";
    }

    @PostMapping("/categorias/novo")
    public String salvarCategoria(CategoriaModel categoria, RedirectAttributes attributes) {
        service.salvar(categoria);

        // Pega o horário da operação e coloca na sessão temporária (Flash)
        String horario = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        attributes.addFlashAttribute("sucesso", "Operação realizada com sucesso às " + horario + "!");

        return "redirect:/categorias";
    }

    @GetMapping("/categorias/editar/{id}")
    public String exibirFormularioEdicao(@PathVariable("id") long id, Model model) {
        CategoriaModel categoria = service.buscarPorId(id);
        model.addAttribute("categoria", categoria);
        return "cadastro-categoria";
    }

    @GetMapping("/categorias/deletar/{id}")
    public String deletarCategoria(@PathVariable("id") long id, RedirectAttributes attributes) {
        try {
            service.excluir(id);

            String horario = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
            attributes.addFlashAttribute("sucesso", "Categoria excluída com sucesso às " + horario + "!");

        } catch (Exception e) {
            // Se houver algum produto vinculado à categoria, captura o erro
            attributes.addFlashAttribute("erro", "Não é possível excluir uma categoria que possui produtos vinculados.");
            return "redirect:/categorias";
        }
        return "redirect:/categorias";
    }
}