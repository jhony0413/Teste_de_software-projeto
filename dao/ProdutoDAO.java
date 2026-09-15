package dao;

import models.Produto;
import database.ConexaoBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO {

    public void inserir(Produto produto) {
        String sql = "INSERT INTO produto (id_vendedor, id_categoria, nome, descricao, preco_unitario, estoque, ativo) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexaoBD.conectar(); PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, produto.getIdVendedor());
            stmt.setInt(2, produto.getIdCategoria());
            stmt.setString(3, produto.getNome());
            stmt.setString(4, produto.getDescricao());
            stmt.setDouble(5, produto.getPrecoUnitario());
            stmt.setInt(6, produto.getEstoque());
            stmt.setBoolean(7, produto.isAtivo());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    produto.setIdProduto(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir produto: " + e.getMessage(), e);
        }
    }

    public List<Produto> listarPorVendedor(int idVendedor) {
        List<Produto> lista = new ArrayList<>();
        String sql = "SELECT * FROM produto WHERE id_vendedor = ?";
        try (Connection conn = ConexaoBD.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idVendedor);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Produto p = new Produto(
                            rs.getInt("id_produto"),
                            rs.getInt("id_vendedor"),
                            rs.getInt("id_categoria"),
                            rs.getString("nome"),
                            rs.getString("descricao"),
                            rs.getDouble("preco_unitario"),
                            rs.getInt("estoque"),
                            rs.getBoolean("ativo")
                    );
                    lista.add(p);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar produtos: " + e.getMessage(), e);
        }
        return lista;
    }

    public void atualizar(Produto produto) {
        String sql = "UPDATE produto SET id_vendedor = ?, id_categoria = ?, nome = ?, descricao = ?, preco_unitario = ?, estoque = ?, ativo = ? WHERE id_produto = ?";
        try (Connection conn = ConexaoBD.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, produto.getIdVendedor());
            stmt.setInt(2, produto.getIdCategoria());
            stmt.setString(3, produto.getNome());
            stmt.setString(4, produto.getDescricao());
            stmt.setDouble(5, produto.getPrecoUnitario());
            stmt.setInt(6, produto.getEstoque());
            stmt.setBoolean(7, produto.isAtivo());
            stmt.setInt(8, produto.getIdProduto());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar produto: " + e.getMessage(), e);
        }
    }

    public void deletar(int id, int idVendedor) {
        String sql = "DELETE FROM produto WHERE id_produto = ? AND id_vendedor = ?";
        try (Connection conn = ConexaoBD.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.setInt(2, idVendedor);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar produto: " + e.getMessage(), e);
        }
    }
}
