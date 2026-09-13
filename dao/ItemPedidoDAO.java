package dao;

import models.ItemPedido;
import database.ConexaoBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ItemPedidoDAO {

    public void inserir(ItemPedido item) {
        String sql = "INSERT INTO item_pedido (id_pedido, id_produto, quantidade, preco_unitario, subtotal) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConexaoBD.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, item.getIdPedido());
            stmt.setInt(2, item.getIdProduto());
            stmt.setInt(3, item.getQuantidade());
            stmt.setDouble(4, item.getPrecoUnitario());
            stmt.setDouble(5, item.getSubtotal());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir item do pedido: " + e.getMessage(), e);
        }
    }

    public List<ItemPedido> listarPorPedido(int idPedido) {
        List<ItemPedido> lista = new ArrayList<>();
        String sql = "SELECT id_pedido, id_produto, quantidade, preco_unitario, subtotal FROM item_pedido WHERE id_pedido = ?";
        try (Connection conn = ConexaoBD.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idPedido);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ItemPedido item = new ItemPedido(
                            rs.getInt("id_pedido"),
                            rs.getInt("id_produto"),
                            rs.getInt("quantidade"),
                            rs.getDouble("preco_unitario"),
                            rs.getDouble("subtotal")
                    );
                    lista.add(item);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar itens do pedido: " + e.getMessage(), e);
        }
        return lista;
    }

    public void atualizar(ItemPedido item) {
        String sql = "UPDATE item_pedido SET quantidade = ?, preco_unitario = ?, subtotal = ? WHERE id_pedido = ? AND id_produto = ?";
        try (Connection conn = ConexaoBD.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, item.getQuantidade());
            stmt.setDouble(2, item.getPrecoUnitario());
            stmt.setDouble(3, item.getSubtotal());
            stmt.setInt(4, item.getIdPedido());
            stmt.setInt(5, item.getIdProduto());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar item do pedido: " + e.getMessage(), e);
        }
    }

    public void deletar(int idPedido, int idProduto) {
        String sql = "DELETE FROM item_pedido WHERE id_pedido = ? AND id_produto = ?";
        try (Connection conn = ConexaoBD.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idPedido);
            stmt.setInt(2, idProduto);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar item do pedido: " + e.getMessage(), e);
        }
    }
}
