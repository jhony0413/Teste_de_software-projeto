package dao;

import models.FornecedorProduto;
import database.ConexaoBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FornecedorProdutoDAO {

    public void inserir(FornecedorProduto fp) {
        String sql = "INSERT INTO fornecedor_produto (id_fornecedor, id_produto, id_vendedor, preco_custo, prazo_entrega_dias, quantidade) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexaoBD.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, fp.getIdFornecedor());
            stmt.setInt(2, fp.getIdProduto());
            stmt.setInt(3, fp.getIdVendedor());
            stmt.setDouble(4, fp.getPrecoCusto());
            stmt.setInt(5, fp.getPrazoEntregaDias());
            stmt.setInt(6, fp.getQuantidade());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao associar fornecedor ao produto: " + e.getMessage(), e);
        }
    }

    public List<FornecedorProduto> listarPorVendedor(int idVendedor) {
        List<FornecedorProduto> lista = new ArrayList<>();
        String sql = "SELECT id_fornecedor, id_produto, id_vendedor, preco_custo, prazo_entrega_dias, quantidade FROM fornecedor_produto WHERE id_vendedor = ?";
        try (Connection conn = ConexaoBD.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idVendedor);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    FornecedorProduto fp = new FornecedorProduto(
                            rs.getInt("id_fornecedor"),
                            rs.getInt("id_produto"),
                            rs.getInt("id_vendedor"),
                            rs.getDouble("preco_custo"),
                            rs.getInt("prazo_entrega_dias"),
                            rs.getInt("quantidade")
                    );
                    lista.add(fp);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar associações do vendedor: " + e.getMessage(), e);
        }
        return lista;
    }

    public void atualizar(FornecedorProduto fp) {
        String sql = "UPDATE fornecedor_produto SET preco_custo = ?, prazo_entrega_dias = ?, quantidade = ? WHERE id_fornecedor = ? AND id_produto = ? AND id_vendedor = ?";
        try (Connection conn = ConexaoBD.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, fp.getPrecoCusto());
            stmt.setInt(2, fp.getPrazoEntregaDias());
            stmt.setInt(3, fp.getQuantidade());
            stmt.setInt(4, fp.getIdFornecedor());
            stmt.setInt(5, fp.getIdProduto());
            stmt.setInt(6, fp.getIdVendedor());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar associação fornecedor-produto: " + e.getMessage(), e);
        }
    }

    public void deletar(int idFornecedor, int idProduto, int idVendedor) {
        String sql = "DELETE FROM fornecedor_produto WHERE id_fornecedor = ? AND id_produto = ? AND id_vendedor = ?";
        try (Connection conn = ConexaoBD.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idFornecedor);
            stmt.setInt(2, idProduto);
            stmt.setInt(3, idVendedor);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao remover associação fornecedor-produto: " + e.getMessage(), e);
        }
    }
}
