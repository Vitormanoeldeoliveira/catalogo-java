package br.com.fatec.catalogo.services;

import br.com.fatec.catalogo.models.ProdutoModel;
import br.com.fatec.catalogo.models.HistoricoModel;
import br.com.fatec.catalogo.repositories.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository repository;

    @Autowired
    private HistoricoService historicoService; // Injeção da service de histórico para gravar os logs

    // Método utilitário privado para capturar o usuário logado de forma segura
    private String obterUsuarioLogado() {
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            return SecurityContextHolder.getContext().getAuthentication().getName();
        }
        return "Sistema/Anônimo"; // Fallback caso ocorra alguma operação fora de sessão
    }

    // 1. Listar todos os produtos
    public List<ProdutoModel> listarTodos() {
        return repository.findAll();
    }

    // 2. Buscar por ID (lança exceção se não encontrar)
    public ProdutoModel buscarPorId(long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado com o ID: " + id));
    }

    // 3. Filtrar por Nome
    public List<ProdutoModel> listarPorNome(String nome) {
        return repository.findByNomeContainingIgnoreCase(nome);
    }

    // 4. Filtrar por Categoria
    public List<ProdutoModel> listarPorCategoria(Long categoriaId) {
        return repository.findByCategoriaIdCategoria(categoriaId);
    }

    // 5. Excluir Produto
    @Transactional
    public void excluir(long id) {
        ProdutoModel produto = buscarPorId(id);

        // Cria o log de auditoria ANTES de remover o produto do banco
        HistoricoModel log = new HistoricoModel(
                produto.getIdProduto(),
                produto.getNome(),
                produto.getValor(),
                produto.getCategoria() != null ? produto.getCategoria().getNome() : "Geral",
                "EXCLUIR",
                obterUsuarioLogado()
        );
        historicoService.registrarLog(log);

        repository.delete(produto);
    }

    // 6. Salvar/Editar aplicando a Regra do Escopo 1 e 2 + Gravação de Histórico
    @Transactional
    public ProdutoModel salvar(ProdutoModel produto) {
        // TÓPICO 1: Validação de estoque negativo
        if (produto.getQuantidade() == null || produto.getQuantidade() < 0) {
            throw new IllegalArgumentException("A quantidade em estoque não pode ser negativa!");
        }

        // Define se é uma nova inserção ou uma atualização antes de salvar no banco
        String acao = (produto.getIdProduto() != null && produto.getIdProduto() > 0) ? "EDITAR" : "SALVAR";

        // TÓPICO 2: Carimba o momento exato da alteração para a Auditoria
        produto.setDataAtualizacao(LocalDateTime.now());

        // Salva o produto para obter o ID gerado (caso seja um cadastro novo)
        ProdutoModel produtoSalvo = repository.save(produto);

        // Cria e registra o log de auditoria com os dados finais salvos
        HistoricoModel log = new HistoricoModel(
                produtoSalvo.getIdProduto(),
                produtoSalvo.getNome(),
                produtoSalvo.getValor(),
                produtoSalvo.getCategoria() != null ? produtoSalvo.getCategoria().getNome() : "Geral",
                acao,
                obterUsuarioLogado()
        );
        historicoService.registrarLog(log);

        return produtoSalvo;
    }

    // 7. TÓPICO 2: Listar para o painel de auditoria (Mais recentes primeiro)
    public List<ProdutoModel> listarProdutosOrdenadosPorModificacao() {
        return repository.findAllByOrderByDataAtualizacaoDesc();
    }
}