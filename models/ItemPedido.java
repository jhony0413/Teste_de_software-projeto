package models;

public class ItemPedido {

    private int idPedido;
    private int idProduto;
    private int quantidade;
    private double precoUnitario;
    private double subtotal;

    public ItemPedido() {
    }

    public ItemPedido(int idPedido, int idProduto, int quantidade, double precoUnitario, double subtotal) {
        setIdPedido(idPedido);
        setIdProduto(idProduto);
        setQuantidade(quantidade);
        setPrecoUnitario(precoUnitario);
    }

    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        if (idPedido < 0) {
            throw new IllegalArgumentException("ID do pedido inválido para o item.");
        }
        this.idPedido = idPedido;
    }

    public int getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(int idProduto) {
        if (idProduto < 0) {
            throw new IllegalArgumentException("ID do produto inválido para o item.");
        }
        this.idProduto = idProduto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        if (quantidade <= 0) {
            throw new IllegalArgumentException("A quantidade do item deve ser maior que zero.");
        }
        this.quantidade = quantidade;
        atualizarSubtotal();
    }

    public double getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(double precoUnitario) {
        if (precoUnitario < 0.0) {
            throw new IllegalArgumentException("O preço unitário do item não pode ser negativo.");
        }
        this.precoUnitario = precoUnitario;
        atualizarSubtotal();
    }

    public double getSubtotal() {
        return subtotal;
    }

    private void atualizarSubtotal() {
        this.subtotal = this.quantidade * this.precoUnitario;
    }
}
