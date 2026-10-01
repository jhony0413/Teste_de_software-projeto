package dao;

import database.ConexaoBD;
import models.*;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PagamentoDAOTest {

    private static VendedorDAO vendedorDAO;
    private static ClienteDAO clienteDAO;
    private static CategoriaDAO categoriaDAO;
    private static ProdutoDAO produtoDAO;
    private static PedidoDAO pedidoDAO;
    private static PagamentoDAO pagamentoDAO;

    private int idVendedorGerado;
    private int idClienteGerado;
    private int idCategoriaGerada;
    private int idProdutoGerado;
    private int idPedidoGerado;

    @BeforeAll
    public static void setUpClass() {
        vendedorDAO = new VendedorDAO();
        clienteDAO = new ClienteDAO();
        categoriaDAO = new CategoriaDAO();
        produtoDAO = new ProdutoDAO();
        pedidoDAO = new PedidoDAO();
        pagamentoDAO = new PagamentoDAO();
    }

    @AfterAll
    public static void tearDownClass() {
        limparBanco();
    }

    @BeforeEach
    public void setUp() {
        limparBanco();

        Vendedor vendedor = new Vendedor();
        vendedor.setNome("Vendedor Teste");
        vendedor.setCpf("12345678901");
        vendedor.setEmail("vendedor.teste@email.com");
        vendedor.setTelefone("11999999999");
        vendedor.setAtivo(true);
        vendedor.setLogin("vendtest");
        vendedor.setSenhaHash("senha12345");
        vendedorDAO.inserir(vendedor);
        idVendedorGerado = vendedor.getIdPessoa();

        Cliente cliente = new Cliente();
        cliente.setNome("Cliente Teste");
        cliente.setCpf("98765432109");
        cliente.setEmail("cliente.teste@email.com");
        cliente.setTelefone("11888888888");
        cliente.setAtivo(true);
        cliente.setIdVendedor(idVendedorGerado);
        cliente.setDataCadastro(LocalDateTime.now().minusSeconds(1));
        clienteDAO.inserir(cliente);
        idClienteGerado = cliente.getIdPessoa();

        Categoria categoria = new Categoria();
        categoria.setNome("Categoria Teste");
        categoria.setDescricao("Descrição categoria teste");
        categoria.setIdVendedor(idVendedorGerado);
        categoriaDAO.inserir(categoria);
        idCategoriaGerada = categoria.getIdCategoria();

        Produto produto = new Produto();
        produto.setNome("Produto Teste");
        produto.setDescricao("Descrição produto teste");
        produto.setPrecoUnitario(50.00);
        produto.setEstoque(100);
        produto.setAtivo(true);
        produto.setIdVendedor(idVendedorGerado);
        produto.setIdCategoria(idCategoriaGerada);
        produtoDAO.inserir(produto);
        idProdutoGerado = produto.getIdProduto();

        Pedido pedido = new Pedido();
        pedido.setIdCliente(idClienteGerado);
        pedido.setIdVendedor(idVendedorGerado);
        pedido.setDataEmissao(LocalDateTime.now().minusSeconds(1));
        pedido.setValorTotal(100.00);
        pedidoDAO.inserir(pedido);
        idPedidoGerado = pedido.getIdPedido();
    }

    @AfterEach
    public void tearDown() {
        limparBanco();
    }

    private static void limparBanco() {
        try (Connection conn = ConexaoBD.conectar(); java.sql.Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("SET FOREIGN_KEY_CHECKS = 0");
            stmt.executeUpdate("TRUNCATE TABLE item_pedido");
            stmt.executeUpdate("TRUNCATE TABLE historico_status_pedido");
            stmt.executeUpdate("TRUNCATE TABLE pagamento");
            stmt.executeUpdate("TRUNCATE TABLE pedido");
            stmt.executeUpdate("TRUNCATE TABLE fornecedor_produto");
            stmt.executeUpdate("TRUNCATE TABLE produto");
            stmt.executeUpdate("TRUNCATE TABLE categoria");
            stmt.executeUpdate("TRUNCATE TABLE cliente");
            stmt.executeUpdate("TRUNCATE TABLE pessoa");
            stmt.executeUpdate("TRUNCATE TABLE vendedor");
            stmt.executeUpdate("TRUNCATE TABLE fornecedor");
            stmt.executeUpdate("SET FOREIGN_KEY_CHECKS = 1");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testInserir() {
        System.out.println("testInserir");
        Pagamento pagamento = new Pagamento();
        pagamento.setIdPedido(idPedidoGerado);
        pagamento.setFormaPagamento("PIX");
        pagamento.setDataHora(LocalDateTime.now().minusSeconds(1));
        pagamento.setValor(100.00);
        pagamento.setStatus("APROVADO");

        pagamentoDAO.inserir(pagamento);

        assertTrue(pagamento.getIdPagamento() > 0, "O ID do pagamento deve ser gerado após a inserção.");
    }

    @Test
    public void testListar() {
        System.out.println("testListar");
        Pagamento pagamento = new Pagamento();
        pagamento.setIdPedido(idPedidoGerado);
        pagamento.setFormaPagamento("CARTAO_CREDITO");
        pagamento.setDataHora(LocalDateTime.now().minusSeconds(1));
        pagamento.setValor(100.00);
        pagamento.setStatus("PROCESSANDO");
        pagamentoDAO.inserir(pagamento);

        List<Pagamento> lista = pagamentoDAO.listar();
        assertNotNull(lista);
        assertFalse(lista.isEmpty(), "A lista de pagamentos não deveria estar vazia.");
    }

    @Test
    public void testAtualizar() {
        System.out.println("testAtualizar");
        Pagamento pagamento = new Pagamento();
        pagamento.setIdPedido(idPedidoGerado);
        pagamento.setFormaPagamento("BOLETO");
        pagamento.setDataHora(LocalDateTime.now().minusSeconds(1));
        pagamento.setValor(100.00);
        pagamento.setStatus("PROCESSANDO");
        pagamentoDAO.inserir(pagamento);

        pagamento.setStatus("APROVADO");
        pagamento.setDataHora(LocalDateTime.now().minusSeconds(1)); // Garante que não está no "futuro" devido a pequenas variações de relógio
        pagamentoDAO.atualizar(pagamento);

        List<Pagamento> lista = pagamentoDAO.listar();
        boolean atualizado = lista.stream().anyMatch(p -> p.getIdPagamento() == pagamento.getIdPagamento() && "APROVADO".equals(p.getStatus()));
        assertTrue(atualizado, "O status do pagamento deveria ter sido atualizado para APROVADO.");
    }

    @Test
    public void testDeletar() {
        System.out.println("testDeletar");
        Pagamento pagamento = new Pagamento();
        pagamento.setIdPedido(idPedidoGerado);
        pagamento.setFormaPagamento("DINHEIRO");
        pagamento.setDataHora(LocalDateTime.now().minusSeconds(1));
        pagamento.setValor(50.00);
        pagamento.setStatus("APROVADO");
        pagamentoDAO.inserir(pagamento);

        int idPagamento = pagamento.getIdPagamento();
        pagamentoDAO.deletar(idPagamento);

        List<Pagamento> lista = pagamentoDAO.listar();
        boolean existe = lista.stream().anyMatch(p -> p.getIdPagamento() == idPagamento);
        assertFalse(existe, "O pagamento deveria ter sido excluído do banco de dados.");
    }
}
