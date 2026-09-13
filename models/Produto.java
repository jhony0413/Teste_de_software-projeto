package models;

public class Produto {

    private int idProduto;
    private int idCategoria;
    private String nome;
    private String descricao;
    private double precoUnitario;
    private int estoque;
    private boolean ativo;

    public Produto() {
    }

    public Produto(int idProduto, int idCategoria, String nome, String descricao, double precoUnitario, int estoque, boolean ativo) {
        setIdProduto(idProduto);
        setIdCategoria(idCategoria);
        setNome(nome);
        setDescricao(descricao);
        setPrecoUnitario(precoUnitario);
        setEstoque(estoque);
        setAtivo(ativo);
    }

    public int getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(int idProduto) {
        if (idProduto < 0) {
            throw new IllegalArgumentException("O ID do produto não pode ser negativo.");
        }
        this.idProduto = idProduto;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        if (idCategoria <= 0) {
            throw new IllegalArgumentException("O produto deve estar associado a uma categoria válida.");
        }
        this.idCategoria = idCategoria;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do produto não pode ser vazio.");
        }
        if (nome.length() > 100) {
            throw new IllegalArgumentException("O nome do produto deve ter no máximo 100 caracteres.");
        }
        this.nome = nome.trim();
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        if (descricao != null && descricao.length() > 255) {
            throw new IllegalArgumentException("A descrição do produto deve ter no máximo 255 caracteres.");
        }
        this.descricao = descricao != null ? descricao.trim() : null;
    }

    public double getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(double precoUnitario) {
        if (precoUnitario < 0.0) {
            throw new IllegalArgumentException("O preço unitário não pode ser negativo.");
        }
        this.precoUnitario = precoUnitario;
    }

    public int getEstoque() {
        return estoque;
    }

    public void setEstoque(int estoque) {
        if (estoque < 0) {
            throw new IllegalArgumentException("O estoque não pode ser negativo.");
        }
        this.estoque = estoque;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
}
