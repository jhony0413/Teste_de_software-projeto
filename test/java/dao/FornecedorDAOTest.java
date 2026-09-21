package dao;

import java.util.List;
import models.Fornecedor;
import models.Vendedor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import database.ConexaoBD;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;

public class FornecedorDAOTest {

    private VendedorDAO vendedorDAO;
    private FornecedorDAO fornecedorDAO;
    private Vendedor vendedorTeste;

    public FornecedorDAOTest() {
        vendedorDAO = new VendedorDAO();
        fornecedorDAO = new FornecedorDAO();
    }

    @BeforeEach
    public void setUp() {

        limparBanco();

        vendedorTeste = new Vendedor();
        vendedorTeste.setNome("Empresa Teste LTDA");
        vendedorTeste.setCpf("12345678909");
        vendedorTeste.setEmail("contato@empresateste.com");
        vendedorTeste.setTelefone("11999999999");
        vendedorTeste.setAtivo(true);
        vendedorTeste.setLogin("vendedor_teste");
        vendedorTeste.setSenhaHash("senha123secure");

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

        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setIdVendedor(vendedorTeste.getIdPessoa());
        fornecedor.setCnpj("12345678000199");
        fornecedor.setNome("Fornecedor Global S/A");
        fornecedor.setEmail("suporte@fornecedorglobal.com");
        fornecedor.setTelefone("1133334444");
        fornecedor.setAtivo(true);

        fornecedorDAO.inserir(fornecedor);

        assertTrue(fornecedor.getIdFornecedor() > 0, "O ID do fornecedor deveria ter sido gerado.");
    }

    @Test
    public void testListarPorVendedor() {
        System.out.println("listarPorVendedor");

        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setIdVendedor(vendedorTeste.getIdPessoa());
        fornecedor.setCnpj("98765432000188");
        fornecedor.setNome("Distribuidora Alpha");
        fornecedor.setEmail("alpha@distribuidora.com");
        fornecedor.setTelefone("1155556666");
        fornecedor.setAtivo(true);

        fornecedorDAO.inserir(fornecedor);

        List<Fornecedor> resultado = fornecedorDAO.listarPorVendedor(vendedorTeste.getIdPessoa());

        assertNotNull(resultado);
        assertFalse(resultado.isEmpty(), "A lista de fornecedores não deveria estar vazia.");
        assertEquals(1, resultado.size());
        assertEquals("Distribuidora Alpha", resultado.get(0).getNome());
    }

    @Test
    public void testAtualizar() {
        System.out.println("atualizar");

        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setIdVendedor(vendedorTeste.getIdPessoa());
        fornecedor.setCnpj("11222333000144");
        fornecedor.setNome("Nome Antigo");
        fornecedor.setEmail("antigo@email.com");
        fornecedor.setTelefone("1122223333");
        fornecedor.setAtivo(true);

        fornecedorDAO.inserir(fornecedor);

        fornecedor.setNome("Nome Atualizado");
        fornecedor.setEmail("atualizado@email.com");
        fornecedorDAO.atualizar(fornecedor);

        List<Fornecedor> lista = fornecedorDAO.listarPorVendedor(vendedorTeste.getIdPessoa());
        assertEquals("Nome Atualizado", lista.get(0).getNome());
        assertEquals("atualizado@email.com", lista.get(0).getEmail());
    }

    @Test
    public void testDeletar() {
        System.out.println("deletar");

        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setIdVendedor(vendedorTeste.getIdPessoa());
        fornecedor.setCnpj("55444333000111");
        fornecedor.setNome("Para Deletar");
        fornecedor.setEmail("deletar@email.com");
        fornecedor.setTelefone("1177778888");
        fornecedor.setAtivo(true);

        fornecedorDAO.inserir(fornecedor);

        fornecedorDAO.deletar(fornecedor.getIdFornecedor(), vendedorTeste.getIdPessoa());

        List<Fornecedor> lista = fornecedorDAO.listarPorVendedor(vendedorTeste.getIdPessoa());
        assertTrue(lista.isEmpty(), "O fornecedor deveria ter sido excluído com sucesso.");
    }
}
