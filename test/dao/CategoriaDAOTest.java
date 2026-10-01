package dao;

import database.ConexaoBD;
import models.Categoria;
import models.Vendedor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CategoriaDAOTest {

    private CategoriaDAO categoriaDAO;
    private VendedorDAO vendedorDAO;
    private int idVendedorCadastrado;

    @BeforeEach
    public void setUp() {
        categoriaDAO = new CategoriaDAO();
        vendedorDAO = new VendedorDAO();

        limparBancoDeDados();

        Vendedor vendedor = new Vendedor();
        vendedor.setNome("Vendedor Teste");
        vendedor.setCpf("12345678901");
        vendedor.setEmail("vendedor.teste@email.com");
        vendedor.setTelefone("11988887777");
        vendedor.setAtivo(true);
        vendedor.setLogin("vendtest");
        vendedor.setSenhaHash("senha12345");

        vendedorDAO.inserir(vendedor);

        List<Vendedor> vendedores = vendedorDAO.listar();
        idVendedorCadastrado = vendedores.get(0).getIdPessoa();
    }

    @AfterEach
    public void tearDown() {

        limparBancoDeDados();
    }

    private void limparBancoDeDados() {
        try (Connection conn = ConexaoBD.conectar(); Statement stmt = conn.createStatement()) {
            stmt.execute("SET FOREIGN_KEY_CHECKS = 0");
            stmt.execute("TRUNCATE TABLE item_pedido");
            stmt.execute("TRUNCATE TABLE historico_status_pedido");
            stmt.execute("TRUNCATE TABLE pagamento");
            stmt.execute("TRUNCATE TABLE pedido");
            stmt.execute("TRUNCATE TABLE fornecedor_produto");
            stmt.execute("TRUNCATE TABLE produto");
            stmt.execute("TRUNCATE TABLE categoria");
            stmt.execute("TRUNCATE TABLE cliente");
            stmt.execute("TRUNCATE TABLE fornecedor");
            stmt.execute("TRUNCATE TABLE vendedor");
            stmt.execute("TRUNCATE TABLE pessoa");
            stmt.execute("SET FOREIGN_KEY_CHECKS = 1");
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao limpar o banco de dados para os testes: " + e.getMessage(), e);
        }
    }

    @Test
    public void testInserirEListarPorVendedor() {
        Categoria categoria = new Categoria();
        categoria.setIdVendedor(idVendedorCadastrado);
        categoria.setNome("Eletrônicos");
        categoria.setDescricao("Produtos eletrônicos em geral");
        categoria.setIdCategoriaPai(null);

        categoriaDAO.inserir(categoria);

        assertTrue(categoria.getIdCategoria() > 0, "O ID da categoria deve ser gerado após a inserção.");

        List<Categoria> categorias = categoriaDAO.listarPorVendedor(idVendedorCadastrado);
        assertEquals(1, categorias.size(), "Deveria haver exatamente 1 categoria cadastrada.");
        assertEquals("Eletrônicos", categorias.get(0).getNome());
        assertEquals("Produtos eletrônicos em geral", categorias.get(0).getDescricao());
    }

    @Test
    public void testAtualizar() {
        Categoria categoria = new Categoria();
        categoria.setIdVendedor(idVendedorCadastrado);
        categoria.setNome("Roupas");
        categoria.setDescricao("Vestuário");
        categoriaDAO.inserir(categoria);

        categoria.setNome("Moda e Vestuário");
        categoria.setDescricao("Roupas e acessórios");
        categoriaDAO.atualizar(categoria);

        List<Categoria> categorias = categoriaDAO.listarPorVendedor(idVendedorCadastrado);
        assertEquals(1, categorias.size());
        assertEquals("Moda e Vestuário", categorias.get(0).getNome());
        assertEquals("Roupas e acessórios", categorias.get(0).getDescricao());
    }

    @Test
    public void testDeletar() {
        Categoria categoria = new Categoria();
        categoria.setIdVendedor(idVendedorCadastrado);
        categoria.setNome("Livros");
        categoria.setDescricao("Literatura");
        categoriaDAO.inserir(categoria);

        int idCategoria = categoria.getIdCategoria();

        categoriaDAO.deletar(idCategoria, idVendedorCadastrado);

        List<Categoria> categorias = categoriaDAO.listarPorVendedor(idVendedorCadastrado);
        assertTrue(categorias.isEmpty(), "A lista de categorias deveria estar vazia após a exclusão.");
    }
}
