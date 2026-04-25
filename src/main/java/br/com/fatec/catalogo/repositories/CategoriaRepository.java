package br.com.fatec.catalogo.repositories;

import br.com.fatec.catalogo.models.CategoriaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoriaRepository extends JpaRepository<CategoriaModel, Long> {

    // Método para buscar categorias por parte do nome (ignore case)
    @Query("SELECT c FROM CategoriaModel c WHERE UPPER(c.nome) LIKE UPPER(CONCAT('%', :nome, '%'))")
    List<CategoriaModel> buscarPorNome(String nome);
}