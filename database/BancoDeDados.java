package database;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import models.Cliente;
import models.Pedido;
import models.Produto;

public class BancoDeDados {

    /* Coleções de Dados */
    private static final List<Cliente> clientes = new ArrayList<>();
    private static final List<Produto> produtos = new ArrayList<>();
    private static final List<Pedido> pedidos = new ArrayList<>();

    /* Contadores Globais */
    private static int ultimoIdCliente = 0;
    private static int ultimoIdProduto = 0;
    private static int ultimoIdPedido = 0;

    private BancoDeDados() {
    }

    /* Métodos para Gerar Novo ID */
    public static int gerarIdCliente() {
        return ++ultimoIdCliente;
    }

    public static int gerarIdProduto() {
        return ++ultimoIdProduto;
    }

    public static int gerarIdPedido() {
        return ++ultimoIdPedido;
    }

    /* Getters Protegidos */
    public static List<Cliente> getClientes() {
        return Collections.unmodifiableList(clientes);
    }

    public static List<Produto> getProdutos() {
        return Collections.unmodifiableList(produtos);
    }

    public static List<Pedido> getPedidos() {
        return Collections.unmodifiableList(pedidos);
    }

    /* Processo de Adição */
    public static void adicionarCliente(Cliente cliente) {
        if (cliente == null) {
            throw new IllegalArgumentException("Cliente não pode ser nulo.");
        }
        clientes.add(cliente);
    }

    public static void adicionarProduto(Produto produto) {
        if (produto == null) {
            throw new IllegalArgumentException("Produto não pode ser nulo.");
        }
        produtos.add(produto);
    }

    public static void adicionarPedido(Pedido pedido) {
        if (pedido == null) {
            throw new IllegalArgumentException("Pedido não pode ser nulo.");
        }
        pedidos.add(pedido);
    }

    /* Buscar Cliente */
    public static Cliente buscarClientePorId(int id) {
        for (Cliente cliente : clientes) {
            if (cliente.getIdCliente() == id) {
                return cliente;
            }
        }
        return null;
    }

    public static Cliente buscarClientePorCpf(String cpf) {
        if (cpf == null || cpf.trim().isEmpty()) {
            return null;
        }
        String cpfLimpo = cpf.replaceAll("\\D", "");
        for (Cliente cliente : clientes) {
            if (cliente.getCpf().equals(cpfLimpo)) {
                return cliente;
            }
        }
        return null;
    }

    /* Buscar Produto */
    public static Produto buscarProdutoPorId(int id) {
        for (Produto produto : produtos) {
            if (produto.getIdProduto() == id) {
                return produto;
            }
        }
        return null;
    }

    /* Buscar Pedido */
    public static Pedido buscarPedidoPorId(int id) {
        for (Pedido pedido : pedidos) {
            if (pedido.getIdPedido() == id) {
                return pedido;
            }
        }
        return null;
    }

    public static List<Pedido> buscarPedidosPorCpf(String cpf) {
        List<Pedido> pedidosCliente = new ArrayList<>();
        if (cpf == null || cpf.trim().isEmpty()) {
            return pedidosCliente;
        }
        String cpfLimpo = cpf.replaceAll("\\D", "");
        for (Pedido pedido : pedidos) {
            if (pedido.getCliente() != null && pedido.getCliente().getCpf().equals(cpfLimpo)) {
                pedidosCliente.add(pedido);
            }
        }
        return pedidosCliente;
    }

    /* Processo de Exclusão */
    public static boolean removerProduto(int idProduto) {
        Produto produto = buscarProdutoPorId(idProduto);
        if (produto == null) {
            return false;
        }
        return produtos.remove(produto);
    }

    public static boolean removerPedido(int idPedido) {
        Pedido pedido = buscarPedidoPorId(idPedido);
        if (pedido == null) {
            return false;
        }
        return pedidos.remove(pedido);
    }

    /* Processo de Desativação */
    public static boolean desativarCliente(int idCliente) {
        Cliente cliente = buscarClientePorId(idCliente);
        if (cliente == null) {
            return false;
        }
        cliente.desativar();
        return true;
    }
}
