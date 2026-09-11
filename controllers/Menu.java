package views;

import java.util.List;
import models.Cliente;
import models.Pedido;
import models.Produto;
import database.BancoDeDados;
import controllers.Tela;

public class Menu {

    public static void main(String[] args) {
        menuPrincipal();
    }

    /* Menu Principal */
    public static void menuPrincipal() {
        int opcao;
        do {
            String menu = """
                    SISTEMA DE VENDAS
                    1 - Clientes
                    2 - Produtos
                    3 - Pedidos
                    0 - Sair
                    """;
            Tela.imprimirString(menu);
            opcao = Tela.lerInt();
            switch (opcao) {
                case 1 ->
                    menuClientes();
                case 2 ->
                    menuProdutos();
                case 3 ->
                    menuPedidos();
                case 0 ->
                    Tela.imprimirString("Sistema encerrado.\n");
                default ->
                    Tela.imprimirString("Opção inválida.\n");
            }
        } while (opcao != 0);
    }

    /* Menu Clientes */
    public static void menuClientes() {
        int opcao;
        do {
            String menu = """
                    MENU CLIENTES
                    1 - Cadastrar Cliente
                    2 - Listar Clientes
                    3 - Atualizar Cliente
                    4 - Desativar Cliente
                    0 - Voltar
                    """;
            Tela.imprimirString(menu);
            opcao = Tela.lerInt();
            switch (opcao) {
                case 1 ->
                    cadastrarCliente();
                case 2 ->
                    listarClientes();
                case 3 ->
                    atualizarCliente();
                case 4 ->
                    desativarCliente();
                case 0 ->
                    Tela.imprimirString("Voltando...\n");
                default ->
                    Tela.imprimirString("Opção inválida.\n");
            }
        } while (opcao != 0);
    }

    /* Menu Produtos */
    public static void menuProdutos() {
        int opcao;
        do {
            String menu = """
                    MENU PRODUTOS
                    1 - Cadastrar Produto
                    2 - Listar Produtos
                    3 - Atualizar Produto
                    4 - Excluir Produto
                    0 - Voltar
                    """;
            Tela.imprimirString(menu);
            opcao = Tela.lerInt();
            switch (opcao) {
                case 1 ->
                    cadastrarProduto();
                case 2 ->
                    listarProdutos();
                case 3 ->
                    atualizarProduto();
                case 4 ->
                    excluirProduto();
                case 0 ->
                    Tela.imprimirString("Voltando...\n");
                default ->
                    Tela.imprimirString("Opção inválida.\n");
            }
        } while (opcao != 0);
    }

    /* Menu Pedidos */
    public static void menuPedidos() {
        int opcao;
        do {
            String menu = """
                    MENU PEDIDOS
                    1 - Criar Pedido
                    2 - Listar Pedidos
                    3 - Atualizar Pedido
                    4 - Cancelar Pedido
                    5 - Finalizar Pedido
                    0 - Voltar
                    """;
            Tela.imprimirString(menu);
            opcao = Tela.lerInt();
            switch (opcao) {
                case 1 ->
                    cadastrarPedido();
                case 2 ->
                    listarPedidos();
                case 3 ->
                    atualizarPedido();
                case 4 ->
                    cancelarPedido();
                case 5 ->
                    finalizarPedido();
                case 0 ->
                    Tela.imprimirString("Voltando...\n");
                default ->
                    Tela.imprimirString("Opção inválida.\n");
            }
        } while (opcao != 0);
    }

    /* Clientes */
    public static void cadastrarCliente() {
        Tela.imprimirString("Digite o CPF: ");
        String cpf = Tela.lerString();
        Cliente clienteExistente = BancoDeDados.buscarClientePorCpf(cpf);
        if (clienteExistente != null) {
            if (clienteExistente.isAtivo()) {
                Tela.imprimirString("Já existe um cliente ATIVO com este CPF.\n");
                return;
            }
            Tela.imprimirString("""
                    
                    Cliente encontrado, porém INATIVO.

                    Deseja reativá-lo?
                    """);
            boolean reativar = Tela.lerBoolean();
            if (reativar) {
                clienteExistente.ativar();
                Tela.imprimirString("Cliente reativado com sucesso.\n");
            } else {
                Tela.imprimirString("Operação cancelada.\n");
            }
            return;
        }
        Cliente cliente = new Cliente();
        cliente.setIdCliente(BancoDeDados.gerarIdCliente());
        Tela.imprimirString("Digite o nome do cliente: ");
        cliente.setNomeCliente(Tela.lerString());
        cliente.setCpf(cpf);
        BancoDeDados.adicionarCliente(cliente);
        Tela.imprimirString("Cliente cadastrado com sucesso.\n");
    }

    public static void listarClientes() {
        List<Cliente> clientes = BancoDeDados.getClientes();
        if (clientes.isEmpty()) {
            Tela.imprimirString("Nenhum cliente cadastrado.\n");
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (Cliente cliente : clientes) {
            sb.append(cliente).append("\n");
        }
        Tela.imprimirString(sb.toString());
    }

    public static void atualizarCliente() {
        Tela.imprimirString("Digite o CPF do cliente: ");
        String cpfAtual = Tela.lerString();
        Cliente cliente = BancoDeDados.buscarClientePorCpf(cpfAtual);
        if (cliente == null) {
            Tela.imprimirString("Cliente não encontrado.\n");
            return;
        }
        Tela.imprimirString("Novo nome: ");
        String novoNome = Tela.lerString();
        Tela.imprimirString("Novo CPF: ");
        String novoCpf = Tela.lerString();
        Cliente clienteCpfExistente = BancoDeDados.buscarClientePorCpf(novoCpf);
        if (clienteCpfExistente != null && clienteCpfExistente != cliente) {
            Tela.imprimirString("Já existe outro cliente com este CPF.\n");
            return;
        }
        cliente.setNomeCliente(novoNome);
        cliente.setCpf(novoCpf);
        Tela.imprimirString("Cliente atualizado.\n");
    }

    public static void desativarCliente() {
        Tela.imprimirString("Digite o CPF: ");
        String cpf = Tela.lerString();
        Cliente cliente = BancoDeDados.buscarClientePorCpf(cpf);
        if (cliente == null) {
            Tela.imprimirString("Cliente não encontrado.\n");
            return;
        }
        List<Pedido> pedidos = BancoDeDados.buscarPedidosPorCpf(cpf);
        for (Pedido pedido : pedidos) {
            if (pedido.getStatus() == Pedido.PENDENTE) {
                Tela.imprimirString("""
                                    
                        Cliente possui pedidos pendentes. Não pode ser desativado.
                        """);
                return;
            }
        }
        cliente.desativar();
        Tela.imprimirString("Cliente desativado.\n");
    }

    /* Produtos */
    public static void cadastrarProduto() {
        Produto produto = new Produto();
        produto.setIdProduto(BancoDeDados.gerarIdProduto());
        Tela.imprimirString("Nome do produto: ");
        produto.setNomeProduto(Tela.lerString());
        Tela.imprimirString("Preço do produto: ");
        produto.setPrecoUnitario(Tela.lerDouble());
        Tela.imprimirString("Categoria: ");
        produto.setCategoria(Tela.lerString());
        Tela.imprimirString("Data validade (dd/MM/yyyy): ");
        produto.setDataValidade(Tela.lerLocalDate());
        Tela.imprimirString("Quantidade: ");
        produto.setQuantidade(Tela.lerInt());
        BancoDeDados.adicionarProduto(produto);
        Tela.imprimirString("Produto cadastrado.\n");
    }

    public static void listarProdutos() {
        List<Produto> produtos = BancoDeDados.getProdutos();
        if (produtos.isEmpty()) {
            Tela.imprimirString("Nenhum produto cadastrado.\n");
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (Produto produto : produtos) {
            sb.append(produto).append("\n");
        }
        Tela.imprimirString(sb.toString());
    }

    public static void atualizarProduto() {
        Tela.imprimirString("Digite o ID do produto: ");
        int id = Tela.lerInt();
        Produto produto = BancoDeDados.buscarProdutoPorId(id);
        if (produto == null) {
            Tela.imprimirString("Produto não encontrado.\n");
            return;
        }
        Tela.imprimirString("Novo nome: ");
        produto.setNomeProduto(Tela.lerString());
        Tela.imprimirString("Novo preço: ");
        produto.setPrecoUnitario(Tela.lerDouble());
        Tela.imprimirString("Nova categoria: ");
        produto.setCategoria(Tela.lerString());
        Tela.imprimirString("Nova quantidade: ");
        produto.setQuantidade(Tela.lerInt());
        Tela.imprimirString("Produto atualizado.\n");
    }

    public static void excluirProduto() {
        Tela.imprimirString("Digite o ID do produto: ");
        int id = Tela.lerInt();
        boolean removido = BancoDeDados.removerProduto(id);
        if (removido) {
            Tela.imprimirString("Produto removido.\n");
        } else {
            Tela.imprimirString("Produto não encontrado.\n");
        }
    }

    /* Pedidos */
    public static void cadastrarPedido() {
        if (BancoDeDados.getClientes().isEmpty()) {
            Tela.imprimirString("Nenhum cliente cadastrado.\n");
            return;
        }
        if (BancoDeDados.getProdutos().isEmpty()) {
            Tela.imprimirString("Nenhum produto cadastrado.\n");
            return;
        }
        Tela.imprimirString("CPF do cliente: ");
        String cpf = Tela.lerString();
        Cliente cliente = BancoDeDados.buscarClientePorCpf(cpf);
        if (cliente == null) {
            Tela.imprimirString("Cliente não encontrado.\n");
            return;
        }
        if (!cliente.isAtivo()) {
            Tela.imprimirString("Cliente inativo.\n");
            return;
        }
        Pedido pedido = new Pedido();
        pedido.setIdPedido(BancoDeDados.gerarIdPedido());
        pedido.setCliente(cliente);
        adicionarItensPedido(pedido);
        if (pedido.getItens().isEmpty()) {
            Tela.imprimirString("Pedido vazio cancelado.\n");
            return;
        }
        BancoDeDados.adicionarPedido(pedido);
        Tela.imprimirString("""
                
                Pedido criado com sucesso.Total: R$ %.2f
                """.formatted(
                pedido.getValorTotal()));
    }

    public static void adicionarItensPedido(Pedido pedido) {
        while (true) {
            listarProdutos();
            Tela.imprimirString("""
                    
                    Digite o ID do produto.
                    0 para finalizar:
                    """);
            int idProduto = Tela.lerInt();
            if (idProduto == 0) {
                break;
            }
            Produto produto = BancoDeDados.buscarProdutoPorId(idProduto);
            if (produto == null) {
                Tela.imprimirString("Produto não encontrado.\n");
                continue;
            }
            Tela.imprimirString("Quantidade: ");
            int quantidade = Tela.lerInt();
            try {
                pedido.adicionarItem(produto, quantidade);
                Tela.imprimirString("Produto adicionado.\n");
            } catch (Exception e) {
                Tela.imprimirString(e.getMessage());
            }
        }
    }

    public static void listarPedidos() {
        List<Pedido> pedidos = BancoDeDados.getPedidos();
        if (pedidos.isEmpty()) {
            Tela.imprimirString("Nenhum pedido cadastrado.\n");
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (Pedido pedido : pedidos) {
            sb.append(pedido).append("\n");
        }
        Tela.imprimirString(sb.toString());
    }

    public static void atualizarPedido() {
        Tela.imprimirString("Digite o ID do pedido: ");
        int id = Tela.lerInt();
        Pedido pedido = BancoDeDados.buscarPedidoPorId(id);
        if (pedido == null) {
            Tela.imprimirString("Pedido não encontrado.\n");
            return;
        }
        if (pedido.getStatus() != Pedido.PENDENTE) {
            Tela.imprimirString("Somente pedidos pendentes podem ser atualizados.\n");
            return;
        }
        pedido.limparItens();
        adicionarItensPedido(pedido);
        Tela.imprimirString("Pedido atualizado.\n");
    }

    public static void cancelarPedido() {
        Tela.imprimirString("Digite o ID do pedido: ");
        int id = Tela.lerInt();
        Pedido pedido = BancoDeDados.buscarPedidoPorId(id);
        if (pedido == null) {
            Tela.imprimirString("Pedido não encontrado.\n");
            return;
        }
        try {
            pedido.cancelar();
            Tela.imprimirString("Pedido cancelado.\n");
        } catch (Exception e) {
            Tela.imprimirString(e.getMessage());
        }
    }

    public static void finalizarPedido() {
        Tela.imprimirString("Digite o ID do pedido: ");
        int id = Tela.lerInt();
        Pedido pedido = BancoDeDados.buscarPedidoPorId(id);
        if (pedido == null) {
            Tela.imprimirString("Pedido não encontrado.\n");
            return;
        }
        try {
            pedido.finalizar();
            Tela.imprimirString("Pedido finalizado.\n");
        } catch (Exception e) {
            Tela.imprimirString(e.getMessage());
        }
    }
}
