package models;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class Pedido {

    public static final int PENDENTE = 1;
    public static final int FINALIZADO = 2;
    public static final int CANCELADO = 3;

    private static final DecimalFormat formatadorDecimal = new DecimalFormat("R$ #,##0.00", new DecimalFormatSymbols(new Locale("pt", "BR")));

    private int idPedido;
    private Cliente cliente;
    private final List<ItemPedido> itens;
    private int status;

    /* Construtores */
    public Pedido() {
        this.itens = new ArrayList<>();
        this.status = PENDENTE;
    }

    public Pedido(int idPedido, Cliente cliente) {
        this();
        setIdPedido(idPedido);
        setCliente(cliente);
    }

    /* Getters e Setters com Validações */
    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        if (idPedido <= 0) {
            throw new IllegalArgumentException("ID do pedido deve ser maior que zero.");
        }
        this.idPedido = idPedido;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        if (cliente == null) {
            throw new IllegalArgumentException("Cliente não pode ser nulo.");
        }
        this.cliente = cliente;
    }

    public List<ItemPedido> getItens() {
        return Collections.unmodifiableList(itens);
    }

    public int getStatus() {
        return status;
    }

    /* Métodos de Domínio */
    public void adicionarItem(Produto produto, int quantidade) {
        if (status != PENDENTE) {
            throw new IllegalStateException("Não é possível alterar um pedido que não está pendente.");
        }
        if (produto == null) {
            throw new IllegalArgumentException("Produto não pode ser nulo.");
        }

        // Tenta localizar se o produto já existe no pedido
        for (ItemPedido item : itens) {
            if (item.getProduto().getIdProduto() == produto.getIdProduto()) {
                produto.reduzirEstoque(quantidade);
                item.setQuantidade(item.getQuantidade() + quantidade);
                return;
            }
        }

        // Se for um novo produto no pedido
        produto.reduzirEstoque(quantidade);
        ItemPedido novoItem = new ItemPedido(produto, quantidade);
        itens.add(novoItem);
    }

    public double getValorTotal() {
        double total = 0;
        for (ItemPedido item : itens) {
            total += item.getSubtotal();
        }
        return total;
    }

    public void finalizar() {
        if (status != PENDENTE) {
            throw new IllegalStateException("Somente pedidos pendentes podem ser finalizados.");
        }
        if (itens.isEmpty()) {
            throw new IllegalStateException("Não é possível finalizar um pedido sem itens.");
        }
        this.status = FINALIZADO;
    }

    public void cancelar() {
        if (status == FINALIZADO) {
            throw new IllegalStateException("Pedido finalizado não pode ser cancelado.");
        }
        if (status == CANCELADO) {
            throw new IllegalStateException("Pedido já está cancelado.");
        }

        // Devolve os itens ao estoque
        for (ItemPedido item : itens) {
            item.getProduto().adicionarEstoque(item.getQuantidade());
        }
        this.status = CANCELADO;
    }

    public void limparItens() {
        if (status != PENDENTE) {
            throw new IllegalStateException("Somente pedidos pendentes podem ter os itens limpos.");
        }
        for (ItemPedido item : itens) {
            item.getProduto().adicionarEstoque(item.getQuantidade());
        }
        itens.clear();
    }

    public String getStatusExtenso() {
        return switch (status) {
            case PENDENTE ->
                "PENDENTE";
            case FINALIZADO ->
                "FINALIZADO";
            case CANCELADO ->
                "CANCELADO";
            default ->
                "DESCONHECIDO";
        };
    }

    /* Sobrescritas de Objetos Java */
    @Override
    public String toString() {
        String nomeClienteStr = (cliente != null) ? cliente.getNomeCliente() : "Não informado";
        String totalFormatado;

        synchronized (formatadorDecimal) {
            totalFormatado = formatadorDecimal.format(getValorTotal());
        }

        StringBuilder construtorDeString = new StringBuilder();
        construtorDeString.append("""
                                  
                PEDIDO #%d
                Cliente: %s
                Status: %s
                ITENS:
                """.formatted(
                idPedido,
                nomeClienteStr,
                getStatusExtenso()));

        for (ItemPedido item : itens) {
            construtorDeString.append(item).append("\n");
        }

        construtorDeString.append("""
                                  
                -------------------------------
                TOTAL: %s
                -------------------------------
                """.formatted(totalFormatado));

        return construtorDeString.toString();
    }
}
