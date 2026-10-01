package dao;

import java.time.LocalDateTime;
import java.util.List;
import models.Categoria;
import models.Cliente;
import models.ItemPedido;
import models.Pedido;
import models.Produto;
import models.Vendedor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ItemPedidoDAOTest {

    private VendedorDAO vendedorDAO;
    private ClienteDAO clienteDAO;
    private CategoriaDAO categoriaDAO;
    private ProdutoDAO produtoDAO;
    private PedidoDAO pedidoDAO;
    private ItemPedidoDAO itemPedidoDAO;

    private int vendedorId;
    private int clienteId;
    private int categoriaId;
    private int produtoId;
    private int pedidoId;

    @BeforeEach
    public void setUp() {
        vendedorDAO = new VendedorDAO();
        clienteDAO = new ClienteDAO();
        categoriaDAO = new CategoriaDAO();
        produtoDAO = new ProdutoDAO();
        pedidoDAO = new PedidoDAO();
        itemPedidoDAO = new ItemPedidoDAO();

        Vendedor vendedor = new Vendedor();
        vendedor.setNome("Vendedor Teste");
        vendedor.setCpf("12345678901");
        vendedor.setEmail("vendedor@teste.com");
        vendedor.setTelefone("11999999999");
        vendedor.setAtivo(true);
        vendedor.setLogin("vendtest");
        vendedor.setSenhaHash("senha123");
        vendedorDAO.inserir(vendedor);
        vendedorId = vendedor.getIdPessoa();

        Cliente cliente = new Cliente();
        cliente.setNome("Cliente Teste");
        cliente.setCpf("98765432100");
        cliente.setEmail("cliente@teste.com");
        cliente.setTelefone("11888888888");
        cliente.setAtivo(true);
        cliente.setIdVendedor(vendedorId);
        cliente.setDataCadastro(LocalDateTime.now().minusSeconds(1));
        clienteDAO.inserir(cliente);
        clienteId = cliente.getIdPessoa();

        Categoria categoria = new Categoria();
        categoria.setIdVendedor(vendedorId);
        categoria.setNome("Eletrônicos");
        categoria.setDescricao("Categoria de produtos eletrônicos");
        categoriaDAO.inserir(categoria);
        categoriaId = categoria.getIdCategoria();

        Produto produto = new Produto();
        produto.setIdVendedor(vendedorId);
        produto.setIdCategoria(categoriaId);
        produto.setNome("Smartphone");
        produto.setDescricao("Smartphone moderno");
        produto.setPrecoUnitario(1500.00);
        produto.setEstoque(10);
        produto.setAtivo(true);
        produtoDAO.inserir(produto);
        produtoId = produto.getIdProduto();

        Pedido pedido = new Pedido();
        pedido.setIdCliente(clienteId);
        pedido.setIdVendedor(vendedorId);
        pedido.setDataEmissao(LocalDateTime.now().minusSeconds(1));
        pedido.setValorTotal(1500.00);
        pedidoDAO.inserir(pedido);
        pedidoId = pedido.getIdPedido();
    }

    @AfterEach
    public void tearDown() {

        try {
            itemPedidoDAO.deletar(pedidoId, produtoId);
            pedidoDAO.deletar(pedidoId, vendedorId);
            produtoDAO.deletar(produtoId, vendedorId);
            categoriaDAO.deletar(categoriaId, vendedorId);
            clienteDAO.deletar(clienteId, vendedorId);
            vendedorDAO.deletar(vendedorId);
        } catch (Exception e) {

        }
    }

    @Test
    public void testInserirEListarPorPedido() {
        ItemPedido item = new ItemPedido();
        item.setIdPedido(pedidoId);
        item.setIdProduto(produtoId);
        item.setQuantidade(2);
        item.setPrecoUnitario(1500.00);

        itemPedidoDAO.inserir(item);

        List<ItemPedido> itens = itemPedidoDAO.listarPorPedido(pedidoId);
        assertFalse(itens.isEmpty(), "A lista de itens do pedido não deveria estar vazia.");
        assertEquals(1, itens.size());
        assertEquals(produtoId, itens.get(0).getIdProduto());
        assertEquals(2, itens.get(0).getQuantidade());
        assertEquals(3000.00, itens.get(0).getSubtotal(), 0.001);
    }

    @Test
    public void testAtualizar() {
        ItemPedido item = new ItemPedido();
        item.setIdPedido(pedidoId);
        item.setIdProduto(produtoId);
        item.setQuantidade(1);
        item.setPrecoUnitario(1500.00);
        itemPedidoDAO.inserir(item);

        item.setQuantidade(3);
        itemPedidoDAO.atualizar(item);

        List<ItemPedido> itens = itemPedidoDAO.listarPorPedido(pedidoId);
        assertEquals(3, itens.get(0).getQuantidade());
        assertEquals(4500.00, itens.get(0).getSubtotal(), 0.001);
    }

    @Test
    public void testDeletar() {
        ItemPedido item = new ItemPedido();
        item.setIdPedido(pedidoId);
        item.setIdProduto(produtoId);
        item.setQuantidade(1);
        item.setPrecoUnitario(1500.00);
        itemPedidoDAO.inserir(item);

        itemPedidoDAO.deletar(pedidoId, produtoId);

        List<ItemPedido> itens = itemPedidoDAO.listarPorPedido(pedidoId);
        assertTrue(itens.isEmpty(), "O item do pedido deveria ter sido deletado.");
    }
}
