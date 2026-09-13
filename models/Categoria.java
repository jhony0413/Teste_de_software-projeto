package models;

public class Categoria {

    private int idCategoria;
    private String nome;
    private String descricao;
    private Integer idCategoriaPai;

    public Categoria() {
    }

    public Categoria(int idCategoria, String nome, String descricao, Integer idCategoriaPai) {
        setIdCategoria(idCategoria);
        setNome(nome);
        setDescricao(descricao);
        setIdCategoriaPai(idCategoriaPai);
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        if (idCategoria < 0) {
            throw new IllegalArgumentException("O ID da categoria não pode ser negativo.");
        }
        this.idCategoria = idCategoria;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome da categoria não pode ser vazio.");
        }
        if (nome.length() > 100) {
            throw new IllegalArgumentException("O nome da categoria deve ter no máximo 100 caracteres.");
        }
        this.nome = nome.trim();
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        if (descricao != null && descricao.length() > 255) {
            throw new IllegalArgumentException("A descrição deve ter no máximo 255 caracteres.");
        }
        this.descricao = descricao != null ? descricao.trim() : null;
    }

    public Integer getIdCategoriaPai() {
        return idCategoriaPai;
    }

    public void setIdCategoriaPai(Integer idCategoriaPai) {
        if (idCategoriaPai != null && idCategoriaPai < 0) {
            throw new IllegalArgumentException("O ID da categoria pai não pode ser negativo.");
        }
        this.idCategoriaPai = idCategoriaPai;
    }
}
