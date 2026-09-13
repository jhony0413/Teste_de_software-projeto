package dao;

import models.Pedido;
import database.ConexaoBD;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAO {

    public void inserir(Pedido pedido) {
        String sql = "INSERT INTO pedido (id_cliente, id_vendedor, data_emissao, valor_total) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConexaoBD.conectar(); PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, pedido.getIdCliente());
            stmt.setInt(2, pedido.getIdVendedor());
            stmt.setTimestamp(3, Timestamp.valueOf(pedido.getDataEmissao()));
            stmt.setDouble(4, pedido.getValorTotal());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                pedido.setIdPedido(rs.getInt(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir pedido: " + e.getMessage(), e);
        }
    }

    public List<Pedido> listar() {
        List<Pedido> lista = new ArrayList<>();
        String sql = "SELECT id_pedido, id_cliente, id_vendedor, data_emissao, valor_total FROM pedido";
        try (Connection conn = ConexaoBD.conectar(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                LocalDateTime dataEmissao = rs.getTimestamp("data_emissao") != null ? rs.getTimestamp("data_emissao").toLocalDateTime() : null;
                Pedido p = new Pedido(
                        rs.getInt("id_pedido"),
                        rs.getInt("id_cliente"),
                        rs.getInt("id_vendedor"),
                        dataEmissao,
                        rs.getDouble("valor_total")
                );
                lista.add(p);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar pedidos: " + e.getMessage(), e);
        }
        return lista;
    }

    public void atualizar(Pedido pedido) {
        String sql = "UPDATE pedido SET id_cliente = ?, id_vendedor = ?, data_emissao = ?, valor_total = ? WHERE id_pedido = ?";
        try (Connection conn = ConexaoBD.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, pedido.getIdCliente());
            stmt.setInt(2, pedido.getIdVendedor());
            stmt.setTimestamp(3, Timestamp.valueOf(pedido.getDataEmissao()));
            stmt.setDouble(4, pedido.getValorTotal());
            stmt.setInt(5, pedido.getIdPedido());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar pedido: " + e.getMessage(), e);
        }
    }

    public void deletar(int id) {
        String sql = "DELETE FROM pedido WHERE id_pedido = ?";
        try (Connection conn = ConexaoBD.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar pedido: " + e.getMessage(), e);
        }
    }
}
