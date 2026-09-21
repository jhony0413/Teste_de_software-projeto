package models;

import models.Produto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ProdutoTest {

    public ProdutoTest() {
    }

    @BeforeAll
    public static void setUpClass() {
    }

    @AfterAll
    public static void tearDownClass() {
    }

    @BeforeEach
    public void setUp() {
    }

    @AfterEach
    public void tearDown() {
    }

    @Test
    public void testSetNome() {
        Produto instance = new Produto(10, 2, 5, "Tênis Esportivo", "Tênis esportivo masculino para corrida", 199.90, 50, true);

        instance.setNome("Chuteira");

        assertEquals("Chuteira", instance.getNome());
    }

    @Test
    public void testSetNomeVazio() {
        Produto instance = new Produto(10, 2, 5, "Tênis Esportivo", "Tênis esportivo masculino para corrida", 199.90, 50, true);

        assertThrows(IllegalArgumentException.class, () -> {
            instance.setNome("");
        });
    }

    @Test
    public void testSetNomeNULL() {
        Produto instance = new Produto(10, 2, 5, "Tênis Esportivo", "Tênis esportivo masculino para corrida", 199.90, 50, true);

        assertThrows(IllegalArgumentException.class, () -> {
            instance.setNome(null);
        });
    }

    @Test
    public void testSetNomeMaiorQue100() {
        Produto instance = new Produto(10, 2, 5, "Tênis Esportivo", "Tênis esportivo masculino para corrida", 199.90, 50, true);

        String nomeGrande = "a".repeat(101);

        assertThrows(IllegalArgumentException.class, () -> {
            instance.setNome(nomeGrande);
        });
    }

    @Test
    public void testSetNomeIgualA100() {
        Produto instance = new Produto(10, 2, 5, "Tênis Esportivo", "Tênis esportivo masculino para corrida", 199.90, 50, true);

        String nome = "a".repeat(100);

        instance.setNome(nome);

        assertEquals(nome, instance.getNome());
    }

    @Test
    public void testSetNomeEspacosVazios() {
        Produto instance = new Produto(10, 2, 5, "Tênis Esportivo", "Tênis esportivo masculino para corrida", 199.90, 50, true);

        instance.setNome("  Tênis Esportivo  ");

        assertEquals("Tênis Esportivo", instance.getNome());
    }

    @Test
    public void testSetDescricao() {
        Produto instance = new Produto(10, 2, 5, "Tênis Esportivo", "Tênis esportivo masculino para corrida", 199.90, 50, true);

        instance.setDescricao("Chuteira masculina para futebol");

        assertEquals("Chuteira masculina para futebol", instance.getDescricao());
    }

    @Test
    public void testSetDescricaoNULL() {
        Produto instance = new Produto(10, 2, 5, "Tênis Esportivo", "Tênis esportivo masculino para corrida", 199.90, 50, true);

        instance.setDescricao(null);

        assertNull(instance.getDescricao());
    }

    @Test
    public void testSetDescricaoMaiorQue255() {
        Produto instance = new Produto(10, 2, 5, "Tênis Esportivo", "Tênis esportivo masculino para corrida", 199.90, 50, true);

        String descricaoGrande = "a".repeat(256);

        assertThrows(IllegalArgumentException.class, () -> {
            instance.setDescricao(descricaoGrande);
        });
    }

    @Test
    public void testSetDescricaoIgualA255() {
        Produto instance = new Produto(10, 2, 5, "Tênis Esportivo", "Tênis esportivo masculino para corrida", 199.90, 50, true);

        String descricao = "a".repeat(255);

        instance.setDescricao(descricao);

        assertEquals(descricao, instance.getDescricao());
    }

    @Test
    public void testSetPrecoUnitario() {
        Produto instance = new Produto(10, 2, 5, "Tênis Esportivo", "Tênis esportivo masculino para corrida", 199.90, 50, true);

        instance.setPrecoUnitario(126.90);

        assertEquals(126.90, instance.getPrecoUnitario(), 0.001);
    }

    @Test
    public void testSetPrecoUnitarioNegativo() {
        Produto instance = new Produto(10, 2, 5, "Tênis Esportivo", "Tênis esportivo masculino para corrida", 199.90, 50, true);

        assertThrows(IllegalArgumentException.class, () -> {
            instance.setPrecoUnitario(-199.90);
        });
    }

    @Test
    public void testSetEstoque() {
        Produto instance = new Produto(10, 2, 5, "Tênis Esportivo", "Tênis esportivo masculino para corrida", 199.90, 50, true);

        instance.setEstoque(30);

        assertEquals(30, instance.getEstoque());
    }

    @Test
    public void testSetEstoqueNegativo() {
        Produto instance = new Produto(10, 2, 5, "Tênis Esportivo", "Tênis esportivo masculino para corrida", 199.90, 50, true);

        assertThrows(IllegalArgumentException.class, () -> {
            instance.setEstoque(-1);
        });
    }

}
