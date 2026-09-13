package dao;

import models.FornecedorProduto;
import database.ConexaoBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FornecedorProdutoDAO {

    public void inserir(FornecedorProduto fp) {
        String sql = "INSERT INTO fornecedor_produto (id_fornecedor, id_produto, preco_custo, prazo_entrega_dias, quantidade) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConexaoBD.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, fp.getIdFornecedor());
            stmt.setInt(2, fp.getIdProduto());
            stmt.setDouble(3, fp.getPrecoCusto());
            stmt.setInt(4, fp.getPrazoEntregaDias());
            stmt.setInt(5, fp.getQuantidade());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao associar fornecedor ao produto: " + e.getMessage(), e);
        }
    }

    public List<FornecedorProduto> listarPorFornecedor(int idFornecedor) {
        List<FornecedorProduto> lista = new ArrayList<>();
        String sql = "SELECT id_fornecedor, id_produto, preco_custo, prazo_entrega_dias, quantidade FROM fornecedor_produto WHERE id_fornecedor = ?";
        try (Connection conn = ConexaoBD.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idFornecedor);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    FornecedorProduto fp = new FornecedorProduto(
                            rs.getInt("id_fornecedor"),
                            rs.getInt("id_produto"),
                            rs.getDouble("preco_custo"),
                            rs.getInt("prazo_entrega_dias"),
                            rs.getInt("quantidade")
                    );
                    lista.add(fp);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar produtos do fornecedor: " + e.getMessage(), e);
        }
        return lista;
    }

    public void atualizar(FornecedorProduto fp) {
        String sql = "UPDATE fornecedor_produto SET preco_custo = ?, prazo_entrega_dias = ?, quantidade = ? WHERE id_fornecedor = ? AND id_produto = ?";
        try (Connection conn = ConexaoBD.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, fp.getPrecoCusto());
            stmt.setInt(2, fp.getPrazoEntregaDias());
            stmt.setInt(3, fp.getQuantidade());
            stmt.setInt(4, fp.getIdFornecedor());
            stmt.setInt(5, fp.getIdProduto());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar associação fornecedor-produto: " + e.getMessage(), e);
        }
    }

    public void deletar(int idFornecedor, int idProduto) {
        String sql = "DELETE FROM fornecedor_produto WHERE id_fornecedor = ? AND id_produto = ?";
        try (Connection conn = ConexaoBD.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idFornecedor);
            stmt.setInt(2, idProduto);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao remover associação fornecedor-produto: " + e.getMessage(), e);
        }
    }
}
