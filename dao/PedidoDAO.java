package dao;

import models.*;
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

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    pedido.setIdPedido(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir pedido: " + e.getMessage(), e);
        }
    }

    public List<Pedido> listarPorVendedor(int idVendedor) {
        List<Pedido> lista = new ArrayList<>();
        String sql = "SELECT id_pedido, id_cliente, id_vendedor, data_emissao, valor_total FROM pedido WHERE id_vendedor = ?";
        try (Connection conn = ConexaoBD.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idVendedor);
            try (ResultSet rs = stmt.executeQuery()) {
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

    public void deletar(int id, int idVendedor) {
        String sql = "DELETE FROM pedido WHERE id_pedido = ? AND id_vendedor = ?";
        try (Connection conn = ConexaoBD.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.setInt(2, idVendedor);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar pedido: " + e.getMessage(), e);
        }
    }

    public void inserirPedidoCompleto(Pedido pedido, List<ItemPedido> itens, List<Pagamento> pagamentos, HistoricoStatusPedido historico) throws SQLException {
        String sqlPedido = "INSERT INTO pedido (id_cliente, id_vendedor, data_emissao, valor_total) VALUES (?, ?, ?, ?)";
        String sqlItem = "INSERT INTO item_pedido (id_pedido, id_produto, quantidade, preco_unitario, subtotal) VALUES (?, ?, ?, ?, ?)";
        String sqlUpdateEstoque = "UPDATE produto SET estoque = estoque - ? WHERE id_produto = ? AND estoque >= ?";
        String sqlPgto = "INSERT INTO pagamento (id_pedido, forma_pagamento, data_hora, valor, status) VALUES (?, ?, ?, ?, ?)";
        String sqlHist = "INSERT INTO historico_status_pedido (id_pedido, data_alteracao, status) VALUES (?, ?, ?)";

        Connection conn = null;
        try {
            conn = ConexaoBD.conectar();
            conn.setAutoCommit(false);

            int idPedidoGerado;
            try (PreparedStatement stmtPedido = conn.prepareStatement(sqlPedido, Statement.RETURN_GENERATED_KEYS)) {
                stmtPedido.setInt(1, pedido.getIdCliente());
                stmtPedido.setInt(2, pedido.getIdVendedor());
                stmtPedido.setTimestamp(3, Timestamp.valueOf(pedido.getDataEmissao()));
                stmtPedido.setDouble(4, pedido.getValorTotal());
                stmtPedido.executeUpdate();

                try (ResultSet rs = stmtPedido.getGeneratedKeys()) {
                    if (!rs.next()) {
                        throw new SQLException("Falha ao obter ID do pedido.");
                    }
                    idPedidoGerado = rs.getInt(1);
                }
            }

            try (PreparedStatement stmtItem = conn.prepareStatement(sqlItem); PreparedStatement stmtEstoque = conn.prepareStatement(sqlUpdateEstoque)) {

                for (ItemPedido item : itens) {
                    stmtEstoque.setInt(1, item.getQuantidade());
                    stmtEstoque.setInt(2, item.getIdProduto());
                    stmtEstoque.setInt(3, item.getQuantidade());
                    if (stmtEstoque.executeUpdate() == 0) {
                        throw new SQLException("Estoque insuficiente para o produto ID: " + item.getIdProduto());
                    }

                    stmtItem.setInt(1, idPedidoGerado);
                    stmtItem.setInt(2, item.getIdProduto());
                    stmtItem.setInt(3, item.getQuantidade());
                    stmtItem.setDouble(4, item.getPrecoUnitario());
                    stmtItem.setDouble(5, item.getSubtotal());
                    stmtItem.addBatch();
                }
                stmtItem.executeBatch();
            }

            try (PreparedStatement stmtPgto = conn.prepareStatement(sqlPgto)) {
                for (Pagamento pagamento : pagamentos) {
                    stmtPgto.setInt(1, idPedidoGerado);
                    stmtPgto.setString(2, pagamento.getFormaPagamento());
                    stmtPgto.setTimestamp(3, Timestamp.valueOf(pagamento.getDataHora()));
                    stmtPgto.setDouble(4, pagamento.getValor());
                    stmtPgto.setString(5, pagamento.getStatus());
                    stmtPgto.addBatch();
                }
                stmtPgto.executeBatch();
            }

            try (PreparedStatement stmtHist = conn.prepareStatement(sqlHist)) {
                stmtHist.setInt(1, idPedidoGerado);
                stmtHist.setTimestamp(2, Timestamp.valueOf(historico.getDataAlteracao()));
                stmtHist.setString(3, historico.getStatus());
                stmtHist.executeUpdate();
            }

            conn.commit();
            pedido.setIdPedido(idPedidoGerado);

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            throw e;
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

    public void atualizarPedidoCompleto(Pedido pedido, List<ItemPedido> novosItens, List<Pagamento> novosPagamentos, HistoricoStatusPedido novoHistorico) throws SQLException {
        String sqlVerificaDono = "SELECT 1 FROM pedido WHERE id_pedido = ? AND id_vendedor = ?";
        String sqlPedido = "UPDATE pedido SET id_cliente = ?, data_emissao = ?, valor_total = ? WHERE id_pedido = ?";

        String sqlSelItensAntigos = "SELECT id_produto, quantidade FROM item_pedido WHERE id_pedido = ?";
        String sqlEstoqueDevolve = "UPDATE produto SET estoque = estoque + ? WHERE id_produto = ?";
        String sqlDelItensAntigos = "DELETE FROM item_pedido WHERE id_pedido = ?";

        String sqlInsItem = "INSERT INTO item_pedido (id_pedido, id_produto, quantidade, preco_unitario, subtotal) VALUES (?, ?, ?, ?, ?)";
        String sqlEstoqueRetira = "UPDATE produto SET estoque = estoque - ? WHERE id_produto = ? AND estoque >= ?";

        String sqlDelPgto = "DELETE FROM pagamento WHERE id_pedido = ?";
        String sqlInsPgto = "INSERT INTO pagamento (id_pedido, forma_pagamento, data_hora, valor, status) VALUES (?, ?, ?, ?, ?)";
        String sqlInsHist = "INSERT INTO historico_status_pedido (id_pedido, data_alteracao, status) VALUES (?, ?, ?)";

        Connection conn = null;
        try {
            conn = ConexaoBD.conectar();
            conn.setAutoCommit(false);

            try (PreparedStatement stmtV = conn.prepareStatement(sqlVerificaDono)) {
                stmtV.setInt(1, pedido.getIdPedido());
                stmtV.setInt(2, pedido.getIdVendedor());
                try (ResultSet rs = stmtV.executeQuery()) {
                    if (!rs.next()) {
                        throw new SQLException("Pedido não encontrado ou não pertence à sua conta.");
                    }
                }
            }

            try (PreparedStatement stmt = conn.prepareStatement(sqlPedido)) {
                stmt.setInt(1, pedido.getIdCliente());
                stmt.setTimestamp(2, Timestamp.valueOf(pedido.getDataEmissao()));
                stmt.setDouble(3, pedido.getValorTotal());
                stmt.setInt(4, pedido.getIdPedido());
                stmt.executeUpdate();
            }

            try (PreparedStatement stmtSel = conn.prepareStatement(sqlSelItensAntigos); PreparedStatement stmtDev = conn.prepareStatement(sqlEstoqueDevolve); PreparedStatement stmtDel = conn.prepareStatement(sqlDelItensAntigos)) {

                stmtSel.setInt(1, pedido.getIdPedido());
                try (ResultSet rs = stmtSel.executeQuery()) {
                    while (rs.next()) {
                        stmtDev.setInt(1, rs.getInt("quantidade"));
                        stmtDev.setInt(2, rs.getInt("id_produto"));
                        stmtDev.addBatch();
                    }
                }
                stmtDev.executeBatch();

                stmtDel.setInt(1, pedido.getIdPedido());
                stmtDel.executeUpdate();
            }

            try (PreparedStatement stmtItem = conn.prepareStatement(sqlInsItem); PreparedStatement stmtEstoque = conn.prepareStatement(sqlEstoqueRetira)) {

                for (ItemPedido item : novosItens) {
                    stmtEstoque.setInt(1, item.getQuantidade());
                    stmtEstoque.setInt(2, item.getIdProduto());
                    stmtEstoque.setInt(3, item.getQuantidade());
                    if (stmtEstoque.executeUpdate() == 0) {
                        throw new SQLException("Estoque insuficiente para o produto ID: " + item.getIdProduto());
                    }

                    stmtItem.setInt(1, pedido.getIdPedido());
                    stmtItem.setInt(2, item.getIdProduto());
                    stmtItem.setInt(3, item.getQuantidade());
                    stmtItem.setDouble(4, item.getPrecoUnitario());
                    stmtItem.setDouble(5, item.getSubtotal());
                    stmtItem.addBatch();
                }
                stmtItem.executeBatch();
            }

            try (PreparedStatement stmtDelPgto = conn.prepareStatement(sqlDelPgto); PreparedStatement stmtInsPgto = conn.prepareStatement(sqlInsPgto)) {

                stmtDelPgto.setInt(1, pedido.getIdPedido());
                stmtDelPgto.executeUpdate();

                for (Pagamento pgto : novosPagamentos) {
                    stmtInsPgto.setInt(1, pedido.getIdPedido());
                    stmtInsPgto.setString(2, pgto.getFormaPagamento());
                    stmtInsPgto.setTimestamp(3, Timestamp.valueOf(pgto.getDataHora()));
                    stmtInsPgto.setDouble(4, pgto.getValor());
                    stmtInsPgto.setString(5, pgto.getStatus());
                    stmtInsPgto.addBatch();
                }
                stmtInsPgto.executeBatch();
            }

            try (PreparedStatement stmtHist = conn.prepareStatement(sqlInsHist)) {
                stmtHist.setInt(1, pedido.getIdPedido());
                stmtHist.setTimestamp(2, Timestamp.valueOf(novoHistorico.getDataAlteracao()));
                stmtHist.setString(3, novoHistorico.getStatus());
                stmtHist.executeUpdate();
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
            throw e;
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

    public void deletarPedidoCompleto(int idPedido, int idVendedor) throws SQLException {
        String sqlVerificaDono = "SELECT 1 FROM pedido WHERE id_pedido = ? AND id_vendedor = ?";
        String sqlDelHist = "DELETE FROM historico_status_pedido WHERE id_pedido = ?";
        String sqlDelPgto = "DELETE FROM pagamento WHERE id_pedido = ?";
        String sqlSelItens = "SELECT id_produto, quantidade FROM item_pedido WHERE id_pedido = ?";
        String sqlUpEstoque = "UPDATE produto SET estoque = estoque + ? WHERE id_produto = ?";
        String sqlDelItens = "DELETE FROM item_pedido WHERE id_pedido = ?";
        String sqlDelPedido = "DELETE FROM pedido WHERE id_pedido = ?";

        Connection conn = null;
        try {
            conn = ConexaoBD.conectar();
            conn.setAutoCommit(false);

            try (PreparedStatement stmtV = conn.prepareStatement(sqlVerificaDono)) {
                stmtV.setInt(1, idPedido);
                stmtV.setInt(2, idVendedor);
                try (ResultSet rs = stmtV.executeQuery()) {
                    if (!rs.next()) {
                        throw new SQLException("Pedido não encontrado ou não pertence à sua conta.");
                    }
                }
            }

            try (PreparedStatement stmt = conn.prepareStatement(sqlDelHist)) {
                stmt.setInt(1, idPedido);
                stmt.executeUpdate();
            }

            try (PreparedStatement stmt = conn.prepareStatement(sqlDelPgto)) {
                stmt.setInt(1, idPedido);
                stmt.executeUpdate();
            }

            try (PreparedStatement stmtSel = conn.prepareStatement(sqlSelItens); PreparedStatement stmtUp = conn.prepareStatement(sqlUpEstoque); PreparedStatement stmtDelItem = conn.prepareStatement(sqlDelItens)) {

                stmtSel.setInt(1, idPedido);
                try (ResultSet rs = stmtSel.executeQuery()) {
                    while (rs.next()) {
                        stmtUp.setInt(1, rs.getInt("quantidade"));
                        stmtUp.setInt(2, rs.getInt("id_produto"));
                        stmtUp.addBatch();
                    }
                }
                stmtUp.executeBatch();

                stmtDelItem.setInt(1, idPedido);
                stmtDelItem.executeUpdate();
            }

            try (PreparedStatement stmt = conn.prepareStatement(sqlDelPedido)) {
                stmt.setInt(1, idPedido);
                stmt.executeUpdate();
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
            throw e;
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
