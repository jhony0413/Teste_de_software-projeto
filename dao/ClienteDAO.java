package dao;

import models.Cliente;
import database.ConexaoBD;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    public void inserir(Cliente cliente) {
        String sqlPessoa = "INSERT INTO pessoa (nome, cpf, email, telefone, ativo) VALUES (?, ?, ?, ?, ?)";
        String sqlCliente = "INSERT INTO cliente (id_pessoa, data_cadastro) VALUES (?, ?)";

        Connection conn = null;
        try {
            conn = ConexaoBD.conectar();
            conn.setAutoCommit(false);

            try (PreparedStatement stmtPessoa = conn.prepareStatement(sqlPessoa, Statement.RETURN_GENERATED_KEYS)) {
                stmtPessoa.setString(1, cliente.getNome());
                stmtPessoa.setString(2, cliente.getCpf());
                stmtPessoa.setString(3, cliente.getEmail());
                stmtPessoa.setString(4, cliente.getTelefone());
                stmtPessoa.setBoolean(5, cliente.isAtivo());
                stmtPessoa.executeUpdate();

                ResultSet rs = stmtPessoa.getGeneratedKeys();
                if (rs.next()) {
                    cliente.setIdPessoa(rs.getInt(1));
                }
            }

            try (PreparedStatement stmtCliente = conn.prepareStatement(sqlCliente)) {
                stmtCliente.setInt(1, cliente.getIdPessoa());
                stmtCliente.setTimestamp(2, Timestamp.valueOf(cliente.getDataCadastro()));
                stmtCliente.executeUpdate();
            }

            conn.commit();
        } catch (SQLException e) {
            if (conn != null) try {
                conn.rollback();
            } catch (SQLException ex) {
            }
            throw new RuntimeException("Erro ao inserir cliente: " + e.getMessage(), e);
        } finally {
            if (conn != null) try {
                conn.setAutoCommit(true);
                conn.close();
            } catch (SQLException e) {
            }
        }
    }

    public List<Cliente> listar() {
        List<Cliente> lista = new ArrayList<>();
        // Faz o JOIN para resgatar tanto os dados de pessoa quanto a data de cadastro do cliente
        String sql = "SELECT p.id_pessoa, p.nome, p.cpf, p.email, p.telefone, p.ativo, c.data_cadastro "
                + "FROM cliente c JOIN pessoa p ON c.id_pessoa = p.id_pessoa";

        try (Connection conn = ConexaoBD.conectar(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                LocalDateTime dataCadastro = rs.getTimestamp("data_cadastro") != null ? rs.getTimestamp("data_cadastro").toLocalDateTime() : null;
                Cliente c = new Cliente(
                        rs.getInt("id_pessoa"),
                        rs.getString("nome"),
                        rs.getString("cpf"),
                        rs.getString("email"),
                        rs.getString("telefone"),
                        rs.getBoolean("ativo"),
                        dataCadastro
                );
                lista.add(c);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar clientes: " + e.getMessage(), e);
        }
        return lista;
    }

    public void atualizar(Cliente cliente) {
        String sqlPessoa = "UPDATE pessoa SET nome = ?, cpf = ?, email = ?, telefone = ?, ativo = ? WHERE id_pessoa = ?";
        String sqlCliente = "UPDATE cliente SET data_cadastro = ? WHERE id_pessoa = ?";

        Connection conn = null;
        try {
            conn = ConexaoBD.conectar();
            conn.setAutoCommit(false);

            try (PreparedStatement stmtPessoa = conn.prepareStatement(sqlPessoa)) {
                stmtPessoa.setString(1, cliente.getNome());
                stmtPessoa.setString(2, cliente.getCpf());
                stmtPessoa.setString(3, cliente.getEmail());
                stmtPessoa.setString(4, cliente.getTelefone());
                stmtPessoa.setBoolean(5, cliente.isAtivo());
                stmtPessoa.setInt(6, cliente.getIdPessoa());
                stmtPessoa.executeUpdate();
            }

            try (PreparedStatement stmtCliente = conn.prepareStatement(sqlCliente)) {
                stmtCliente.setTimestamp(1, Timestamp.valueOf(cliente.getDataCadastro()));
                stmtCliente.setInt(2, cliente.getIdPessoa());
                stmtCliente.executeUpdate();
            }

            conn.commit();
        } catch (SQLException e) {
            if (conn != null) try {
                conn.rollback();
            } catch (SQLException ex) {
            }
            throw new RuntimeException("Erro ao atualizar cliente: " + e.getMessage(), e);
        } finally {
            if (conn != null) try {
                conn.setAutoCommit(true);
                conn.close();
            } catch (SQLException e) {
            }
        }
    }

    public void deletar(int id) {
        // Devido à restrição de chave estrangeira, apagamos primeiro da tabela filha e depois da pai
        // (Ou dependendo do banco, se houver CASCADE, apagar da pessoa já resolve, mas explicitamos para segurança)
        String sqlCliente = "DELETE FROM cliente WHERE id_pessoa = ?";
        String sqlPessoa = "DELETE FROM pessoa WHERE id_pessoa = ?";

        Connection conn = null;
        try {
            conn = ConexaoBD.conectar();
            conn.setAutoCommit(false);

            try (PreparedStatement stmtCliente = conn.prepareStatement(sqlCliente)) {
                stmtCliente.setInt(1, id);
                stmtCliente.executeUpdate();
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
            throw new RuntimeException("Erro ao deletar cliente: " + e.getMessage(), e);
        } finally {
            if (conn != null) try {
                conn.setAutoCommit(true);
                conn.close();
            } catch (SQLException e) {
            }
        }
    }
}
