package models;

import java.time.LocalDateTime;

public class Pagamento {

    private int idPagamento;
    private int idPedido;
    private String formaPagamento;
    private LocalDateTime dataHora;
    private double valor;
    private String status;

    public Pagamento() {
        this.dataHora = LocalDateTime.now();
    }

    public Pagamento(int idPagamento, int idPedido, String formaPagamento, LocalDateTime dataHora, double valor, String status) {
        setIdPagamento(idPagamento);
        setIdPedido(idPedido);
        setFormaPagamento(formaPagamento);
        setDataHora(dataHora);
        setValor(valor);
        setStatus(status);
    }

    public int getIdPagamento() {
        return idPagamento;
    }

    public void setIdPagamento(int idPagamento) {
        if (idPagamento < 0) {
            throw new IllegalArgumentException("O ID do pagamento não pode ser negativo.");
        }
        this.idPagamento = idPagamento;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        if (idPedido <= 0) {
            throw new IllegalArgumentException("O pagamento deve estar associado a um pedido válido.");
        }
        this.idPedido = idPedido;
    }

    public String getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(String formaPagamento) {
        if (formaPagamento == null || formaPagamento.trim().isEmpty()) {
            throw new IllegalArgumentException("A forma de pagamento não pode ser vazia.");
        }
        String formaUpper = formaPagamento.trim().toUpperCase();
        if (!formaUpper.equals("DINHEIRO") && !formaUpper.equals("CARTAO_CREDITO")
                && !formaUpper.equals("CARTAO_DEBITO") && !formaUpper.equals("PIX") && !formaUpper.equals("BOLETO")) {
            throw new IllegalArgumentException("Forma de pagamento inválida.");
        }
        this.formaPagamento = formaUpper;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora != null ? dataHora : LocalDateTime.now();
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        if (valor <= 0.0) {
            throw new IllegalArgumentException("O valor do pagamento deve ser maior que zero.");
        }
        this.valor = valor;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            throw new IllegalArgumentException("O status do pagamento não pode ser vazio.");
        }
        String statusUpper = status.trim().toUpperCase();
        if (!statusUpper.equals("PROCESSANDO") && !statusUpper.equals("APROVADO")
                && !statusUpper.equals("RECUSADO") && !statusUpper.equals("ESTORNADO")) {
            throw new IllegalArgumentException("Status de pagamento inválido.");
        }
        this.status = statusUpper;
    }
}
