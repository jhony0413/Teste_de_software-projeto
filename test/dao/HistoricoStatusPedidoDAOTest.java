package dao;

import database.ConexaoBD;
import java.sql.Connection;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.List;
import models.Cliente;
import models.HistoricoStatusPedido;
import models.Pedido;
import models.Vendedor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class HistoricoStatusPedidoDAOTest {

    private VendedorDAO vendedorDAO;
    private ClienteDAO clienteDAO;
    private PedidoDAO pedidoDAO;
    private HistoricoStatusPedidoDAO historicoDAO;

    private Vendedor vendedor;
    private Cliente cliente;
    private Pedido pedido;

    @BeforeEach
    public void setUp() {
        vendedorDAO = new VendedorDAO();
        clienteDAO = new ClienteDAO();
        pedidoDAO = new PedidoDAO();
        historicoDAO = new HistoricoStatusPedidoDAO();

        limparBanco();

        vendedor = new Vendedor(
                0,
                "Vendedor Teste",
                "12345678901",
                "vendedor@teste.com",
                "11988887777",
                true,
                "vendtest",
                "senha123"
        );
        vendedorDAO.inserir(vendedor);

        cliente = new Cliente(
                0,
                vendedor.getIdPessoa(),
                "Cliente Teste",
                "98765432109",
                "cliente@teste.com",
                "11977776666",
                true,
                LocalDateTime.now().minusSeconds(1)
        );
        clienteDAO.inserir(cliente);

        pedido = new Pedido(
                0,
                cliente.getIdPessoa(),
                vendedor.getIdPessoa(),
                LocalDateTime.now().minusSeconds(1).minusSeconds(5),
                150.00
        );
        pedidoDAO.inserir(pedido);
    }

    @AfterEach
    public void tearDown() {

        limparBanco();
    }

    private void limparBanco() {
        try (Connection conn = ConexaoBD.conectar(); Statement stmt = conn.createStatement()) {
            stmt.execute("SET FOREIGN_KEY_CHECKS = 0;");
            stmt.execute("TRUNCATE TABLE historico_status_pedido;");
            stmt.execute("TRUNCATE TABLE pagamento;");
            stmt.execute("TRUNCATE TABLE item_pedido;");
            stmt.execute("TRUNCATE TABLE pedido;");
            stmt.execute("TRUNCATE TABLE fornecedor_produto;");
            stmt.execute("TRUNCATE TABLE produto;");
            stmt.execute("TRUNCATE TABLE categoria;");
            stmt.execute("TRUNCATE TABLE cliente;");
            stmt.execute("TRUNCATE TABLE pessoa;");
            stmt.execute("TRUNCATE TABLE vendedor;");
            stmt.execute("TRUNCATE TABLE fornecedor;");
            stmt.execute("SET FOREIGN_KEY_CHECKS = 1;");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testInserirEListarPorPedido() {
        LocalDateTime dataAlteracao = LocalDateTime.now().minusSeconds(1).minusSeconds(2);
        HistoricoStatusPedido historico = new HistoricoStatusPedido(
                pedido.getIdPedido(),
                dataAlteracao,
                "PENDENTE"
        );

        historicoDAO.inserir(historico);

        List<HistoricoStatusPedido> lista = historicoDAO.listarPorPedido(pedido.getIdPedido());
        assertNotNull(lista);
        assertFalse(lista.isEmpty());
        assertEquals(pedido.getIdPedido(), lista.get(0).getIdPedido());
        assertEquals("PENDENTE", lista.get(0).getStatus());
    }

    @Test
    public void testAtualizar() {
        LocalDateTime dataAlteracao = LocalDateTime.now().minusSeconds(1).minusSeconds(2);
        HistoricoStatusPedido historico = new HistoricoStatusPedido(
                pedido.getIdPedido(),
                dataAlteracao,
                "PENDENTE"
        );
        historicoDAO.inserir(historico);

        historico.setStatus("PAGO");

        List<HistoricoStatusPedido> listaAntes = historicoDAO.listarPorPedido(pedido.getIdPedido());
        HistoricoStatusPedido historicoSalvo = listaAntes.get(0);

        historicoSalvo.setStatus("PAGO");
        historicoDAO.atualizar(historicoSalvo);

        List<HistoricoStatusPedido> lista = historicoDAO.listarPorPedido(pedido.getIdPedido());
        assertEquals(1, lista.size());
        assertEquals("PAGO", lista.get(0).getStatus());
    }

    @Test
    public void testDeletarPorPedido() {
        LocalDateTime dataAlteracao = LocalDateTime.now().minusSeconds(1).minusSeconds(2);
        HistoricoStatusPedido historico = new HistoricoStatusPedido(
                pedido.getIdPedido(),
                dataAlteracao,
                "PENDENTE"
        );
        historicoDAO.inserir(historico);

        historicoDAO.deletarPorPedido(pedido.getIdPedido());

        List<HistoricoStatusPedido> lista = historicoDAO.listarPorPedido(pedido.getIdPedido());
        assertTrue(lista.isEmpty());
    }
}
