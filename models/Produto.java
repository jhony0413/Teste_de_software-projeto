package models;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Produto {

    // Formatadores estáticos e thread-safe para evitar alocações desnecessárias
    private static final DateTimeFormatter formatadorData = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DecimalFormat formatadorDecimal = new DecimalFormat("R$ #,##0.00", new DecimalFormatSymbols(new Locale("pt", "BR")));

    private int idProduto;
    private String nomeProduto;
    private double precoUnitario;
    private String categoria;
    private LocalDate dataValidade;
    private int quantidade;

    /* Construtores */
    public Produto() {
    }

    public Produto(int idProduto, String nomeProduto, double precoUnitario, String categoria, LocalDate dataValidade, int quantidade) {
        setIdProduto(idProduto);
        setNomeProduto(nomeProduto);
        setPrecoUnitario(precoUnitario);
        setCategoria(categoria);
        setDataValidade(dataValidade);
        setQuantidade(quantidade);
    }

    /* Getters e Setters */
    public int getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(int idProduto) {
        if (idProduto <= 0) {
            throw new IllegalArgumentException("ID do produto deve ser maior que zero.");
        }
        this.idProduto = idProduto;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        if (nomeProduto == null || nomeProduto.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do produto não pode ser vazio.");
        }
        this.nomeProduto = nomeProduto.trim();
    }

    public double getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(double precoUnitario) {
        if (precoUnitario < 0) {
            throw new IllegalArgumentException("Preço inválido.");
        }
        this.precoUnitario = precoUnitario;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria != null ? categoria.trim() : "";
    }

    public LocalDate getDataValidade() {
        return dataValidade;
    }

    public void setDataValidade(LocalDate dataValidade) {
        this.dataValidade = dataValidade;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        if (quantidade < 0) {
            throw new IllegalArgumentException("Quantidade inválida.");
        }
        this.quantidade = quantidade;
    }

    /* Métodos de Domínio */
    public boolean possuiEstoque() {
        return quantidade > 0;
    }

    public void reduzirEstoque(int valorSubtrair) {
        if (valorSubtrair <= 0) {
            throw new IllegalArgumentException("A quantidade a ser reduzida deve ser maior do que zero.");
        }
        if (this.quantidade < valorSubtrair) {
            throw new IllegalArgumentException("Estoque insuficiente.");
        }
        this.quantidade -= valorSubtrair;
    }

    public void adicionarEstoque(int valorSomar) {
        if (valorSomar <= 0) {
            throw new IllegalArgumentException("A quantidade a ser adicionada deve ser maior do que zero.");
        }
        this.quantidade += valorSomar;
    }

    /* Sobrescritas de Objetos Java */
    @Override
    public String toString() {
        String validade = dataValidade != null ? formatadorData.format(dataValidade) : "Não informada";

        // Sincronização necessária pois DecimalFormat não é thread-safe por padrão
        String precoFormatado;
        synchronized (formatadorDecimal) {
            precoFormatado = formatadorDecimal.format(precoUnitario);
        }

        return """
               
                ID Produto: %d
                Nome: %s
                Categoria: %s
                Preço: %s
                Quantidade: %d
                Validade: %s
                -------------------------------
                """.formatted(
                idProduto,
                nomeProduto,
                categoria,
                precoFormatado,
                quantidade,
                validade);
    }

}
