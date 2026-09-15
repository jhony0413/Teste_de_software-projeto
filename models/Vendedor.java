package models;

public class Vendedor extends Pessoa {

    private String login;
    private String senhaHash;

    public Vendedor() {
        super();
    }

    public Vendedor(int idPessoa, String nome, String cpf, String email, String telefone, boolean ativo, String login, String senhaHash) {
        super(idPessoa, nome, cpf, email, telefone, ativo);
        setLogin(login);
        setSenhaHash(senhaHash);
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        if (login == null || login.trim().isEmpty()) {
            throw new IllegalArgumentException("O login não pode ser nulo ou vazio.");
        }
        if (login.length() > 50) {
            throw new IllegalArgumentException("O login deve ter no máximo 50 caracteres.");
        }
        this.login = login.trim();
    }

    public String getSenhaHash() {
        return senhaHash;
    }

    public void setSenhaHash(String senhaHash) {
        if (senhaHash == null || senhaHash.trim().isEmpty()) {
            throw new IllegalArgumentException("A senha hash não pode ser nula ou vazia.");
        }
        if (senhaHash.length() < 6) {
            throw new IllegalArgumentException("A senha parece ser inválida ou insegura (mínimo 6 caracteres esperados).");
        }
        this.senhaHash = senhaHash.trim();
    }
}
