package br.com.fatec.catalogo.controllers;

import br.com.fatec.catalogo.models.UsuarioModel;
import br.com.fatec.catalogo.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    @GetMapping("/usuarios")
    public String listarUsuarios(@RequestParam(value = "login", required = false) String login, Model model) {
        if (login != null && !login.isBlank()) {
            model.addAttribute("usuarios", service.listarPorLogin(login));
        } else {
            model.addAttribute("usuarios", service.listarTodos());
        }
        return "lista-usuario";
    }

    @GetMapping("/usuarios/deletar/{id}")
    public String deletarUsuario(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            service.excluir(id);
            redirectAttributes.addFlashAttribute("sucesso", "Usuário excluído com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Não foi possível excluir o usuário.");
        }
        return "redirect:/usuarios";
    }

    @GetMapping("/cadastro")
    public String exibirCadastro(@ModelAttribute("usuario") UsuarioModel usuarioModel) {
        return "cadastro-usuario";
    }

    @GetMapping("/usuarios/editar/{id}")
    public String exibirEdicao(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<UsuarioModel> usuarioOpt = service.buscarPorId(id);

        if (usuarioOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("erro", "Usuário não encontrado.");
            return "redirect:/usuarios"; // Corrigido de /usuario para /usuarios
        }

        model.addAttribute("usuario", usuarioOpt.get());
        return "cadastro-usuario";
    }

    @PostMapping("/cadastro")
    public String salvarUsuario(@ModelAttribute("usuario") UsuarioModel usuarioModel,
                                RedirectAttributes redirectAttributes) {
        try {
            service.salvar(usuarioModel);
            redirectAttributes.addFlashAttribute("sucesso", "Usuário salvo com sucesso!");
            return "redirect:/usuarios";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
            return "redirect:/cadastro"; // Corrigido de /cadastro-usuario (HTML) para a rota /cadastro
        }
    }
}