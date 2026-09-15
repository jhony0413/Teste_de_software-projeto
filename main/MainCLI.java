package main;

import dao.*;
import models.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
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
            System.out.println("5. CRUD Pedidos (Com Cascata e Histórico)");
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

    private static void menuClientes() {
        int op;
        do {
            System.out.println("\n--- GESTÃO DE CLIENTES ---");
            System.out.printf("1. Inserir | 2. Listar | 3. Atualizar | 4. Deletar | 0. Voltar -> Escolha: ");
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
                    Cliente c = new Cliente(0, vendedorLogado.getIdPessoa(), nome, cpf, email, tel, true, LocalDateTime.now());
                    clienteDAO.inserir(c);
                    System.out.println("Cliente salvo!");
                } else if (op == 2) {
                    clienteDAO.listarPorVendedor(vendedorLogado.getIdPessoa())
                            .forEach(c -> System.out.println("ID: " + c.getIdPessoa() + " - " + c.getNome() + " | CPF: " + c.getCpf()));
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
                    Cliente c = new Cliente(id, vendedorLogado.getIdPessoa(), nome, cpf, email, tel, true, LocalDateTime.now());
                    clienteDAO.atualizar(c);
                    System.out.println("Cliente atualizado!");
                } else if (op == 4) {
                    System.out.printf("ID para deletar: ");
                    clienteDAO.deletar(lerInteiro(), vendedorLogado.getIdPessoa());
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
            System.out.printf("1. Inserir | 2. Listar | 3. Atualizar | 4. Deletar | 0. Voltar -> Escolha: ");
            op = lerInteiro();
            try {
                if (op == 1) {
                    System.out.printf("Nome: ");
                    String nome = scanner.nextLine();
                    System.out.printf("Descrição: ");
                    String desc = scanner.nextLine();
                    Categoria c = new Categoria(0, vendedorLogado.getIdPessoa(), nome, desc, null);
                    categoriaDAO.inserir(c);
                    System.out.println("Categoria salva!");
                } else if (op == 2) {
                    categoriaDAO.listarPorVendedor(vendedorLogado.getIdPessoa())
                            .forEach(c -> System.out.println("ID: " + c.getIdCategoria() + " - " + c.getNome()));
                } else if (op == 3) {
                    System.out.printf("ID para atualizar: ");
                    int id = lerInteiro();
                    System.out.printf("Novo Nome: ");
                    String nome = scanner.nextLine();
                    System.out.printf("Nova Descrição: ");
                    String desc = scanner.nextLine();
                    Categoria c = new Categoria(id, vendedorLogado.getIdPessoa(), nome, desc, null);
                    categoriaDAO.atualizar(c);
                    System.out.println("Categoria atualizada!");
                } else if (op == 4) {
                    System.out.printf("ID para deletar: ");
                    categoriaDAO.deletar(lerInteiro(), vendedorLogado.getIdPessoa());
                    System.out.println("Excluída!");
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
            System.out.printf("1. Inserir | 2. Listar | 3. Atualizar | 4. Deletar | 0. Voltar -> Escolha: ");
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

                    Produto p = new Produto(0, vendedorLogado.getIdPessoa(), idCat, nome, desc, preco, qtd, true);
                    produtoDAO.inserir(p);
                    System.out.println("Produto salvo!");
                } else if (op == 2) {
                    produtoDAO.listarPorVendedor(vendedorLogado.getIdPessoa())
                            .forEach(p -> System.out.println("ID: " + p.getIdProduto() + " - " + p.getNome() + " | R$ " + p.getPrecoUnitario() + " | Estoque: " + p.getEstoque()));
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

                    Produto p = new Produto(id, vendedorLogado.getIdPessoa(), idCat, nome, desc, preco, qtd, true);
                    produtoDAO.atualizar(p);
                    System.out.println("Produto atualizado!");
                } else if (op == 4) {
                    System.out.printf("ID para deletar: ");
                    produtoDAO.deletar(lerInteiro(), vendedorLogado.getIdPessoa());
                    System.out.println("Excluído!");
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
            System.out.printf("1. Inserir | 2. Listar | 3. Atualizar | 4. Deletar | 0. Voltar -> Escolha: ");
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
                    Fornecedor f = new Fornecedor(0, vendedorLogado.getIdPessoa(), cnpj, nome, email, tel, true);
                    fornecedorDAO.inserir(f);
                    System.out.println("Fornecedor salvo!");
                } else if (op == 2) {
                    fornecedorDAO.listarPorVendedor(vendedorLogado.getIdPessoa())
                            .forEach(f -> System.out.println("ID: " + f.getIdFornecedor() + " - " + f.getNome() + " | CNPJ: " + f.getCnpj()));
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
                    Fornecedor f = new Fornecedor(id, vendedorLogado.getIdPessoa(), cnpj, nome, email, tel, true);
                    fornecedorDAO.atualizar(f);
                    System.out.println("Fornecedor atualizado!");
                } else if (op == 4) {
                    System.out.printf("ID para deletar: ");
                    fornecedorDAO.deletar(lerInteiro(), vendedorLogado.getIdPessoa());
                    System.out.println("Excluído!");
                }
            } catch (Exception e) {
                System.out.println("Erro: " + e.getMessage());
            }
        } while (op != 0);
    }

    private static void menuPedidos() {
        int opcao;
        do {
            System.out.println("\n--- GESTÃO DE PEDIDOS ---");
            System.out.println("1. Criar Pedido");
            System.out.println("2. Listar Pedidos");
            System.out.println("3. Atualizar Pedido");
            System.out.println("4. Deletar Pedido");
            System.out.println("0. Voltar");
            System.out.printf("Escolha: ");
            opcao = lerInteiro();

            switch (opcao) {
                case 1 ->
                    inserirPedidoCompleto();
                case 2 ->
                    listarPedidoCompleto();
                case 3 ->
                    atualizarPedidoCompleto();
                case 4 ->
                    deletarPedidoCompleto();
                case 0 ->
                    System.out.println("Voltando ao menu principal...");
                default ->
                    System.out.println("Opção inválida!");
            }
        } while (opcao != 0);
    }

    private static void inserirPedidoCompleto() {
        System.out.println("\n--- CRIAR NOVO PEDIDO COMPLETO ---");
        try {
            System.out.printf("ID do Cliente: ");
            int idCliente = lerInteiro();

            List<ItemPedido> itens = new ArrayList<>();
            List<Pagamento> pagamentos = new ArrayList<>();
            double valorTotal = 0.0;

            System.out.println("\n-- Adicionar Itens --");
            boolean adicionarMaisItens = true;
            do {
                System.out.printf("ID do Produto: ");
                int idProduto = lerInteiro();
                System.out.printf("Quantidade: ");
                int qtd = lerInteiro();
                double preco = produtoDAO.listarPorVendedor(vendedorLogado.getIdPessoa())
                        .stream()
                        .filter(p -> p.getIdProduto() == idProduto)
                        .findFirst()
                        .orElse(null)
                        .getPrecoUnitario();
                System.out.printf("Preço Unitário: R$ %f\n", preco);

                double subtotal = qtd * preco;
                valorTotal += subtotal;

                itens.add(new ItemPedido(0, idProduto, qtd, preco, subtotal));

                System.out.printf("Adicionar mais itens? (1-Sim / 0-Não): ");
                adicionarMaisItens = lerInteiro() == 1;
            } while (adicionarMaisItens);

            System.out.println("\n-- Adicionar Pagamentos --");
            System.out.println("Valor total do pedido: R$ " + valorTotal);
            boolean adicionarMaisPagamentos = true;
            do {
                System.out.printf("Forma de Pagamento (DINHEIRO, PIX, CARTAO_CREDITO, CARTAO_DEBITO, BOLETO): ");
                String formaPgto = scanner.nextLine();
                System.out.printf("Valor deste pagamento: R$ ");
                double valorPgto = lerDouble();
                System.out.printf("Status (PROCESSANDO, APROVADO, RECUSADO): ");
                String statusPgto = scanner.nextLine();

                pagamentos.add(new Pagamento(0, 0, formaPgto, LocalDateTime.now(), valorPgto, statusPgto));

                System.out.printf("Adicionar mais pagamentos? (1-Sim / 0-Não): ");
                adicionarMaisPagamentos = lerInteiro() == 1;
            } while (adicionarMaisPagamentos);

            System.out.printf("\nStatus inicial do Pedido (ex: PENDENTE, PAGO): ");
            String statusPedido = scanner.nextLine();
            HistoricoStatusPedido historico = new HistoricoStatusPedido(0, LocalDateTime.now(), statusPedido);

            Pedido novoPedido = new Pedido(0, idCliente, vendedorLogado.getIdPessoa(), LocalDateTime.now(), valorTotal);

            pedidoDAO.inserirPedidoCompleto(novoPedido, itens, pagamentos, historico);
            System.out.println("\nPedido criado com sucesso! ID Gerado: " + novoPedido.getIdPedido());

        } catch (Exception e) {
            System.out.println("Erro ao criar pedido: " + e.getMessage());
        }
    }

    private static void listarPedidoCompleto() {
        System.out.println("\n--- LISTA DE PEDIDOS COMPLETA ---");
        try {

            List<Pedido> pedidos = pedidoDAO.listarPorVendedor(vendedorLogado.getIdPessoa());

            if (pedidos.isEmpty()) {
                System.out.println("Nenhum pedido encontrado para a sua conta.");
                return;
            }

            List<Pagamento> todosPagamentos = pagamentoDAO.listar();

            for (Pedido p : pedidos) {
                System.out.println("\n========================================");
                System.out.println("PEDIDO ID: " + p.getIdPedido() + " | Cliente ID: " + p.getIdCliente() + " | Data: " + p.getDataEmissao() + " | Valor Total: R$ " + p.getValorTotal());

                System.out.println("  -> ITENS:");
                List<ItemPedido> itens = itemPedidoDAO.listarPorPedido(p.getIdPedido());
                for (ItemPedido item : itens) {
                    System.out.println("     - Produto ID: " + item.getIdProduto() + " | Qtd: " + item.getQuantidade() + " | Subtotal: R$ " + item.getSubtotal());
                }

                System.out.println("  -> PAGAMENTOS:");
                todosPagamentos.stream()
                        .filter(pgto -> pgto.getIdPedido() == p.getIdPedido())
                        .forEach(pgto -> System.out.println("     - " + pgto.getFormaPagamento() + " | R$ " + pgto.getValor() + " | Status: " + pgto.getStatus()));

                System.out.println("  -> HISTÓRICO DE STATUS:");
                List<HistoricoStatusPedido> historicos = historicoDAO.listarPorPedido(p.getIdPedido());
                for (HistoricoStatusPedido h : historicos) {
                    System.out.println("     - " + h.getStatus() + " em " + h.getDataAlteracao());
                }
                System.out.println("========================================");
            }
        } catch (Exception e) {
            System.out.println("Erro ao listar pedidos: " + e.getMessage());
        }
    }

    private static void atualizarPedidoCompleto() {
        System.out.println("\n--- ATUALIZAR PEDIDO (SUBSTITUIÇÃO DE ITENS E PAGAMENTOS) ---");
        try {
            System.out.printf("Digite o ID do Pedido que deseja atualizar: ");
            int idPedido = lerInteiro();

            System.out.printf("Novo ID do Cliente: ");
            int idCliente = lerInteiro();

            List<ItemPedido> novosItens = new ArrayList<>();
            List<Pagamento> novosPagamentos = new ArrayList<>();
            double novoValorTotal = 0.0;

            System.out.println("\n-- Registrar Novos Itens --");
            boolean adicionarMaisItens = true;
            do {
                System.out.printf("ID do Produto: ");
                int idProduto = lerInteiro();
                System.out.printf("Quantidade: ");
                int qtd = lerInteiro();
                double preco = produtoDAO.listarPorVendedor(vendedorLogado.getIdPessoa())
                        .stream()
                        .filter(p -> p.getIdProduto() == idProduto)
                        .findFirst()
                        .orElse(null)
                        .getPrecoUnitario();
                System.out.printf("Preço Unitário: R$ %f\n", preco);

                double subtotal = qtd * preco;
                novoValorTotal += subtotal;

                novosItens.add(new ItemPedido(idPedido, idProduto, qtd, preco, subtotal));

                System.out.printf("Adicionar mais itens? (1-Sim / 0-Não): ");
                adicionarMaisItens = lerInteiro() == 1;
            } while (adicionarMaisItens);

            System.out.println("\n-- Registrar Novos Pagamentos --");
            System.out.println("Novo valor total do pedido: R$ " + novoValorTotal);
            boolean adicionarMaisPagamentos = true;
            do {
                System.out.printf("Forma de Pagamento (DINHEIRO, PIX, CARTAO_CREDITO, CARTAO_DEBITO, BOLETO): ");
                String formaPgto = scanner.nextLine();
                System.out.printf("Valor deste pagamento: R$ ");
                double valorPgto = lerDouble();
                System.out.printf("Status (PROCESSANDO, APROVADO, RECUSADO): ");
                String statusPgto = scanner.nextLine();

                novosPagamentos.add(new Pagamento(0, idPedido, formaPgto, LocalDateTime.now(), valorPgto, statusPgto));

                System.out.printf("Adicionar mais pagamentos? (1-Sim / 0-Não): ");
                adicionarMaisPagamentos = lerInteiro() == 1;
            } while (adicionarMaisPagamentos);

            System.out.printf("\nNovo Status do Pedido para adicionar ao histórico (ex: PAGO, FINALIZADO): ");
            String statusPedido = scanner.nextLine();
            HistoricoStatusPedido novoHistorico = new HistoricoStatusPedido(idPedido, LocalDateTime.now(), statusPedido);

            Pedido pedidoAtualizado = new Pedido(idPedido, idCliente, vendedorLogado.getIdPessoa(), LocalDateTime.now(), novoValorTotal);

            pedidoDAO.atualizarPedidoCompleto(pedidoAtualizado, novosItens, novosPagamentos, novoHistorico);
            System.out.println("\nPedido atualizado com sucesso!");

        } catch (Exception e) {
            System.out.println("Erro ao atualizar pedido: " + e.getMessage());
        }
    }

    private static void deletarPedidoCompleto() {
        System.out.println("\n--- DELETAR PEDIDO COMPLETO ---");
        try {
            System.out.printf("Digite o ID do Pedido que deseja excluir: ");
            int idPedido = lerInteiro();

            System.out.printf("Tem certeza que deseja excluir tudo relacionado a este pedido? O estoque será devolvido. (1-Sim / 0-Não): ");
            if (lerInteiro() == 1) {

                pedidoDAO.deletarPedidoCompleto(idPedido, vendedorLogado.getIdPessoa());
                System.out.println("Pedido e todas as suas dependências foram excluídos com sucesso!");
            } else {
                System.out.println("Operação cancelada.");
            }
        } catch (Exception e) {
            System.out.println("Erro ao deletar pedido: " + e.getMessage());
        }
    }

    private static int lerInteiro() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.printf("Entrada inválida. Digite um número inteiro: ");
            }
        }
    }

    private static double lerDouble() {
        while (true) {
            try {
                return Double.parseDouble(scanner.nextLine().trim().replace(",", "."));
            } catch (NumberFormatException e) {
                System.out.printf("Entrada inválida. Digite um número decimal: ");
            }
        }
    }
}
