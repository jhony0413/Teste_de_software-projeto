package models;

import java.time.LocalDateTime;

public class Cliente extends Pessoa {

    private LocalDateTime dataCadastro;
    private int idVendedor;

    public Cliente() {
        super();
        this.dataCadastro = LocalDateTime.now();
    }

    public Cliente(int idPessoa, int idVendedor, String nome, String cpf, String email, String telefone, boolean ativo, LocalDateTime dataCadastro) {
        super(idPessoa, nome, cpf, email, telefone, ativo);
        setIdVendedor(idVendedor);
        setDataCadastro(dataCadastro);
    }

    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDateTime dataCadastro) {
        if (dataCadastro != null && dataCadastro.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("A data de cadastro não pode ser no futuro.");
        }
        this.dataCadastro = dataCadastro != null ? dataCadastro : LocalDateTime.now();
    }

    public int getIdVendedor() {
        return idVendedor;
    }

    public void setIdVendedor(int idVendedor) {
        if (idVendedor < 0) {
            throw new IllegalArgumentException("O ID do vendedor deve ser maior que zero.");
        }
        this.idVendedor = idVendedor;
    }
}
