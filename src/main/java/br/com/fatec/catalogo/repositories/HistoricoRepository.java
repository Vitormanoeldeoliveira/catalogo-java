package br.com.fatec.catalogo.repositories;

import  br.com.fatec.catalogo.models.HistoricoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface HistoricoRepository extends JpaRepository<HistoricoModel, Long> {
    
    // Busca todo o histórico ordenando da modificação mais recente para a mais antiga
    List<HistoricoModel> findAllByOrderByDataHoraDesc();
}