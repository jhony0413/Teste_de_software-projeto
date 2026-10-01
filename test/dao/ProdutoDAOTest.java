/*
 * Click nbfs:
 * Click nbfs:
 */
package dao;

import java.util.List;
import models.Categoria;
import models.Produto;
import models.Vendedor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ProdutoDAOTest {

    private VendedorDAO vendedorDAO;
    private CategoriaDAO categoriaDAO;
    private ProdutoDAO produtoDAO;

    private Vendedor vendedorTeste;
    private Categoria categoriaTeste;

    public ProdutoDAOTest() {
    }

    @BeforeAll
    public static void setUpClass() {
    }

    @AfterAll
    public static void tearDownClass() {
    }

    @BeforeEach
    public void setUp() {
        vendedorDAO = new VendedorDAO();
        categoriaDAO = new CategoriaDAO();
        produtoDAO = new ProdutoDAO();

        vendedorTeste = new Vendedor();
        vendedorTeste.setNome("Vendedor Teste Produto");
        vendedorTeste.setCpf("12345678901");
        vendedorTeste.setEmail("vendedor.produto@teste.com");
        vendedorTeste.setTelefone("11988887777");
        vendedorTeste.setAtivo(true);
        vendedorTeste.setLogin("vend_prod_teste");
        vendedorTeste.setSenhaHash("senha123456");

        vendedorDAO.inserir(vendedorTeste);

        categoriaTeste = new Categoria();
        categoriaTeste.setIdVendedor(vendedorTeste.getIdPessoa());
        categoriaTeste.setNome("Eletrônicos");
        categoriaTeste.setDescricao("Categoria de produtos eletrônicos");
        categoriaTeste.setIdCategoriaPai(null);

        categoriaDAO.inserir(categoriaTeste);
    }

    @AfterEach
    public void tearDown() {

        try {
            List<Produto> produtos = produtoDAO.listarPorVendedor(vendedorTeste.getIdPessoa());
            for (Produto p : produtos) {
                produtoDAO.deletar(p.getIdProduto(), vendedorTeste.getIdPessoa());
            }

            List<Categoria> categorias = categoriaDAO.listarPorVendedor(vendedorTeste.getIdPessoa());
            for (Categoria c : categorias) {
                categoriaDAO.deletar(c.getIdCategoria(), vendedorTeste.getIdPessoa());
            }

            vendedorDAO.deletar(vendedorTeste.getIdPessoa());
        } catch (Exception e) {
            System.err.println("Erro ao limpar dados do banco após o teste: " + e.getMessage());
        }
    }

    @Test
    public void testInserir() {
        System.out.println("inserir");

        Produto produto = new Produto();
        produto.setIdVendedor(vendedorTeste.getIdPessoa());
        produto.setIdCategoria(categoriaTeste.getIdCategoria());
        produto.setNome("Smartphone X");
        produto.setDescricao("Smartphone com 128GB");
        produto.setPrecoUnitario(1500.00);
        produto.setEstoque(10);
        produto.setAtivo(true);

        produtoDAO.inserir(produto);

        assertTrue(produto.getIdProduto() > 0, "O ID do produto deve ser gerado após a inserção.");

        List<Produto> produtos = produtoDAO.listarPorVendedor(vendedorTeste.getIdPessoa());
        assertFalse(produtos.isEmpty(), "A lista de produtos não deveria estar vazia.");
        assertEquals("Smartphone X", produtos.get(0).getNome());
    }

    @Test
    public void testListarPorVendedor() {
        System.out.println("listarPorVendedor");

        Produto produto = new Produto();
        produto.setIdVendedor(vendedorTeste.getIdPessoa());
        produto.setIdCategoria(categoriaTeste.getIdCategoria());
        produto.setNome("Notebook Y");
        produto.setDescricao("Notebook Core i5");
        produto.setPrecoUnitario(3500.00);
        produto.setEstoque(5);
        produto.setAtivo(true);

        produtoDAO.inserir(produto);

        List<Produto> result = produtoDAO.listarPorVendedor(vendedorTeste.getIdPessoa());
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Notebook Y", result.get(0).getNome());
    }

    @Test
    public void testAtualizar() {
        System.out.println("atualizar");

        Produto produto = new Produto();
        produto.setIdVendedor(vendedorTeste.getIdPessoa());
        produto.setIdCategoria(categoriaTeste.getIdCategoria());
        produto.setNome("Mouse Gamer");
        produto.setDescricao("Mouse RGB");
        produto.setPrecoUnitario(150.00);
        produto.setEstoque(20);
        produto.setAtivo(true);

        produtoDAO.inserir(produto);

        produto.setNome("Mouse Gamer Pro");
        produto.setPrecoUnitario(199.99);
        produto.setEstoque(15);

        produtoDAO.atualizar(produto);

        List<Produto> produtos = produtoDAO.listarPorVendedor(vendedorTeste.getIdPessoa());
        assertEquals(1, produtos.size());
        assertEquals("Mouse Gamer Pro", produtos.get(0).getNome());
        assertEquals(199.99, produtos.get(0).getPrecoUnitario());
        assertEquals(15, produtos.get(0).getEstoque());
    }

    @Test
    public void testDeletar() {
        System.out.println("deletar");

        Produto produto = new Produto();
        produto.setIdVendedor(vendedorTeste.getIdPessoa());
        produto.setIdCategoria(categoriaTeste.getIdCategoria());
        produto.setNome("Teclado Mecânico");
        produto.setDescricao("Switch Blue");
        produto.setPrecoUnitario(250.00);
        produto.setEstoque(8);
        produto.setAtivo(true);

        produtoDAO.inserir(produto);
        int idProduto = produto.getIdProduto();

        produtoDAO.deletar(idProduto, vendedorTeste.getIdPessoa());

        List<Produto> produtos = produtoDAO.listarPorVendedor(vendedorTeste.getIdPessoa());
        assertTrue(produtos.isEmpty(), "O produto deveria ter sido deletado com sucesso.");
    }
}
