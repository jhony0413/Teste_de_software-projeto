package models;

public class Fornecedor {

    private int idFornecedor;
    private String cnpj;
    private String nome;
    private String email;
    private String telefone;
    private boolean ativo;
    private int idVendedor;

    public Fornecedor() {
    }

    public Fornecedor(int idFornecedor, int idVendedor, String cnpj, String nome, String email, String telefone, boolean ativo) {
        setIdFornecedor(idFornecedor);
        setIdVendedor(idVendedor);
        setCnpj(cnpj);
        setNome(nome);
        setEmail(email);
        setTelefone(telefone);
        setAtivo(ativo);
    }

    public int getIdFornecedor() {
        return idFornecedor;
    }

    public void setIdFornecedor(int idFornecedor) {
        if (idFornecedor < 0) {
            throw new IllegalArgumentException("O ID do fornecedor não pode ser negativo.");
        }
        this.idFornecedor = idFornecedor;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        if (cnpj == null) {
            throw new IllegalArgumentException("O CNPJ não pode ser nulo.");
        }
        String cnpjLimpo = cnpj.replaceAll("\\D", "");
        if (cnpjLimpo.length() != 14) {
            throw new IllegalArgumentException("O CNPJ deve conter exatamente 14 dígitos.");
        }
        this.cnpj = cnpjLimpo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do fornecedor não pode ser vazio.");
        }
        if (nome.length() > 100) {
            throw new IllegalArgumentException("O nome deve ter no máximo 100 caracteres.");
        }
        this.nome = nome.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email == null || !email.trim().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("E-mail do fornecedor inválido.");
        }
        this.email = email.trim();
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        if (telefone != null && !telefone.trim().isEmpty()) {
            String telefoneLimpo = telefone.replaceAll("\\D", "");
            if (telefoneLimpo.length() < 10 || telefoneLimpo.length() > 11) {
                throw new IllegalArgumentException("O telefone deve ter 10 ou 11 dígitos.");
            }
            this.telefone = telefoneLimpo;
        } else {
            this.telefone = null;
        }
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
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
