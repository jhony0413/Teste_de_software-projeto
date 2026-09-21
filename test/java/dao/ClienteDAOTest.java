package dao;

import java.time.LocalDateTime;
import java.util.List;
import models.Cliente;
import models.Vendedor;
import database.ConexaoBD;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ClienteDAOTest {

    private ClienteDAO clienteDAO;
    private VendedorDAO vendedorDAO;
    private Vendedor vendedorTeste;

    public ClienteDAOTest() {
    }

    @BeforeEach
    public void setUp() {
        clienteDAO = new ClienteDAO();
        vendedorDAO = new VendedorDAO();

        limparBanco();

        vendedorTeste = new Vendedor();
        vendedorTeste.setNome("Vendedor Teste");
        vendedorTeste.setCpf("12345678901");
        vendedorTeste.setEmail("vendedor.teste@email.com");
        vendedorTeste.setTelefone("11988887777");
        vendedorTeste.setAtivo(true);
        vendedorTeste.setLogin("vendtest");
        vendedorTeste.setSenhaHash("senha123");

        vendedorDAO.inserir(vendedorTeste);
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

        Cliente cliente = new Cliente();
        cliente.setNome("Cliente Teste");
        cliente.setCpf("98765432100");
        cliente.setEmail("cliente.teste@email.com");
        cliente.setTelefone("11999998888");
        cliente.setAtivo(true);
        cliente.setIdVendedor(vendedorTeste.getIdPessoa());
        cliente.setDataCadastro(LocalDateTime.now().minusSeconds(1));

        clienteDAO.inserir(cliente);

        assertTrue(cliente.getIdPessoa() > 0, "O ID do cliente deve ser gerado após a inserção.");

        List<Cliente> clientes = clienteDAO.listarPorVendedor(vendedorTeste.getIdPessoa());
        assertFalse(clientes.isEmpty(), "A lista de clientes não deveria estar vazia.");
        assertEquals("Cliente Teste", clientes.get(0).getNome());
    }

    @Test
    public void testListarPorVendedor() {
        System.out.println("listarPorVendedor");

        Cliente cliente = new Cliente();
        cliente.setNome("Cliente Listar");
        cliente.setCpf("45678912300");
        cliente.setEmail("cliente.listar@email.com");
        cliente.setTelefone("11977776666");
        cliente.setAtivo(true);
        cliente.setIdVendedor(vendedorTeste.getIdPessoa());
        cliente.setDataCadastro(LocalDateTime.now().minusSeconds(1));

        clienteDAO.inserir(cliente);

        List<Cliente> resultado = clienteDAO.listarPorVendedor(vendedorTeste.getIdPessoa());

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Cliente Listar", resultado.get(0).getNome());
    }

    @Test
    public void testAtualizar() {
        System.out.println("atualizar");

        Cliente cliente = new Cliente();
        cliente.setNome("Cliente Antigo");
        cliente.setCpf("11122233344");
        cliente.setEmail("cliente.antigo@email.com");
        cliente.setTelefone("11966665555");
        cliente.setAtivo(true);
        cliente.setIdVendedor(vendedorTeste.getIdPessoa());
        cliente.setDataCadastro(LocalDateTime.now().minusSeconds(1));

        clienteDAO.inserir(cliente);

        cliente.setNome("Cliente Atualizado");
        cliente.setEmail("cliente.atualizado@email.com");
        cliente.setDataCadastro(LocalDateTime.now().minusSeconds(1));
        clienteDAO.atualizar(cliente);

        List<Cliente> resultado = clienteDAO.listarPorVendedor(vendedorTeste.getIdPessoa());
        assertEquals(1, resultado.size());
        assertEquals("Cliente Atualizado", resultado.get(0).getNome());
        assertEquals("cliente.atualizado@email.com", resultado.get(0).getEmail());
    }

    @Test
    public void testDeletar() {
        System.out.println("deletar");

        Cliente cliente = new Cliente();
        cliente.setNome("Cliente Deletar");
        cliente.setCpf("99988877766");
        cliente.setEmail("cliente.deletar@email.com");
        cliente.setTelefone("11955554444");
        cliente.setAtivo(true);
        cliente.setIdVendedor(vendedorTeste.getIdPessoa());
        cliente.setDataCadastro(LocalDateTime.now().minusSeconds(1));

        clienteDAO.inserir(cliente);

        clienteDAO.deletar(cliente.getIdPessoa(), vendedorTeste.getIdPessoa());

        List<Cliente> resultado = clienteDAO.listarPorVendedor(vendedorTeste.getIdPessoa());
        assertTrue(resultado.isEmpty(), "O cliente deveria ter sido removido com sucesso.");
    }
}
