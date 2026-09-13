package models;

import java.time.LocalDateTime;

public class Pedido {

    private int idPedido;
    private int idCliente;
    private int idVendedor;
    private LocalDateTime dataEmissao;
    private double valorTotal;

    public Pedido() {
        this.dataEmissao = LocalDateTime.now();
    }

    public Pedido(int idPedido, int idCliente, int idVendedor, LocalDateTime dataEmissao, double valorTotal) {
        setIdPedido(idPedido);
        setIdCliente(idCliente);
        setIdVendedor(idVendedor);
        setDataEmissao(dataEmissao);
        setValorTotal(valorTotal);
    }

    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        if (idPedido < 0) {
            throw new IllegalArgumentException("O ID do pedido não pode ser negativo.");
        }
        this.idPedido = idPedido;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        if (idCliente <= 0) {
            throw new IllegalArgumentException("O pedido deve estar associado a um cliente válido.");
        }
        this.idCliente = idCliente;
    }

    public int getIdVendedor() {
        return idVendedor;
    }

    public void setIdVendedor(int idVendedor) {
        if (idVendedor <= 0) {
            throw new IllegalArgumentException("O pedido deve estar associado a um vendedor válido.");
        }
        this.idVendedor = idVendedor;
    }

    public LocalDateTime getDataEmissao() {
        return dataEmissao;
    }

    public void setDataEmissao(LocalDateTime dataEmissao) {
        if (dataEmissao != null && dataEmissao.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("A data de emissão não pode ser no futuro.");
        }
        this.dataEmissao = dataEmissao != null ? dataEmissao : LocalDateTime.now();
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(double valorTotal) {
        if (valorTotal < 0.0) {
            throw new IllegalArgumentException("O valor total do pedido não pode ser negativo.");
        }
        this.valorTotal = valorTotal;
    }
}
