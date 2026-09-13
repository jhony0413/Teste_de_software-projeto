package dao;

import models.HistoricoStatusPedido;
import database.ConexaoBD;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HistoricoStatusPedidoDAO {

    public void inserir(HistoricoStatusPedido historico) {
        String sql = "INSERT INTO historico_status_pedido (id_pedido, data_alteracao, status) VALUES (?, ?, ?)";
        try (Connection conn = ConexaoBD.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, historico.getIdPedido());
            stmt.setTimestamp(2, Timestamp.valueOf(historico.getDataAlteracao()));
            stmt.setString(3, historico.getStatus());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir histórico de status: " + e.getMessage(), e);
        }
    }

    public List<HistoricoStatusPedido> listarPorPedido(int idPedido) {
        List<HistoricoStatusPedido> lista = new ArrayList<>();
        String sql = "SELECT id_pedido, data_alteracao, status FROM historico_status_pedido WHERE id_pedido = ? ORDER BY data_alteracao DESC";
        try (Connection conn = ConexaoBD.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idPedido);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    LocalDateTime dataAlteracao = rs.getTimestamp("data_alteracao") != null ? rs.getTimestamp("data_alteracao").toLocalDateTime() : null;
                    HistoricoStatusPedido h = new HistoricoStatusPedido(
                            rs.getInt("id_pedido"),
                            dataAlteracao,
                            rs.getString("status")
                    );
                    lista.add(h);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar histórico do pedido: " + e.getMessage(), e);
        }
        return lista;
    }

    public void atualizar(HistoricoStatusPedido historico) {
        String sql = "UPDATE historico_status_pedido SET status = ? WHERE id_pedido = ? AND data_alteracao = ?";
        try (Connection conn = ConexaoBD.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, historico.getStatus());
            stmt.setInt(2, historico.getIdPedido());
            stmt.setTimestamp(3, Timestamp.valueOf(historico.getDataAlteracao()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar histórico de status: " + e.getMessage(), e);
        }
    }

    public void deletarPorPedido(int idPedido) {
        String sql = "DELETE FROM historico_status_pedido WHERE id_pedido = ?";
        try (Connection conn = ConexaoBD.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idPedido);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar histórico do pedido: " + e.getMessage(), e);
        }
    }
}
