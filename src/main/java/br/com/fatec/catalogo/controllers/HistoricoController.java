package br.com.fatec.catalogo.controllers;

import br.com.fatec.catalogo.models.HistoricoModel;
import br.com.fatec.catalogo.services.HistoricoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/auditoria")
public class HistoricoController {

    @Autowired
    private HistoricoService historicoService;

    @GetMapping
    public String exibirTelaHistorico(Model model) {
        List<HistoricoModel> listaHistorico = historicoService.listarTodos();

        // Garante que o Thymeleaf nunca receba um atributo nulo para iterar
        if (listaHistorico == null) {
            listaHistorico = new ArrayList<>();
        }

        model.addAttribute("historicos", listaHistorico);
        return "auditoria"; // Retorna o arquivo auditoria.html
    }
}