package dao;

import models.Vendedor;
import database.ConexaoBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VendedorDAO {

    public void inserir(Vendedor vendedor) {
        String sqlPessoa = "INSERT INTO pessoa (nome, cpf, email, telefone, ativo) VALUES (?, ?, ?, ?, ?)";
        String sqlVendedor = "INSERT INTO vendedor (id_pessoa, login, senha_hash) VALUES (?, ?, ?)";

        Connection conn = null;
        try {
            conn = ConexaoBD.conectar();
            conn.setAutoCommit(false);

            try (PreparedStatement stmtPessoa = conn.prepareStatement(sqlPessoa, Statement.RETURN_GENERATED_KEYS)) {
                stmtPessoa.setString(1, vendedor.getNome());
                stmtPessoa.setString(2, vendedor.getCpf());
                stmtPessoa.setString(3, vendedor.getEmail());
                stmtPessoa.setString(4, vendedor.getTelefone());
                stmtPessoa.setBoolean(5, vendedor.isAtivo());
                stmtPessoa.executeUpdate();

                ResultSet rs = stmtPessoa.getGeneratedKeys();
                if (rs.next()) {
                    vendedor.setIdPessoa(rs.getInt(1));
                }
            }

            try (PreparedStatement stmtVendedor = conn.prepareStatement(sqlVendedor)) {
                stmtVendedor.setInt(1, vendedor.getIdPessoa());
                stmtVendedor.setString(2, vendedor.getLogin());
                stmtVendedor.setString(3, vendedor.getSenhaHash());
                stmtVendedor.executeUpdate();
            }

            conn.commit();
        } catch (SQLException e) {
            if (conn != null) try {
                conn.rollback();
            } catch (SQLException ex) {
            }
            throw new RuntimeException("Erro ao inserir vendedor: " + e.getMessage(), e);
        } finally {
            if (conn != null) try {
                conn.setAutoCommit(true);
                conn.close();
            } catch (SQLException e) {
            }
        }
    }

    public List<Vendedor> listar() {
        List<Vendedor> lista = new ArrayList<>();
        String sql = "SELECT p.id_pessoa, p.nome, p.cpf, p.email, p.telefone, p.ativo, v.login, v.senha_hash "
                + "FROM vendedor v JOIN pessoa p ON v.id_pessoa = p.id_pessoa";

        try (Connection conn = ConexaoBD.conectar(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Vendedor v = new Vendedor(
                        rs.getInt("id_pessoa"),
                        rs.getString("nome"),
                        rs.getString("cpf"),
                        rs.getString("email"),
                        rs.getString("telefone"),
                        rs.getBoolean("ativo"),
                        rs.getString("login"),
                        rs.getString("senha_hash")
                );
                lista.add(v);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar vendedores: " + e.getMessage(), e);
        }
        return lista;
    }

    public void atualizar(Vendedor vendedor) {
        String sqlPessoa = "UPDATE pessoa SET nome = ?, cpf = ?, email = ?, telefone = ?, ativo = ? WHERE id_pessoa = ?";
        String sqlVendedor = "UPDATE vendedor SET login = ?, senha_hash = ? WHERE id_pessoa = ?";

        Connection conn = null;
        try {
            conn = ConexaoBD.conectar();
            conn.setAutoCommit(false);

            try (PreparedStatement stmtPessoa = conn.prepareStatement(sqlPessoa)) {
                stmtPessoa.setString(1, vendedor.getNome());
                stmtPessoa.setString(2, vendedor.getCpf());
                stmtPessoa.setString(3, vendedor.getEmail());
                stmtPessoa.setString(4, vendedor.getTelefone());
                stmtPessoa.setBoolean(5, vendedor.isAtivo());
                stmtPessoa.setInt(6, vendedor.getIdPessoa());
                stmtPessoa.executeUpdate();
            }

            try (PreparedStatement stmtVendedor = conn.prepareStatement(sqlVendedor)) {
                stmtVendedor.setString(1, vendedor.getLogin());
                stmtVendedor.setString(2, vendedor.getSenhaHash());
                stmtVendedor.setInt(3, vendedor.getIdPessoa());
                stmtVendedor.executeUpdate();
            }

            conn.commit();
        } catch (SQLException e) {
            if (conn != null) try {
                conn.rollback();
            } catch (SQLException ex) {
            }
            throw new RuntimeException("Erro ao atualizar vendedor: " + e.getMessage(), e);
        } finally {
            if (conn != null) try {
                conn.setAutoCommit(true);
                conn.close();
            } catch (SQLException e) {
            }
        }
    }

    public void deletar(int id) {
        String sqlVendedor = "DELETE FROM vendedor WHERE id_pessoa = ?";
        String sqlPessoa = "DELETE FROM pessoa WHERE id_pessoa = ?";

        Connection conn = null;
        try {
            conn = ConexaoBD.conectar();
            conn.setAutoCommit(false);

            try (PreparedStatement stmtVendedor = conn.prepareStatement(sqlVendedor)) {
                stmtVendedor.setInt(1, id);
                stmtVendedor.executeUpdate();
            }

            try (PreparedStatement stmtPessoa = conn.prepareStatement(sqlPessoa)) {
                stmtPessoa.setInt(1, id);
                stmtPessoa.executeUpdate();
            }

            conn.commit();
        } catch (SQLException e) {
            if (conn != null) try {
                conn.rollback();
            } catch (SQLException ex) {
            }
            throw new RuntimeException("Erro ao deletar vendedor: " + e.getMessage(), e);
        } finally {
            if (conn != null) try {
                conn.setAutoCommit(true);
                conn.close();
            } catch (SQLException e) {
            }
        }
    }

    public Vendedor autenticar(String login, String senha) {
        String sql = "SELECT p.id_pessoa, p.nome, p.cpf, p.email, p.ativo, v.login, v.senha_hash "
                + "FROM vendedor v "
                + "INNER JOIN pessoa p ON v.id_pessoa = p.id_pessoa "
                + "WHERE v.login = ? AND v.senha_hash = ? AND p.ativo = true";

        try (Connection conn = ConexaoBD.conectar();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, login);
            stmt.setString(2, senha);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Vendedor(
                            rs.getInt("id_pessoa"),
                            rs.getString("nome"),
                            rs.getString("cpf"),
                            rs.getString("email"),
                            null,
                            rs.getBoolean("ativo"),
                            rs.getString("login"),
                            rs.getString("senha_hash")
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro na autenticação: " + e.getMessage());
        }
        return null;
    }
}
