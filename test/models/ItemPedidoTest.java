package models;

import models.ItemPedido;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ItemPedidoTest {

    public ItemPedidoTest() {
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
    public void testSetQuantidade() {
        ItemPedido instance = new ItemPedido(10, 5, 2, 89.90, 179.80);

        instance.setQuantidade(1);

        assertEquals(1, instance.getQuantidade());
        assertEquals(89.90, instance.getSubtotal(), 0.001);
    }

    @Test
    public void testSetQuantidadeIgualA0() {
        ItemPedido instance = new ItemPedido(10, 5, 2, 89.90, 179.80);

        assertThrows(IllegalArgumentException.class, () -> {
            instance.setQuantidade(0);
        });
    }

    @Test
    public void testSetPrecoUnitario() {
        ItemPedido instance = new ItemPedido(10, 5, 2, 89.90, 179.80);

        instance.setPrecoUnitario(50.90);

        assertEquals(50.90, instance.getPrecoUnitario(), 0.001);
        assertEquals(101.80, instance.getSubtotal(), 0.001);
    }

    @Test
    public void testSetPrecoUnitarioNegativo() {
        ItemPedido instance = new ItemPedido(10, 5, 2, 89.90, 179.80);

        assertThrows(IllegalArgumentException.class, () -> {
            instance.setPrecoUnitario(-89.90);
        });
    }

    @Test
    public void testAtualizarSubtotal() {
        ItemPedido instance = new ItemPedido(10, 5, 2, 89.90, 179.80);

        assertEquals(179.80, instance.getSubtotal(), 0.001);
    }

}
