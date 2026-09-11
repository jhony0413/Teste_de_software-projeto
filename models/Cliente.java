package models;

public class Cliente {

    private int idCliente;
    private String nomeCliente;
    private String cpf;
    private boolean ativo;

    /* Construtores */
    public Cliente() {
        this.ativo = true;
    }

    public Cliente(int idCliente, String nomeCliente, String cpf) {
        setIdCliente(idCliente);
        setNomeCliente(nomeCliente);
        setCpf(cpf);
        this.ativo = true;
    }

    /* Getters e Setters */
    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        if (idCliente <= 0) {
            throw new IllegalArgumentException("ID do cliente deve ser maior que zero.");
        }
        this.idCliente = idCliente;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        if (nomeCliente == null || nomeCliente.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do cliente não pode ser vazio.");
        }
        this.nomeCliente = nomeCliente.trim();
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        if (cpf == null || cpf.trim().isEmpty()) {
            throw new IllegalArgumentException("CPF é um campo obrigatório.");
        }
        String cpfLimpo = cpf.replaceAll("\\D", "");
        if (cpfLimpo.length() != 11) {
            throw new IllegalArgumentException("CPF deve conter exatamente 11 dígitos numéricos.");
        }
        this.cpf = cpfLimpo;
    }

    public boolean isAtivo() {
        return ativo;
    }

    /* Métodos de Domínio */
    public void desativar() {
        this.ativo = false;
    }

    public void ativar() {
        this.ativo = true;
    }

    /* Formatação auxiliar */
    private String getCpfFormatado() {
        if (cpf != null && cpf.length() == 11) {
            return cpf.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
        }
        return cpf;
    }

    /* Sobrescritas de Objetos Java */
    @Override
    public String toString() {
        String status = ativo ? "ATIVO" : "INATIVO";
        return """
               
                ID Cliente: %d
                Nome: %s
                CPF: %s
                Status: %s
                -------------------------------
                """.formatted(
                idCliente,
                nomeCliente,
                getCpfFormatado(),
                status);
    }
}
