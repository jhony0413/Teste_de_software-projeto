package dao;

import java.util.List;
import models.Vendedor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class VendedorDAOTest {

    private VendedorDAO vendedorDAO;

    public VendedorDAOTest() {
    }

    @BeforeEach
    public void setUp() {
        vendedorDAO = new VendedorDAO();
        limparBanco();
    }

    @AfterEach
    public void tearDown() {
        limparBanco();
    }

    private void limparBanco() {
        try (var conn = database.ConexaoBD.conectar(); var stmt = conn.createStatement()) {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testInserir() {
        System.out.println("inserir");

        Vendedor vendedor = new Vendedor();
        vendedor.setNome("Carlos Silva");
        vendedor.setCpf("12345678901");
        vendedor.setEmail("carlos.silva@email.com");
        vendedor.setTelefone("11988887777");
        vendedor.setAtivo(true);
        vendedor.setLogin("csilva");
        vendedor.setSenhaHash("senha123");

        vendedorDAO.inserir(vendedor);

        assertTrue(vendedor.getIdPessoa() > 0, "O ID do vendedor deve ser gerado após a inserção.");

        List<Vendedor> lista = vendedorDAO.listar();
        assertEquals(1, lista.size(), "Deveria existir exatamente um vendedor cadastrado.");
        assertEquals("Carlos Silva", lista.get(0).getNome());
    }

    @Test
    public void testListar() {
        System.out.println("listar");

        Vendedor v1 = new Vendedor(0, "Ana Souza", "11144477735", "ana@email.com", "11999998888", true, "asouza", "senha123");
        Vendedor v2 = new Vendedor(0, "Bruno Lima", "22255588846", "bruno@email.com", "11977776666", true, "blima", "senha123");

        vendedorDAO.inserir(v1);
        vendedorDAO.inserir(v2);

        List<Vendedor> resultado = vendedorDAO.listar();

        assertNotNull(resultado);
        assertEquals(2, resultado.size(), "A lista deve conter dois vendedores.");
    }

    @Test
    public void testAtualizar() {
        System.out.println("atualizar");

        Vendedor vendedor = new Vendedor(0, "Daniela Costa", "33366699957", "daniela@email.com", "11966665555", true, "dcosta", "senha123");
        vendedorDAO.inserir(vendedor);

        vendedor.setNome("Daniela Costa Atualizada");
        vendedor.setEmail("daniela.nova@email.com");
        vendedor.setLogin("dcostanova");

        vendedorDAO.atualizar(vendedor);

        List<Vendedor> lista = vendedorDAO.listar();
        assertEquals(1, lista.size());
        assertEquals("Daniela Costa Atualizada", lista.get(0).getNome());
        assertEquals("daniela.nova@email.com", lista.get(0).getEmail());
        assertEquals("dcostanova", lista.get(0).getLogin());
    }

    @Test
    public void testDeletar() {
        System.out.println("deletar");

        Vendedor vendedor = new Vendedor(0, "Eduardo Santos", "44477700068", "eduardo@email.com", "11955554444", true, "esantos", "senha123");
        vendedorDAO.inserir(vendedor);

        int idVendedor = vendedor.getIdPessoa();

        vendedorDAO.deletar(idVendedor);

        List<Vendedor> lista = vendedorDAO.listar();
        assertTrue(lista.isEmpty(), "A lista de vendedores deveria estar vazia após a exclusão.");
    }

    @Test
    public void testAutenticar() {
        System.out.println("autenticar");

        Vendedor vendedor = new Vendedor(0, "Fernanda Dias", "55588811179", "fernanda@email.com", "11944443333", true, "fdias", "senhaSegura123");
        vendedorDAO.inserir(vendedor);

        Vendedor autenticado = vendedorDAO.autenticar("fdias", "senhaSegura123");
        assertNotNull(autenticado, "A autenticação deveria ser bem-sucedida com login e senha corretos.");
        assertEquals("Fernanda Dias", autenticado.getNome());

        Vendedor falhaSenha = vendedorDAO.autenticar("fdias", "senhaErrada");
        assertNull(falhaSenha, "A autenticação deve retornar null para senha incorreta.");

        Vendedor falhaLogin = vendedorDAO.autenticar("loginInexistente", "senhaSegura123");
        assertNull(falhaLogin, "A autenticação deve retornar null para login inexistente.");
    }
}
