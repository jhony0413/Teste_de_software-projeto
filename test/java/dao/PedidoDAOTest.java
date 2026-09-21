/*
 * Click nbfs:
 * Click nbfs:
 */
package dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import database.ConexaoBD;
import models.Categoria;
import models.Cliente;
import models.HistoricoStatusPedido;
import models.ItemPedido;
import models.Pagamento;
import models.Pedido;
import models.Produto;
import models.Vendedor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PedidoDAOTest {

    private Vendedor vendedorTeste;
    private Cliente clienteTeste;
    private Categoria categoriaTeste;
    private Produto produtoTeste;

    public PedidoDAOTest() {
    }

    @BeforeAll
    public static void setUpClass() {
    }

    @AfterAll
    public static void tearDownClass() {
    }

    @BeforeEach
    public void setUp() {
        limparBanco();

        vendedorTeste = new Vendedor();
        vendedorTeste.setNome("Vendedor Teste");
        vendedorTeste.setCpf("12345678901");
        vendedorTeste.setEmail("vendedor@teste.com");
        vendedorTeste.setTelefone("11999999999");
        vendedorTeste.setAtivo(true);
        vendedorTeste.setLogin("vend_teste");
        vendedorTeste.setSenhaHash("senha123");

        VendedorDAO vendedorDAO = new VendedorDAO();
        vendedorDAO.inserir(vendedorTeste);

        clienteTeste = new Cliente();
        clienteTeste.setNome("Cliente Teste");
        clienteTeste.setCpf("98765432109");
        clienteTeste.setEmail("cliente@teste.com");
        clienteTeste.setTelefone("11888888888");
        clienteTeste.setAtivo(true);
        clienteTeste.setIdVendedor(vendedorTeste.getIdPessoa());
        clienteTeste.setDataCadastro(LocalDateTime.now().minusSeconds(1));

        ClienteDAO clienteDAO = new ClienteDAO();
        clienteDAO.inserir(clienteTeste);

        categoriaTeste = new Categoria();
        categoriaTeste.setIdVendedor(vendedorTeste.getIdPessoa());
        categoriaTeste.setNome("Categoria Teste");
        categoriaTeste.setDescricao("Descrição categoria teste");
        categoriaTeste.setIdCategoriaPai(null);

        CategoriaDAO categoriaDAO = new CategoriaDAO();
        categoriaDAO.inserir(categoriaTeste);

        produtoTeste = new Produto();
        produtoTeste.setIdVendedor(vendedorTeste.getIdPessoa());
        produtoTeste.setIdCategoria(categoriaTeste.getIdCategoria());
        produtoTeste.setNome("Produto Teste");
        produtoTeste.setDescricao("Descrição produto teste");
        produtoTeste.setPrecoUnitario(50.0);
        produtoTeste.setEstoque(100);
        produtoTeste.setAtivo(true);

        ProdutoDAO produtoDAO = new ProdutoDAO();
        produtoDAO.inserir(produtoTeste);
    }

    @AfterEach
    public void tearDown() {
        limparBanco();
    }

    private void limparBanco() {
        try (Connection conn = ConexaoBD.conectar(); Statement stmt = conn.createStatement()) {
            stmt.execute("SET FOREIGN_KEY_CHECKS = 0;");
            stmt.execute("TRUNCATE TABLE item_pedido;");
            stmt.execute("TRUNCATE TABLE historico_status_pedido;");
            stmt.execute("TRUNCATE TABLE pagamento;");
            stmt.execute("TRUNCATE TABLE pedido;");
            stmt.execute("TRUNCATE TABLE fornecedor_produto;");
            stmt.execute("TRUNCATE TABLE produto;");
            stmt.execute("TRUNCATE TABLE categoria;");
            stmt.execute("TRUNCATE TABLE cliente;");
            stmt.execute("TRUNCATE TABLE pessoa;");
            stmt.execute("TRUNCATE TABLE vendedor;");
            stmt.execute("TRUNCATE TABLE fornecedor;");
            stmt.execute("SET FOREIGN_KEY_CHECKS = 1;");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testInserir() {
        System.out.println("inserir");
        Pedido pedido = new Pedido();
        pedido.setIdCliente(clienteTeste.getIdPessoa());
        pedido.setIdVendedor(vendedorTeste.getIdPessoa());
        pedido.setDataEmissao(LocalDateTime.now().minusSeconds(1));
        pedido.setValorTotal(100.0);

        PedidoDAO instance = new PedidoDAO();
        instance.inserir(pedido);

        assertTrue(pedido.getIdPedido() > 0, "O ID do pedido deve ser gerado após a inserção.");
    }

    @Test
    public void testListarPorVendedor() {
        System.out.println("listarPorVendedor");
        Pedido pedido = new Pedido();
        pedido.setIdCliente(clienteTeste.getIdPessoa());
        pedido.setIdVendedor(vendedorTeste.getIdPessoa());
        pedido.setDataEmissao(LocalDateTime.now().minusSeconds(1));
        pedido.setValorTotal(100.0);

        PedidoDAO instance = new PedidoDAO();
        instance.inserir(pedido);

        List<Pedido> result = instance.listarPorVendedor(vendedorTeste.getIdPessoa());
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    public void testAtualizar() {
        System.out.println("atualizar");
        Pedido pedido = new Pedido();
        pedido.setIdCliente(clienteTeste.getIdPessoa());
        pedido.setIdVendedor(vendedorTeste.getIdPessoa());
        pedido.setDataEmissao(LocalDateTime.now().minusSeconds(1));
        pedido.setValorTotal(100.0);

        PedidoDAO instance = new PedidoDAO();
        instance.inserir(pedido);

        pedido.setDataEmissao(LocalDateTime.now().minusSeconds(1));
        pedido.setValorTotal(150.0);
        instance.atualizar(pedido);

        List<Pedido> lista = instance.listarPorVendedor(vendedorTeste.getIdPessoa());
        assertEquals(150.0, lista.get(0).getValorTotal(), 0.01);
    }

    @Test
    public void testDeletar() {
        System.out.println("deletar");
        Pedido pedido = new Pedido();
        pedido.setIdCliente(clienteTeste.getIdPessoa());
        pedido.setIdVendedor(vendedorTeste.getIdPessoa());
        pedido.setDataEmissao(LocalDateTime.now().minusSeconds(1));
        pedido.setValorTotal(100.0);

        PedidoDAO instance = new PedidoDAO();
        instance.inserir(pedido);

        instance.deletar(pedido.getIdPedido(), vendedorTeste.getIdPessoa());
        List<Pedido> lista = instance.listarPorVendedor(vendedorTeste.getIdPessoa());
        assertTrue(lista.isEmpty());
    }

    @Test
    public void testInserirPedidoCompleto() throws Exception {
        System.out.println("inserirPedidoCompleto");

        Pedido pedido = new Pedido();
        pedido.setIdCliente(clienteTeste.getIdPessoa());
        pedido.setIdVendedor(vendedorTeste.getIdPessoa());
        pedido.setDataEmissao(LocalDateTime.now().minusSeconds(1));
        pedido.setValorTotal(100.0);

        List<ItemPedido> itens = new ArrayList<>();
        ItemPedido item = new ItemPedido();
        item.setIdProduto(produtoTeste.getIdProduto());
        item.setQuantidade(2);
        item.setPrecoUnitario(50.0);
        itens.add(item);

        List<Pagamento> pagamentos = new ArrayList<>();
        Pagamento pagamento = new Pagamento();
        pagamento.setFormaPagamento("PIX");
        pagamento.setDataHora(LocalDateTime.now().minusSeconds(1));
        pagamento.setValor(100.0);
        pagamento.setStatus("APROVADO");
        pagamentos.add(pagamento);

        HistoricoStatusPedido historico = new HistoricoStatusPedido();
        historico.setDataAlteracao(LocalDateTime.now().minusSeconds(1));
        historico.setStatus("PENDENTE");

        PedidoDAO instance = new PedidoDAO();
        instance.inserirPedidoCompleto(pedido, itens, pagamentos, historico);

        assertTrue(pedido.getIdPedido() > 0, "O ID do pedido completo deve ser gerado.");

        List<Pedido> lista = instance.listarPorVendedor(vendedorTeste.getIdPessoa());
        assertEquals(1, lista.size());
    }

    @Test
    public void testAtualizarPedidoCompleto() throws Exception {
        System.out.println("atualizarPedidoCompleto");

        Pedido pedido = new Pedido();
        pedido.setIdCliente(clienteTeste.getIdPessoa());
        pedido.setIdVendedor(vendedorTeste.getIdPessoa());
        pedido.setDataEmissao(LocalDateTime.now().minusSeconds(1));
        pedido.setValorTotal(50.0);

        List<ItemPedido> itens = new ArrayList<>();
        ItemPedido item = new ItemPedido();
        item.setIdProduto(produtoTeste.getIdProduto());
        item.setQuantidade(1);
        item.setPrecoUnitario(50.0);
        itens.add(item);

        List<Pagamento> pagamentos = new ArrayList<>();
        Pagamento pagamento = new Pagamento();
        pagamento.setFormaPagamento("DINHEIRO");
        pagamento.setDataHora(LocalDateTime.now().minusSeconds(1));
        pagamento.setValor(50.0);
        pagamento.setStatus("APROVADO");
        pagamentos.add(pagamento);

        HistoricoStatusPedido historico = new HistoricoStatusPedido();
        historico.setDataAlteracao(LocalDateTime.now().minusSeconds(1));
        historico.setStatus("PENDENTE");

        PedidoDAO instance = new PedidoDAO();
        instance.inserirPedidoCompleto(pedido, itens, pagamentos, historico);

        Thread.sleep(1000);

        pedido.setDataEmissao(LocalDateTime.now().minusSeconds(1));
        pedido.setValorTotal(50.0);
        HistoricoStatusPedido novoHistorico = new HistoricoStatusPedido();
        novoHistorico.setDataAlteracao(LocalDateTime.now().minusSeconds(1));
        novoHistorico.setStatus("PAGO");

        instance.atualizarPedidoCompleto(pedido, itens, pagamentos, novoHistorico);

        List<Pedido> lista = instance.listarPorVendedor(vendedorTeste.getIdPessoa());
        assertFalse(lista.isEmpty());
    }

    @Test
    public void testDeletarPedidoCompleto() throws Exception {
        System.out.println("deletarPedidoCompleto");

        Pedido pedido = new Pedido();
        pedido.setIdCliente(clienteTeste.getIdPessoa());
        pedido.setIdVendedor(vendedorTeste.getIdPessoa());
        pedido.setDataEmissao(LocalDateTime.now().minusSeconds(1));
        pedido.setValorTotal(50.0);

        List<ItemPedido> itens = new ArrayList<>();
        ItemPedido item = new ItemPedido();
        item.setIdProduto(produtoTeste.getIdProduto());
        item.setQuantidade(1);
        item.setPrecoUnitario(50.0);
        itens.add(item);

        List<Pagamento> pagamentos = new ArrayList<>();
        Pagamento pagamento = new Pagamento();
        pagamento.setFormaPagamento("PIX");
        pagamento.setDataHora(LocalDateTime.now().minusSeconds(1));
        pagamento.setValor(50.0);
        pagamento.setStatus("APROVADO");
        pagamentos.add(pagamento);

        HistoricoStatusPedido historico = new HistoricoStatusPedido();
        historico.setDataAlteracao(LocalDateTime.now().minusSeconds(1));
        historico.setStatus("PENDENTE");

        PedidoDAO instance = new PedidoDAO();
        instance.inserirPedidoCompleto(pedido, itens, pagamentos, historico);

        instance.deletarPedidoCompleto(pedido.getIdPedido(), vendedorTeste.getIdPessoa());

        List<Pedido> lista = instance.listarPorVendedor(vendedorTeste.getIdPessoa());
        assertTrue(lista.isEmpty());
    }
}
