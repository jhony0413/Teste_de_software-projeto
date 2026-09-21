package models;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FornecedorProdutoTest {

    public FornecedorProdutoTest() {
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
    public void testSetPrecoCusto() {
        FornecedorProduto instance = new FornecedorProduto(10, 5, 1, 89.90, 7, 50);

        instance.setPrecoCusto(56.90);

        assertEquals(56.90, instance.getPrecoCusto(), 0.001);
    }

    @Test
    public void testSetPrecoCustoNegativo() {
        FornecedorProduto instance = new FornecedorProduto(10, 5, 1, 89.90, 7, 50);

        assertThrows(IllegalArgumentException.class, () -> {
            instance.setPrecoCusto(-89.90);
        });
    }

    @Test
    public void testSetPrazoEntregaDias() {
        FornecedorProduto instance = new FornecedorProduto(10, 5, 1, 89.90, 7, 50);

        instance.setPrazoEntregaDias(5);

        assertEquals(5, instance.getPrazoEntregaDias());
    }

    @Test
    public void testSetPrazoEntregaDiasIgualA0() {
        FornecedorProduto instance = new FornecedorProduto(10, 5, 1, 89.90, 7, 50);

        assertThrows(IllegalArgumentException.class, () -> {
            instance.setPrazoEntregaDias(0);
        });
    }

    @Test
    public void testSetQuantidade() {
        FornecedorProduto instance = new FornecedorProduto(10, 5, 1, 89.90, 7, 50);

        instance.setQuantidade(25);

        assertEquals(25, instance.getQuantidade());
    }

    @Test
    public void testSetQuantidadeIgualA0() {
        FornecedorProduto instance = new FornecedorProduto(10, 5, 1, 89.90, 7, 50);

        assertThrows(IllegalArgumentException.class, () -> {
            instance.setQuantidade(0);
        });
    }

}
