package dao;

import models.Fornecedor;
import database.ConexaoBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FornecedorDAO {

    public void inserir(Fornecedor fornecedor) {
        String sql = "INSERT INTO fornecedor (id_vendedor, cnpj, nome, email, telefone, ativo) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexaoBD.conectar(); PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, fornecedor.getIdVendedor());
            stmt.setString(2, fornecedor.getCnpj());
            stmt.setString(3, fornecedor.getNome());
            stmt.setString(4, fornecedor.getEmail());
            stmt.setString(5, fornecedor.getTelefone());
            stmt.setBoolean(6, fornecedor.isAtivo());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    fornecedor.setIdFornecedor(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir fornecedor: " + e.getMessage(), e);
        }
    }

    public List<Fornecedor> listarPorVendedor(int idVendedor) {
        List<Fornecedor> lista = new ArrayList<>();
        String sql = "SELECT id_fornecedor, id_vendedor, cnpj, nome, email, telefone, ativo FROM fornecedor WHERE id_vendedor = ?";
        try (Connection conn = ConexaoBD.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idVendedor);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Fornecedor f = new Fornecedor(
                            rs.getInt("id_fornecedor"),
                            rs.getInt("id_vendedor"),
                            rs.getString("cnpj"),
                            rs.getString("nome"),
                            rs.getString("email"),
                            rs.getString("telefone"),
                            rs.getBoolean("ativo")
                    );
                    lista.add(f);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar fornecedores: " + e.getMessage(), e);
        }
        return lista;
    }

    public void atualizar(Fornecedor fornecedor) {
        String sql = "UPDATE fornecedor SET id_vendedor = ?, cnpj = ?, nome = ?, email = ?, telefone = ?, ativo = ? WHERE id_fornecedor = ?";
        try (Connection conn = ConexaoBD.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, fornecedor.getIdVendedor());
            stmt.setString(2, fornecedor.getCnpj());
            stmt.setString(3, fornecedor.getNome());
            stmt.setString(4, fornecedor.getEmail());
            stmt.setString(5, fornecedor.getTelefone());
            stmt.setBoolean(6, fornecedor.isAtivo());
            stmt.setInt(7, fornecedor.getIdFornecedor());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar fornecedor: " + e.getMessage(), e);
        }
    }

    public void deletar(int id, int idVendedor) {
        String sql = "DELETE FROM fornecedor WHERE id_fornecedor = ? AND id_vendedor = ?";
        try (Connection conn = ConexaoBD.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.setInt(2, idVendedor);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar fornecedor: " + e.getMessage(), e);
        }
    }
}
