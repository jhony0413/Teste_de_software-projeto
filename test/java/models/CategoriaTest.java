package models;

import models.Categoria;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CategoriaTest {

    public CategoriaTest() {
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
        Categoria instance = new Categoria(10, 5, "Categoria de calçados", "descricao", 2);

        instance.setNome("Categoria de roupas");

        assertEquals("Categoria de roupas", instance.getNome());
    }

    @Test
    public void testSetNomeVazio() {
        Categoria instance = new Categoria(10, 5, "Categoria de calçados", "descricao", 2);

        assertThrows(IllegalArgumentException.class, () -> {
            instance.setNome("");
        });
    }

    @Test
    public void testSetNomeNULL() {
        Categoria instance = new Categoria(10, 5, "Categoria de calçados", "descricao", 2);

        assertThrows(IllegalArgumentException.class, () -> {
            instance.setNome(null);
        });
    }

    @Test
    public void testSetNomeMaiorQue100() {
        Categoria instance = new Categoria(10, 5, "Categoria de calçados", "descricao", 2);

        String nomeGrande = "a".repeat(101);

        assertThrows(IllegalArgumentException.class, () -> {
            instance.setNome(nomeGrande);
        });
    }

    @Test
    public void testSetNomeIgualA100() {
        Categoria instance = new Categoria(10, 5, "Categoria de calçados", "descricao", 2);

        String nome = "a".repeat(100);

        instance.setNome(nome);

        assertEquals(nome, instance.getNome());
    }

    @Test
    public void testSetNomeEspacosVazios() {
        Categoria instance = new Categoria(10, 5, "Categoria de calçados", "descricao", 2);

        instance.setNome("  Calçados  ");

        assertEquals("Calçados", instance.getNome());
    }

    @Test
    public void testSetDescricao() {
        Categoria instance = new Categoria(10, 5, "Categoria de calçados", "descricao", 2);

        instance.setDescricao("Calçados");

        assertEquals("Calçados", instance.getDescricao());
    }

    @Test
    public void testSetDescricaoNULL() {
        Categoria instance = new Categoria(10, 5, "Categoria de calçados", "descricao", 2);

        instance.setDescricao(null);

        assertNull(instance.getDescricao());
    }

    @Test
    public void testSetDescricaoMaiorQue255() {
        Categoria instance = new Categoria(10, 5, "Categoria de calçados", "descricao", 2);

        String descricaoGrande = "a".repeat(256);

        assertThrows(IllegalArgumentException.class, () -> {
            instance.setDescricao(descricaoGrande);
        });
    }

    @Test
    public void testSetDescricaoIgualA255() {
        Categoria instance = new Categoria(10, 5, "Categoria de calçados", "descricao", 2);

        String descricao = "a".repeat(255);

        instance.setDescricao(descricao);

        assertEquals(descricao, instance.getDescricao());
    }

    @Test
    public void testSetIdCategoriaPai() {
        Categoria instance = new Categoria(10, 5, "Categoria de calçados", "descricao", 2);

        instance.setIdCategoriaPai(4);

        assertEquals(4, instance.getIdCategoriaPai());
    }

    @Test
    public void testSetIdCategoriaPaiNULL() {
        Categoria instance = new Categoria(10, 5, "Categoria de calçados", "descricao", 2);

        instance.setIdCategoriaPai(null);

        assertNull(instance.getIdCategoriaPai());
    }

    @Test
    public void testSetIdCategoriaPaiNegativo() {
        Categoria instance = new Categoria(10, 5, "Categoria de calçados", "descricao", 2);

        assertThrows(IllegalArgumentException.class, () -> {
            instance.setIdCategoriaPai(-1);
        });
    }

}
