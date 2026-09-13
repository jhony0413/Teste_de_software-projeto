package main;

import dao.*;
import models.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class MainCLI {

    private static final Scanner scanner = new Scanner(System.in);
    private static final ClienteDAO clienteDAO = new ClienteDAO();
    private static final VendedorDAO vendedorDAO = new VendedorDAO();
    private static final ProdutoDAO produtoDAO = new ProdutoDAO();
    private static final CategoriaDAO categoriaDAO = new CategoriaDAO();
    private static final FornecedorDAO fornecedorDAO = new FornecedorDAO();
    private static final FornecedorProdutoDAO fornecedorProdutoDAO = new FornecedorProdutoDAO();
    private static final PedidoDAO pedidoDAO = new PedidoDAO();
    private static final ItemPedidoDAO itemPedidoDAO = new ItemPedidoDAO();
    private static final PagamentoDAO pagamentoDAO = new PagamentoDAO();
    private static final HistoricoStatusPedidoDAO historicoDAO = new HistoricoStatusPedidoDAO();

    private static Vendedor vendedorLogado = null;

    public static void main(String[] args) {
        System.out.println("=== BEM-VINDO AO SGL ===");

        while (vendedorLogado == null) {
            System.out.println("\n1. Fazer Login");
            System.out.println("2. Cadastrar novo Vendedor");
            System.out.println("0. Sair do Sistema");
            System.out.printf("Escolha: ");
            int opcao = lerInteiro();

            if (opcao == 1) {
                if (realizarLogin()) {
                    break;
                } else {
                    System.out.println("Voltando ao menu inicial...");
                }
            } else if (opcao == 2) {
                cadastrarNovoVendedor();
            } else if (opcao == 0) {
                System.out.println("Encerrando o sistema...");
                return;
            } else {
                System.out.println("Opção inválida!");
            }
        }

        int opcaoMenu;
        do {
            System.out.println("\n=== MENU PRINCIPAL SGL ===");
            System.out.println("Usuário logado: " + vendedorLogado.getNome());
            System.out.println("1. CRUD Clientes");
            System.out.println("2. CRUD Categorias");
            System.out.println("3. CRUD Produtos");
            System.out.println("4. CRUD Fornecedores");
            System.out.println("5. CRUD Compras (Fornecedor x Produto)");
            System.out.println("6. CRUD Pedidos (Com Cascata e Histórico)");
            System.out.println("0. Sair");
            System.out.printf("Escolha: ");
            opcaoMenu = lerInteiro();

            switch (opcaoMenu) {
                case 1 ->
                    menuClientes();
                case 2 ->
                    menuCategorias();
                case 3 ->
                    menuProdutos();
                case 4 ->
                    menuFornecedores();
                case 5 ->
                    menuCompras();
                case 6 ->
                    menuPedidos();
                case 0 ->
                    System.out.println("Encerrando o Sistema Gerenciador de Loja...");
                default ->
                    System.out.println("Opção inválida!");
            }
        } while (opcaoMenu != 0);
    }

    private static boolean realizarLogin() {
        int tentativas = 0;
        while (tentativas < 3) {
            System.out.printf("\nLogin: ");
            String login = scanner.nextLine();
            System.out.printf("Senha: ");
            String senha = scanner.nextLine();

            vendedorLogado = vendedorDAO.autenticar(login, senha);

            if (vendedorLogado != null) {
                System.out.println("\nLogin realizado com sucesso!");
                return true;
            } else {
                tentativas++;
                System.out.println("Credenciais inválidas. Tentativas restantes: " + (3 - tentativas));
            }
        }
        return false;
    }

    private static void cadastrarNovoVendedor() {
        try {
            System.out.println("\n--- NOVO CADASTRO DE VENDEDOR ---");
            System.out.printf("Nome: ");
            String nome = scanner.nextLine();
            System.out.printf("CPF (11 dígitos): ");
            String cpf = scanner.nextLine();
            System.out.printf("Email: ");
            String email = scanner.nextLine();
            System.out.printf("Telefone: ");
            String telefone = scanner.nextLine();
            System.out.printf("Escolha um Login: ");
            String login = scanner.nextLine();
            System.out.printf("Escolha uma Senha: ");
            String senha = scanner.nextLine();

            Vendedor novoVendedor = new Vendedor(0, nome, cpf, email, telefone, true, login, senha);
            vendedorDAO.inserir(novoVendedor);
            System.out.println("\nCadastro realizado com sucesso! Faça o login agora.");
        } catch (Exception e) {
            System.out.println("Erro ao cadastrar vendedor: " + e.getMessage());
        }
    }

    private static void menuPedidos() {
        int opcao;
        do {
            System.out.println("\n--- GESTÃO DE PEDIDOS ---");
            System.out.println("1. Novo Pedido (Cria itens, pagamento e primeiro status)");
            System.out.println("2. Listar Pedidos");
            System.out.println("3. Atualizar Pedido (Gera novo status)");
            System.out.println("4. Excluir Pedido (Apaga em cascata)");
            System.out.println("0. Voltar");
            System.out.printf("Escolha: ");
            opcao = lerInteiro();

            switch (opcao) {
                case 1 ->
                    criarPedidoCompleto();
                case 2 ->
                    listarPedidos();
                case 3 ->
                    atualizarPedido();
                case 4 ->
                    excluirPedidoCascata();
            }
        } while (opcao != 0);
    }

    private static void criarPedidoCompleto() {
        try {
            System.out.printf("ID do Cliente: ");
            int idCli = lerInteiro();

            int idVend = vendedorLogado.getIdPessoa();
            System.out.println("Usando Vendedor logado ID: " + idVend);

            Pedido pedido = new Pedido(0, idCli, idVend, LocalDateTime.now(), 0.0);
            pedidoDAO.inserir(pedido);
            int idPedido = pedido.getIdPedido();
            System.out.println("Pedido gerado com ID: " + idPedido);

            double valorTotalPedido = 0.0;
            String continuar = "";
            do {
                System.out.printf("ID do Produto: ");
                int idProd = lerInteiro();

                Produto p = produtoDAO.listar().stream()
                        .filter(prod -> prod.getIdProduto() == idProd)
                        .findFirst()
                        .orElse(null);

                if (p == null) {
                    System.out.println("Produto não encontrado com ID " + idProd + ". Tente novamente.");
                    continue;
                }

                System.out.printf("Quantidade (Estoque disponível: " + p.getEstoque() + "): ");
                int qtd = lerInteiro();

                if (qtd > p.getEstoque()) {
                    System.out.println("Quantidade excede o estoque atual. Tente novamente");
                    continue;
                }

                double subtotal = qtd * p.getPrecoUnitario();

                ItemPedido item = new ItemPedido(idPedido, idProd, qtd, p.getPrecoUnitario(), subtotal);
                itemPedidoDAO.inserir(item);
                valorTotalPedido += subtotal;

                p.setEstoque(p.getEstoque() - qtd);
                produtoDAO.atualizar(p);

                System.out.printf("Adicionar outro produto? (S/N): ");
                continuar = scanner.nextLine().trim().toUpperCase();
            } while (continuar.equals("S"));

            pedido.setValorTotal(valorTotalPedido);
            pedidoDAO.atualizar(pedido);

            System.out.println("\n--- Registro de Pagamento ---");
            System.out.printf("Forma de Pagamento (DINHEIRO, CARTAO_CREDITO, CARTAO_DEBITO, PIX, BOLETO): ");
            String formaPgto = scanner.nextLine();
            Pagamento pagamento = new Pagamento(0, idPedido, formaPgto, LocalDateTime.now(), valorTotalPedido, "APROVADO");
            pagamentoDAO.inserir(pagamento);

            HistoricoStatusPedido historico = new HistoricoStatusPedido(idPedido, LocalDateTime.now(), "PENDENTE");
            historicoDAO.inserir(historico);

            System.out.println("Pedido completo finalizado com sucesso! Valor Total: R$ " + valorTotalPedido);
        } catch (Exception e) {
            System.out.println("Erro ao criar pedido: " + e.getMessage());
        }
    }

    private static void atualizarPedido() {
        try {
            System.out.printf("ID do Pedido para atualizar: ");
            int idPedido = lerInteiro();

            System.out.printf("Novo ID do Cliente: ");
            int idCli = lerInteiro();

            int idVend = vendedorLogado.getIdPessoa();
            System.out.println("Usando Vendedor logado ID: " + idVend);

            Pedido pedido = new Pedido(idPedido, idCli, idVend, LocalDateTime.now(), 0.0);

            System.out.println("Excluindo itens do pedido...");
            List<ItemPedido> itens = itemPedidoDAO.listarPorPedido(idPedido);
            for (ItemPedido item : itens) {
                Produto p = produtoDAO.listar().stream()
                        .filter(prod -> prod.getIdProduto() == item.getIdProduto())
                        .findFirst()
                        .orElse(null);
                p.setEstoque(p.getEstoque() + item.getQuantidade());
                produtoDAO.atualizar(p);
                itemPedidoDAO.deletar(idPedido, item.getIdProduto());
            }

            System.out.println("Preparando para adicionar novos itens...");
            double valorTotalPedido = 0.0;
            String continuar = "";
            do {
                System.out.printf("ID do Produto: ");
                int idProd = lerInteiro();

                Produto p = produtoDAO.listar().stream()
                        .filter(prod -> prod.getIdProduto() == idProd)
                        .findFirst()
                        .orElse(null);

                if (p == null) {
                    System.out.println("Produto não encontrado com ID " + idProd + ". Tente novamente.");
                    continue;
                }

                System.out.printf("Quantidade (Estoque disponível: " + p.getEstoque() + "): ");
                int qtd = lerInteiro();

                if (qtd > p.getEstoque()) {
                    System.out.println("Quantidade excede o estoque atual. Tente novamente");
                    continue;
                }

                double subtotal = qtd * p.getPrecoUnitario();

                ItemPedido item = new ItemPedido(idPedido, idProd, qtd, p.getPrecoUnitario(), subtotal);
                itemPedidoDAO.inserir(item);
                valorTotalPedido += subtotal;

                p.setEstoque(p.getEstoque() - qtd);
                produtoDAO.atualizar(p);

                System.out.printf("Adicionar outro produto? (S/N): ");
                continuar = scanner.nextLine().trim().toUpperCase();
            } while (continuar.equals("S"));

            pedido.setValorTotal(valorTotalPedido);
            pedidoDAO.atualizar(pedido);

            System.out.println("\n--- Registro de Pagamento ---");
            System.out.printf("Forma de Pagamento (DINHEIRO, CARTAO_CREDITO, CARTAO_DEBITO, PIX, BOLETO): ");
            String formaPgto = scanner.nextLine();
            Pagamento pagamento = new Pagamento(0, idPedido, formaPgto, LocalDateTime.now(), valorTotalPedido, "APROVADO");
            pagamentoDAO.inserir(pagamento);

            HistoricoStatusPedido historico = new HistoricoStatusPedido(idPedido, LocalDateTime.now(), "PENDENTE");
            historicoDAO.inserir(historico);

            System.out.println("Pedido completo finalizado com sucesso! Valor Total: R$ " + valorTotalPedido);
        } catch (Exception e) {
            System.out.println("Erro ao atualizar pedido: " + e.getMessage());
        }
    }

    private static void excluirPedidoCascata() {
        try {
            System.out.printf("ID do Pedido a ser excluído: ");
            int idPedido = lerInteiro();

            historicoDAO.deletarPorPedido(idPedido);

            List<Pagamento> pagamentos = pagamentoDAO.listar();
            for (Pagamento p : pagamentos) {
                if (p.getIdPedido() == idPedido) {
                    pagamentoDAO.deletar(p.getIdPagamento());
                }
            }

            List<ItemPedido> itens = itemPedidoDAO.listarPorPedido(idPedido);
            for (ItemPedido item : itens) {
                Produto p = produtoDAO.listar().stream()
                        .filter(prod -> prod.getIdProduto() == item.getIdProduto())
                        .findFirst()
                        .orElse(null);
                p.setEstoque(p.getEstoque() + item.getQuantidade());
                produtoDAO.atualizar(p);
                itemPedidoDAO.deletar(idPedido, item.getIdProduto());
            }

            pedidoDAO.deletar(idPedido);
            System.out.println("Pedido e todas as suas dependências foram excluídos com sucesso!");
        } catch (Exception e) {
            System.out.println("Erro ao excluir pedido: " + e.getMessage());
        }
    }

    private static void listarPedidos() {
        for (Pedido p : pedidoDAO.listar()) {
            System.out.println("ID: " + p.getIdPedido() + " | Cliente ID: " + p.getIdCliente() + " | Vendedor ID: " + p.getIdVendedor() + " | Total: R$ " + p.getValorTotal());
        }
    }

    private static void menuClientes() {
        int op;
        do {
            System.out.println("\n--- GESTÃO DE CLIENTES ---");
            System.out.printf("1. Inserir | 2. Listar | 3. Atualizar | 4. Excluir | 0. Voltar -> Escolha: ");
            op = lerInteiro();
            try {
                if (op == 1) {
                    System.out.printf("Nome: ");
                    String nome = scanner.nextLine();
                    System.out.printf("CPF (11 dígitos): ");
                    String cpf = scanner.nextLine();
                    System.out.printf("Email: ");
                    String email = scanner.nextLine();
                    System.out.printf("Telefone: ");
                    String tel = scanner.nextLine();
                    Cliente c = new Cliente(0, nome, cpf, email, tel, true, LocalDateTime.now());
                    clienteDAO.inserir(c);
                    System.out.println("Cliente salvo!");
                } else if (op == 2) {
                    clienteDAO.listar().forEach(c -> System.out.println("ID: " + c.getIdPessoa() + " - " + c.getNome() + " | CPF: " + c.getCpf()));
                } else if (op == 3) {
                    System.out.printf("ID para atualizar: ");
                    int id = lerInteiro();
                    System.out.printf("Novo Nome: ");
                    String nome = scanner.nextLine();
                    System.out.printf("Novo CPF: ");
                    String cpf = scanner.nextLine();
                    System.out.printf("Novo Email: ");
                    String email = scanner.nextLine();
                    System.out.printf("Novo Telefone: ");
                    String tel = scanner.nextLine();
                    Cliente c = new Cliente(id, nome, cpf, email, tel, true, LocalDateTime.now());
                    clienteDAO.atualizar(c);
                    System.out.println("Cliente atualizado!");
                } else if (op == 4) {
                    System.out.printf("ID para excluir: ");
                    clienteDAO.deletar(lerInteiro());
                    System.out.println("Excluído!");
                }
            } catch (Exception e) {
                System.out.println("Erro: " + e.getMessage());
            }
        } while (op != 0);
    }

    private static void menuProdutos() {
        int op;
        do {
            System.out.println("\n--- GESTÃO DE PRODUTOS ---");
            System.out.printf("1. Inserir | 2. Listar | 3. Atualizar | 4. Excluir | 0. Voltar -> Escolha: ");
            op = lerInteiro();
            try {
                if (op == 1) {
                    System.out.printf("ID da Categoria: ");
                    int idCat = lerInteiro();
                    System.out.printf("Nome: ");
                    String nome = scanner.nextLine();
                    System.out.printf("Descrição: ");
                    String desc = scanner.nextLine();
                    System.out.printf("Preço: ");
                    double preco = lerDouble();
                    System.out.printf("Qtd Estoque: ");
                    int qtd = lerInteiro();

                    Produto p = new Produto(0, idCat, nome, desc, preco, qtd, true);
                    produtoDAO.inserir(p);
                    System.out.println("Produto salvo!");
                } else if (op == 2) {
                    produtoDAO.listar().forEach(p -> System.out.println("ID: " + p.getIdProduto() + " - " + p.getNome() + " | R$ " + p.getPrecoUnitario() + " | Estoque: " + p.getEstoque()));
                } else if (op == 3) {
                    System.out.printf("ID para atualizar: ");
                    int id = lerInteiro();
                    System.out.printf("Novo ID da Categoria: ");
                    int idCat = lerInteiro();
                    System.out.printf("Novo Nome: ");
                    String nome = scanner.nextLine();
                    System.out.printf("Nova Descrição: ");
                    String desc = scanner.nextLine();
                    System.out.printf("Novo Preço: ");
                    double preco = lerDouble();
                    System.out.printf("Nova Qtd Estoque: ");
                    int qtd = lerInteiro();
                    Produto p = new Produto(id, idCat, nome, desc, preco, qtd, true);
                    produtoDAO.atualizar(p);
                    System.out.println("Produto atualizado!");
                } else if (op == 4) {
                    System.out.printf("ID para excluir: ");
                    produtoDAO.deletar(lerInteiro());
                    System.out.println("Excluído!");
                }
            } catch (Exception e) {
                System.out.println("Erro: " + e.getMessage());
            }
        } while (op != 0);
    }

    private static void menuCategorias() {
        int op;
        do {
            System.out.println("\n--- GESTÃO DE CATEGORIAS ---");
            System.out.printf("1. Inserir | 2. Listar | 3. Atualizar | 4. Excluir | 0. Voltar -> Escolha: ");
            op = lerInteiro();
            try {
                if (op == 1) {
                    System.out.printf("Nome: ");
                    String nome = scanner.nextLine();
                    System.out.printf("Descrição: ");
                    String desc = scanner.nextLine();
                    Categoria c = new Categoria(0, nome, desc, null);
                    categoriaDAO.inserir(c);
                    System.out.println("Categoria salva!");
                } else if (op == 2) {
                    categoriaDAO.listar().forEach(c -> System.out.println("ID: " + c.getIdCategoria() + " - " + c.getNome()));
                } else if (op == 3) {
                    System.out.printf("ID para atualizar: ");
                    int id = lerInteiro();
                    System.out.printf("Novo Nome: ");
                    String nome = scanner.nextLine();
                    System.out.printf("Nova Descrição: ");
                    String desc = scanner.nextLine();
                    Categoria c = new Categoria(id, nome, desc, null);
                    categoriaDAO.atualizar(c);
                    System.out.println("Categoria atualizada!");
                } else if (op == 4) {
                    System.out.printf("ID para excluir: ");
                    categoriaDAO.deletar(lerInteiro());
                    System.out.println("Excluída!");
                }
            } catch (Exception e) {
                System.out.println("Erro: " + e.getMessage());
            }
        } while (op != 0);
    }

    private static void menuFornecedores() {
        int op;
        do {
            System.out.println("\n--- GESTÃO DE FORNECEDORES ---");
            System.out.printf("1. Inserir | 2. Listar | 3. Atualizar | 4. Excluir | 0. Voltar -> Escolha: ");
            op = lerInteiro();
            try {
                if (op == 1) {
                    System.out.printf("CNPJ (14 dígitos): ");
                    String cnpj = scanner.nextLine();
                    System.out.printf("Nome: ");
                    String nome = scanner.nextLine();
                    System.out.printf("Email: ");
                    String email = scanner.nextLine();
                    System.out.printf("Telefone: ");
                    String tel = scanner.nextLine();
                    Fornecedor f = new Fornecedor(0, cnpj, nome, email, tel, true);
                    fornecedorDAO.inserir(f);
                    System.out.println("Fornecedor salvo!");
                } else if (op == 2) {
                    fornecedorDAO.listar().forEach(f -> System.out.println("ID: " + f.getIdFornecedor() + " - " + f.getNome() + " | CNPJ: " + f.getCnpj()));
                } else if (op == 3) {
                    System.out.printf("ID para atualizar: ");
                    int id = lerInteiro();
                    System.out.printf("Novo CNPJ: ");
                    String cnpj = scanner.nextLine();
                    System.out.printf("Novo Nome: ");
                    String nome = scanner.nextLine();
                    System.out.printf("Novo Email: ");
                    String email = scanner.nextLine();
                    System.out.printf("Novo Telefone: ");
                    String tel = scanner.nextLine();
                    Fornecedor f = new Fornecedor(id, cnpj, nome, email, tel, true);
                    fornecedorDAO.atualizar(f);
                    System.out.println("Fornecedor atualizado!");
                } else if (op == 4) {
                    System.out.printf("ID para excluir: ");
                    fornecedorDAO.deletar(lerInteiro());
                    System.out.println("Excluído!");
                }
            } catch (Exception e) {
                System.out.println("Erro: " + e.getMessage());
            }
        } while (op != 0);
    }

    private static void menuCompras() {
        int op;
        do {
            System.out.println("\n--- GESTÃO DE COMPRAS (FORNECEDOR X PRODUTO) ---");
            System.out.printf("1. Associar | 2. Listar | 3. Atualizar | 4. Excluir | 0. Voltar -> Escolha: ");
            op = lerInteiro();
            try {
                if (op == 1) {
                    System.out.printf("ID Fornecedor: ");
                    int idF = lerInteiro();
                    System.out.printf("ID Produto: ");
                    int idP = lerInteiro();

                    Produto p = produtoDAO.listar().stream()
                            .filter(prod -> prod.getIdProduto() == idP)
                            .findFirst()
                            .orElse(null);

                    System.out.printf("Prazo Entrega (dias): ");
                    int prazo = lerInteiro();
                    System.out.printf("Quantidade: ");
                    int qtd = lerInteiro();
                    System.out.printf("Preço Custo: ");
                    double custo = p.getPrecoUnitario() * qtd;
                    fornecedorProdutoDAO.inserir(new FornecedorProduto(idF, idP, custo, prazo, qtd));
                    System.out.println("Associação salva!");
                } else if (op == 2) {
                    System.out.printf("ID Fornecedor para listar: ");
                    fornecedorProdutoDAO.listarPorFornecedor(lerInteiro()).forEach(fp
                            -> System.out.println("Produto ID: " + fp.getIdProduto() + " | Custo: R$ " + fp.getPrecoCusto() + " | Qtd: " + fp.getQuantidade())
                    );
                } else if (op == 3) {
                    System.out.printf("ID Fornecedor: ");
                    int idF = lerInteiro();
                    System.out.printf("ID Produto: ");
                    int idP = lerInteiro();
                    System.out.printf("Novo Preço Custo: ");
                    double custo = lerDouble();
                    System.out.printf("Novo Prazo Entrega (dias): ");
                    int prazo = lerInteiro();
                    System.out.printf("Nova Quantidade: ");
                    int qtd = lerInteiro();
                    fornecedorProdutoDAO.atualizar(new FornecedorProduto(idF, idP, custo, prazo, qtd));
                    System.out.println("Associação atualizada!");
                } else if (op == 4) {
                    System.out.printf("ID Fornecedor: ");
                    int idF = lerInteiro();
                    System.out.printf("ID Produto: ");
                    int idP = lerInteiro();
                    fornecedorProdutoDAO.deletar(idF, idP);
                    System.out.println("Excluído!");
                }
            } catch (Exception e) {
                System.out.println("Erro: " + e.getMessage());
            }
        } while (op != 0);
    }

    private static int lerInteiro() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static double lerDouble() {
        try {
            return Double.parseDouble(scanner.nextLine().trim().replace(",", "."));
        } catch (NumberFormatException e) {
            return -1.0;
        }
    }
}
