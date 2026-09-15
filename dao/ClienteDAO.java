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
        String sqlCliente = "INSERT INTO cliente (id_pessoa, id_vendedor, data_cadastro) VALUES (?, ?, ?)";

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

                try (ResultSet rs = stmtPessoa.getGeneratedKeys()) {
                    if (rs.next()) {
                        cliente.setIdPessoa(rs.getInt(1));
                    }
                }
            }

            try (PreparedStatement stmtCliente = conn.prepareStatement(sqlCliente)) {
                stmtCliente.setInt(1, cliente.getIdPessoa());
                stmtCliente.setInt(2, cliente.getIdVendedor());
                stmtCliente.setTimestamp(3, Timestamp.valueOf(cliente.getDataCadastro()));
                stmtCliente.executeUpdate();
            }

            conn.commit();
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            throw new RuntimeException("Erro ao inserir cliente: " + e.getMessage(), e);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public List<Cliente> listarPorVendedor(int idVendedor) {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT p.id_pessoa, c.id_vendedor, p.nome, p.cpf, p.email, p.telefone, p.ativo, c.data_cadastro "
                + "FROM cliente c JOIN pessoa p ON c.id_pessoa = p.id_pessoa WHERE c.id_vendedor = ?";

        try (Connection conn = ConexaoBD.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idVendedor);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    LocalDateTime dataCadastro = rs.getTimestamp("data_cadastro") != null ? rs.getTimestamp("data_cadastro").toLocalDateTime() : null;
                    Cliente c = new Cliente(
                            rs.getInt("id_pessoa"),
                            rs.getInt("id_vendedor"),
                            rs.getString("nome"),
                            rs.getString("cpf"),
                            rs.getString("email"),
                            rs.getString("telefone"),
                            rs.getBoolean("ativo"),
                            dataCadastro
                    );
                    lista.add(c);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar clientes: " + e.getMessage(), e);
        }
        return lista;
    }

    public void atualizar(Cliente cliente) {
        String sqlPessoa = "UPDATE pessoa SET nome = ?, cpf = ?, email = ?, telefone = ?, ativo = ? WHERE id_pessoa = ?";
        String sqlCliente = "UPDATE cliente SET id_vendedor = ?, data_cadastro = ? WHERE id_pessoa = ?";

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
                stmtCliente.setInt(1, cliente.getIdVendedor());
                stmtCliente.setTimestamp(2, Timestamp.valueOf(cliente.getDataCadastro()));
                stmtCliente.setInt(3, cliente.getIdPessoa());
                stmtCliente.executeUpdate();
            }

            conn.commit();
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            throw new RuntimeException("Erro ao atualizar cliente: " + e.getMessage(), e);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void deletar(int id, int idVendedor) {
        String sqlCliente = "DELETE FROM cliente WHERE id_pessoa = ? AND id_vendedor = ?";
        String sqlPessoa = "DELETE FROM pessoa WHERE id_pessoa = ?";

        Connection conn = null;
        try {
            conn = ConexaoBD.conectar();
            conn.setAutoCommit(false);

            try (PreparedStatement stmtCliente = conn.prepareStatement(sqlCliente)) {
                stmtCliente.setInt(1, id);
                stmtCliente.setInt(2, idVendedor);
                stmtCliente.executeUpdate();
            }

            try (PreparedStatement stmtPessoa = conn.prepareStatement(sqlPessoa)) {
                stmtPessoa.setInt(1, id);
                stmtPessoa.executeUpdate();
            }

            conn.commit();
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            throw new RuntimeException("Erro ao deletar cliente: " + e.getMessage(), e);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
