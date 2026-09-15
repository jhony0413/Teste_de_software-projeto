package models;

import java.time.LocalDateTime;

public class HistoricoStatusPedido {

    private int idPedido;
    private LocalDateTime dataAlteracao;
    private String status;

    public HistoricoStatusPedido() {
        this.dataAlteracao = LocalDateTime.now();
    }

    public HistoricoStatusPedido(int idPedido, LocalDateTime dataAlteracao, String status) {
        setIdPedido(idPedido);
        setDataAlteracao(dataAlteracao);
        setStatus(status);
    }

    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        if (idPedido < 0) {
            throw new IllegalArgumentException("ID do pedido inválido para o histórico.");
        }
        this.idPedido = idPedido;
    }

    public LocalDateTime getDataAlteracao() {
        return dataAlteracao;
    }

    public void setDataAlteracao(LocalDateTime dataAlteracao) {
        if (dataAlteracao != null && dataAlteracao.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("A data de alteração não pode ser no futuro.");
        }
        this.dataAlteracao = dataAlteracao != null ? dataAlteracao : LocalDateTime.now();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            throw new IllegalArgumentException("O status não pode ser nulo ou vazio.");
        }
        String statusUpper = status.trim().toUpperCase();
        if (!statusUpper.equals("PENDENTE") && !statusUpper.equals("PAGO")
                && !statusUpper.equals("FINALIZADO") && !statusUpper.equals("CANCELADO")) {
            throw new IllegalArgumentException("Status inválido. Permitidos: PENDENTE, PAGO, FINALIZADO, CANCELADO.");
        }
        this.status = statusUpper;
    }
}
