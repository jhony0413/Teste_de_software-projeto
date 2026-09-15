package models;

public abstract class Pessoa {

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
        /*
        if (!isCpfValido(cpfLimpo)) {
            throw new IllegalArgumentException("O CPF informado é inválido.");
        }
         */
        this.cpf = cpfLimpo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("O e-mail não pode ser nulo ou vazio.");
        }

        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
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
        if (telefone != null && !telefone.trim().isEmpty()) {

            String telefoneLimpo = telefone.replaceAll("\\D", "");
            if (telefoneLimpo.length() < 10 || telefoneLimpo.length() > 11) {
                throw new IllegalArgumentException("O telefone deve ter 10 ou 11 dígitos (incluindo DDD).");
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

    /*
    private boolean isCpfValido(String cpf) {

        if (cpf.equals("00000000000") || cpf.equals("11111111111")
                || cpf.equals("22222222222") || cpf.equals("33333333333")
                || cpf.equals("44444444444") || cpf.equals("55555555555")
                || cpf.equals("66666666666") || cpf.equals("77777777777")
                || cpf.equals("88888888888") || cpf.equals("99999999999")) {
            return false;
        }

        try {

            int soma = 0;
            int peso = 10;
            for (int i = 0; i < 9; i++) {
                int num = (int) (cpf.charAt(i) - 48);
                soma = soma + (num * peso);
                peso--;
            }
            int digito10Calculado = calcularDigitoVerificador(cpf, 9, 10);
            int digito11Calculado = calcularDigitoVerificador(cpf, 10, 11);

            int digito10Real = Character.getNumericValue(cpf.charAt(9));
            int digito11Real = Character.getNumericValue(cpf.charAt(10));

            return digito10Calculado == digito10Real && digito11Calculado == digito11Real;
        } catch (Exception e) {
            return false;
        }
    }

    private int calcularDigitoVerificador(String cpf, int quantidadeDigitos, int pesoInicial) {
        int soma = 0;
        int peso = pesoInicial;

        for (int i = 0; i < quantidadeDigitos; i++) {
            int numero = Character.getNumericValue(cpf.charAt(i));
            soma += numero * peso;
            peso--;
        }

        int resto = 11 - (soma % 11);
        return (resto == 10 || resto == 11) ? 0 : resto;
    }
     */
}
