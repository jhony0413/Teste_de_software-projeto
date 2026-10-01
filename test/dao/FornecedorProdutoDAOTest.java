package dao;

import database.ConexaoBD;
import models.Categoria;
import models.Fornecedor;
import models.FornecedorProduto;
import models.Produto;
import models.Vendedor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FornecedorProdutoDAOTest {

    private VendedorDAO vendedorDAO;
    private CategoriaDAO categoriaDAO;
    private FornecedorDAO fornecedorDAO;
    private ProdutoDAO produtoDAO;
    private FornecedorProdutoDAO fornecedorProdutoDAO;

    private int idVendedor;
    private int idCategoria;
    private int idFornecedor;
    private int idProduto;

    @BeforeEach
    public void setUp() {
        vendedorDAO = new VendedorDAO();
        categoriaDAO = new CategoriaDAO();
        fornecedorDAO = new FornecedorDAO();
        produtoDAO = new ProdutoDAO();
        fornecedorProdutoDAO = new FornecedorProdutoDAO();

        limparBanco();

        Vendedor vendedor = new Vendedor();
        vendedor.setNome("Vendedor Teste");
        vendedor.setCpf("12345678901");
        vendedor.setEmail("vendedor@teste.com");
        vendedor.setTelefone("11988887777");
        vendedor.setAtivo(true);
        vendedor.setLogin("vendtest");
        vendedor.setSenhaHash("senha12345");
        vendedorDAO.inserir(vendedor);
        idVendedor = vendedor.getIdPessoa();

        Categoria categoria = new Categoria();
        categoria.setIdVendedor(idVendedor);
        categoria.setNome("Eletrônicos");
        categoria.setDescricao("Produtos eletrônicos em geral");
        categoria.setIdCategoriaPai(null);
        categoriaDAO.inserir(categoria);
        idCategoria = categoria.getIdCategoria();

        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setIdVendedor(idVendedor);
        fornecedor.setCnpj("12345678000199");
        fornecedor.setNome("Fornecedor Global LTDA");
        fornecedor.setEmail("contato@fornecedor.com");
        fornecedor.setTelefone("1133334444");
        fornecedor.setAtivo(true);
        fornecedorDAO.inserir(fornecedor);
        idFornecedor = fornecedor.getIdFornecedor();

        Produto produto = new Produto();
        produto.setIdVendedor(idVendedor);
        produto.setIdCategoria(idCategoria);
        produto.setNome("Smartphone X");
        produto.setDescricao("Smartphone com 128GB");
        produto.setPrecoUnitario(1500.00);
        produto.setEstoque(10);
        produto.setAtivo(true);
        produtoDAO.inserir(produto);
        idProduto = produto.getIdProduto();
    }

    @AfterEach
    public void tearDown() {
        limparBanco();
    }

    private void limparBanco() {
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
    public void testInserir() {
        System.out.println("Testando inserir associação Fornecedor-Produto");

        FornecedorProduto fp = new FornecedorProduto(idFornecedor, idProduto, idVendedor, 1200.00, 5, 20);

        assertDoesNotThrow(() -> fornecedorProdutoDAO.inserir(fp));

        List<FornecedorProduto> lista = fornecedorProdutoDAO.listarPorVendedor(idVendedor);
        assertEquals(1, lista.size());
        assertEquals(1200.00, lista.get(0).getPrecoCusto());
        assertEquals(5, lista.get(0).getPrazoEntregaDias());
        assertEquals(20, lista.get(0).getQuantidade());
    }

    @Test
    public void testListarPorVendedor() {
        System.out.println("Testando listarPorVendedor");

        FornecedorProduto fp = new FornecedorProduto(idFornecedor, idProduto, idVendedor, 1100.00, 3, 15);
        fornecedorProdutoDAO.inserir(fp);

        List<FornecedorProduto> lista = fornecedorProdutoDAO.listarPorVendedor(idVendedor);
        assertNotNull(lista);
        assertFalse(lista.isEmpty());
        assertEquals(idFornecedor, lista.get(0).getIdFornecedor());
        assertEquals(idProduto, lista.get(0).getIdProduto());
    }

    @Test
    public void testAtualizar() {
        System.out.println("Testando atualizar associação Fornecedor-Produto");

        FornecedorProduto fp = new FornecedorProduto(idFornecedor, idProduto, idVendedor, 1000.00, 7, 10);
        fornecedorProdutoDAO.inserir(fp);

        fp.setPrecoCusto(950.00);
        fp.setPrazoEntregaDias(4);
        fp.setQuantidade(25);

        assertDoesNotThrow(() -> fornecedorProdutoDAO.atualizar(fp));

        List<FornecedorProduto> lista = fornecedorProdutoDAO.listarPorVendedor(idVendedor);
        assertEquals(1, lista.size());
        assertEquals(950.00, lista.get(0).getPrecoCusto());
        assertEquals(4, lista.get(0).getPrazoEntregaDias());
        assertEquals(25, lista.get(0).getQuantidade());
    }

    @Test
    public void testDeletar() {
        System.out.println("Testando deletar associação Fornecedor-Produto");

        FornecedorProduto fp = new FornecedorProduto(idFornecedor, idProduto, idVendedor, 1000.00, 7, 10);
        fornecedorProdutoDAO.inserir(fp);

        assertDoesNotThrow(() -> fornecedorProdutoDAO.deletar(idFornecedor, idProduto, idVendedor));

        List<FornecedorProduto> lista = fornecedorProdutoDAO.listarPorVendedor(idVendedor);
        assertTrue(lista.isEmpty());
    }
}
