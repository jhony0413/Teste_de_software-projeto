package dao;

import models.Pagamento;
import database.ConexaoBD;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PagamentoDAO {

    public void inserir(Pagamento pagamento) {
        String sql = "INSERT INTO pagamento (id_pedido, forma_pagamento, data_hora, valor, status) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConexaoBD.conectar(); PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, pagamento.getIdPedido());
            stmt.setString(2, pagamento.getFormaPagamento());
            stmt.setTimestamp(3, Timestamp.valueOf(pagamento.getDataHora()));
            stmt.setDouble(4, pagamento.getValor());
            stmt.setString(5, pagamento.getStatus());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    pagamento.setIdPagamento(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir pagamento: " + e.getMessage(), e);
        }
    }

    public List<Pagamento> listar() {
        List<Pagamento> lista = new ArrayList<>();
        String sql = "SELECT id_pagamento, id_pedido, forma_pagamento, data_hora, valor, status FROM pagamento";
        try (Connection conn = ConexaoBD.conectar(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                LocalDateTime dataHora = rs.getTimestamp("data_hora") != null ? rs.getTimestamp("data_hora").toLocalDateTime() : null;
                Pagamento p = new Pagamento(
                        rs.getInt("id_pagamento"),
                        rs.getInt("id_pedido"),
                        rs.getString("forma_pagamento"),
                        dataHora,
                        rs.getDouble("valor"),
                        rs.getString("status")
                );
                lista.add(p);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar pagamentos: " + e.getMessage(), e);
        }
        return lista;
    }

    public void atualizar(Pagamento pagamento) {
        String sql = "UPDATE pagamento SET id_pedido = ?, forma_pagamento = ?, data_hora = ?, valor = ?, status = ? WHERE id_pagamento = ?";
        try (Connection conn = ConexaoBD.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, pagamento.getIdPedido());
            stmt.setString(2, pagamento.getFormaPagamento());
            stmt.setTimestamp(3, Timestamp.valueOf(pagamento.getDataHora()));
            stmt.setDouble(4, pagamento.getValor());
            stmt.setString(5, pagamento.getStatus());
            stmt.setInt(6, pagamento.getIdPagamento());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar pagamento: " + e.getMessage(), e);
        }
    }

    public void deletar(int id) {
        String sql = "DELETE FROM pagamento WHERE id_pagamento = ?";
        try (Connection conn = ConexaoBD.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar pagamento: " + e.getMessage(), e);
        }
    }
}
