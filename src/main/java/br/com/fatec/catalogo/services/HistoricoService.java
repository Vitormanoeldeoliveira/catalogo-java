package br.com.fatec.catalogo.services;

import br.com.fatec.catalogo.models.HistoricoModel;
import br.com.fatec.catalogo.repositories.HistoricoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class HistoricoService {

    @Autowired
    private HistoricoRepository historicoRepository;

    // Retorna a lista completa ordenada por data/hora
    @Transactional(readOnly = true)
    public List<HistoricoModel> listarTodos() {
        return historicoRepository.findAllByOrderByDataHoraDesc();
    }

    // Salva uma nova entrada no histórico de auditoria
    @Transactional
    public void registrarLog(HistoricoModel historico) {
        historicoRepository.save(historico);
    }
}