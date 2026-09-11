package models;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class ItemPedido {

    private static final DecimalFormat formatadorDecimal = new DecimalFormat("R$ #,##0.00", new DecimalFormatSymbols(new Locale("pt", "BR")));

    private Produto produto;
    private int quantidade;

    /* Construtores */
    public ItemPedido() {
    }

    public ItemPedido(Produto produto, int quantidade) {
        setProduto(produto);
        setQuantidade(quantidade);
    }

    /* Getters e Setters com Validações */
    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        if (produto == null) {
            throw new IllegalArgumentException("O produto não pode ser nulo.");
        }
        this.produto = produto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        if (quantidade <= 0) {
            throw new IllegalArgumentException("A quantidade do item deve ser maior que zero.");
        }
        this.quantidade = quantidade;
    }

    /* Métodos de Domínio */
    public double getSubtotal() {
        return produto.getPrecoUnitario() * quantidade;
    }

    /* Sobrescritas de Objetos Java */
    @Override
    public String toString() {
        String subtotalFormatado;
        synchronized (formatadorDecimal) {
            subtotalFormatado = formatadorDecimal.format(getSubtotal());
        }

        return """
               
                Produto: %s
                Quantidade: %d
                Subtotal: %s
                -------------------------------
                """.formatted(
                produto.getNomeProduto(),
                quantidade,
                subtotalFormatado);
    }
}
