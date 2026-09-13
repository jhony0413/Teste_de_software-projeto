package models;

public class Pessoa {

    private int idPessoa;
    private String nome;
    private String cpf;
    private String email;
    private String telefone;
    private boolean ativo;

    public Pessoa() {
    }

    public Pessoa(int idPessoa, String nome, String cpf, String email, String telefone, boolean ativo) {
        setIdPessoa(idPessoa);
        setNome(nome);
        setCpf(cpf);
        setEmail(email);
        setTelefone(telefone);
        setAtivo(ativo);
    }

    public int getIdPessoa() {
        return idPessoa;
    }

    public void setIdPessoa(int idPessoa) {
        if (idPessoa < 0) {
            throw new IllegalArgumentException("O ID da pessoa não pode ser negativo.");
        }
        this.idPessoa = idPessoa;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome não pode ser nulo ou vazio.");
        }
        if (nome.length() > 100) {
            throw new IllegalArgumentException("O nome deve ter no máximo 100 caracteres.");
        }
        this.nome = nome.trim();
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        if (cpf == null) {
            throw new IllegalArgumentException("O CPF não pode ser nulo.");
        }
        String cpfLimpo = cpf.replaceAll("\\D", "");
        if (cpfLimpo.length() != 11) {
            throw new IllegalArgumentException("O CPF deve conter exatamente 11 dígitos.");
        }
        this.cpf = cpfLimpo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("O e-mail não pode ser nulo ou vazio.");
        }
        if (!email.contains("@") || !email.contains(".")) {
            throw new IllegalArgumentException("Formato de e-mail inválido.");
        }
        if (email.length() > 120) {
            throw new IllegalArgumentException("O e-mail deve ter no máximo 120 caracteres.");
        }
        this.email = email.trim();
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        if (telefone != null && telefone.length() > 20) {
            throw new IllegalArgumentException("O telefone deve ter no máximo 20 caracteres.");
        }
        this.telefone = telefone != null ? telefone.trim() : null;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
}
