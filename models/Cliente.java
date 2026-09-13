package models;

import java.time.LocalDateTime;

public class Cliente extends Pessoa {

    private LocalDateTime dataCadastro;

    public Cliente() {
        super();
        this.dataCadastro = LocalDateTime.now();
    }

    public Cliente(int idPessoa, String nome, String cpf, String email, String telefone, boolean ativo, LocalDateTime dataCadastro) {
        super(idPessoa, nome, cpf, email, telefone, ativo);
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
}
