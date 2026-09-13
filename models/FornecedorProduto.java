package models;

public class FornecedorProduto {

    private int idFornecedor;
    private int idProduto;
    private double precoCusto;
    private int prazoEntregaDias;
    private int quantidade;

    public FornecedorProduto() {
    }

    public FornecedorProduto(int idFornecedor, int idProduto, double precoCusto, int prazoEntregaDias, int quantidade) {
        setIdFornecedor(idFornecedor);
        setIdProduto(idProduto);
        setPrecoCusto(precoCusto);
        setPrazoEntregaDias(prazoEntregaDias);
        setQuantidade(quantidade);
    }

    public int getIdFornecedor() {
        return idFornecedor;
    }

    public void setIdFornecedor(int idFornecedor) {
        if (idFornecedor <= 0) {
            throw new IllegalArgumentException("ID do fornecedor inválido.");
        }
        this.idFornecedor = idFornecedor;
    }

    public int getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(int idProduto) {
        if (idProduto <= 0) {
            throw new IllegalArgumentException("ID do produto inválido.");
        }
        this.idProduto = idProduto;
    }

    public double getPrecoCusto() {
        return precoCusto;
    }

    public void setPrecoCusto(double precoCusto) {
        if (precoCusto < 0.0) {
            throw new IllegalArgumentException("O preço de custo não pode ser negativo.");
        }
        this.precoCusto = precoCusto;
    }

    public int getPrazoEntregaDias() {
        return prazoEntregaDias;
    }

    public void setPrazoEntregaDias(int prazoEntregaDias) {
        if (prazoEntregaDias <= 0) {
            throw new IllegalArgumentException("O prazo de entrega deve ser de pelo menos 1 dia.");
        }
        this.prazoEntregaDias = prazoEntregaDias;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        if (quantidade <= 0) {
            throw new IllegalArgumentException("A quantidade deve ser maior do que zero.");
        }
        this.quantidade = quantidade;
    }
}
