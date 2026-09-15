package dao;

import models.Categoria;
import database.ConexaoBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAO {

    public void inserir(Categoria categoria) {
        String sql = "INSERT INTO categoria (id_vendedor, nome, descricao, id_categoria_pai) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConexaoBD.conectar(); PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, categoria.getIdVendedor());
            stmt.setString(2, categoria.getNome());
            stmt.setString(3, categoria.getDescricao());
            if (categoria.getIdCategoriaPai() != null) {
                stmt.setInt(4, categoria.getIdCategoriaPai());
            } else {
                stmt.setNull(4, Types.INTEGER);
            }
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    categoria.setIdCategoria(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir categoria: " + e.getMessage(), e);
        }
    }

    public List<Categoria> listarPorVendedor(int idVendedor) {
        List<Categoria> lista = new ArrayList<>();
        String sql = "SELECT id_categoria, id_vendedor, nome, descricao, id_categoria_pai FROM categoria WHERE id_vendedor = ?";
        try (Connection conn = ConexaoBD.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idVendedor);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Integer idPai = rs.getObject("id_categoria_pai") != null ? rs.getInt("id_categoria_pai") : null;
                    Categoria cat = new Categoria(
                            rs.getInt("id_categoria"),
                            rs.getInt("id_vendedor"),
                            rs.getString("nome"),
                            rs.getString("descricao"),
                            idPai
                    );
                    lista.add(cat);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar categorias: " + e.getMessage(), e);
        }
        return lista;
    }

    public void atualizar(Categoria categoria) {
        String sql = "UPDATE categoria SET id_vendedor = ?, nome = ?, descricao = ?, id_categoria_pai = ? WHERE id_categoria = ?";
        try (Connection conn = ConexaoBD.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, categoria.getIdVendedor());
            stmt.setString(2, categoria.getNome());
            stmt.setString(3, categoria.getDescricao());
            if (categoria.getIdCategoriaPai() != null) {
                stmt.setInt(4, categoria.getIdCategoriaPai());
            } else {
                stmt.setNull(4, Types.INTEGER);
            }
            stmt.setInt(5, categoria.getIdCategoria());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar categoria: " + e.getMessage(), e);
        }
    }

    public void deletar(int id, int idVendedor) {
        String sql = "DELETE FROM categoria WHERE id_categoria = ? AND id_vendedor = ?";
        try (Connection conn = ConexaoBD.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.setInt(2, idVendedor);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar categoria (verifique restrições de chave estrangeira): " + e.getMessage(), e);
        }
    }
}
