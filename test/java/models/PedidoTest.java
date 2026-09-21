package models;

import java.time.LocalDateTime;
import models.Pedido;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PedidoTest {

    public PedidoTest() {
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
    public void testSetDataEmissao() {
        Pedido instance = new Pedido(25, 10, 3, LocalDateTime.of(2027, 9, 20, 14, 30), 179.80);

        instance.setDataEmissao(LocalDateTime.of(2026, 4, 15, 14, 30));

        assertEquals(LocalDateTime.of(2026, 4, 15, 14, 30), instance.getDataEmissao());
    }

    @Test
    public void testSetDataEmissaoNULL() {
        Pedido instance = new Pedido(25, 10, 3, LocalDateTime.of(2026, 9, 20, 14, 30), 179.80);

        instance.setDataEmissao(null);

        assertNotNull(instance.getDataEmissao());
    }

    @Test
    public void testSetDataEmissaoFuturo() {
        Pedido instance = new Pedido(25, 10, 3, LocalDateTime.of(2026, 9, 20, 14, 30), 179.80);

        LocalDateTime dataFutura = LocalDateTime.of(2027, 2, 18, 14, 30);

        assertThrows(IllegalArgumentException.class, () -> {
            instance.setDataEmissao(dataFutura);
        });
    }

    @Test
    public void testSetValorTotal() {
        Pedido instance = new Pedido(25, 10, 3, LocalDateTime.of(2026, 9, 20, 14, 30), 179.80);

        instance.setValorTotal(49.90);

        assertEquals(49.90, instance.getValorTotal(), 0.001);
    }

    @Test
    public void testSetValorTotalNegativo() {
        Pedido instance = new Pedido(25, 10, 3, LocalDateTime.of(2026, 9, 20, 14, 30), 179.80);

        assertThrows(IllegalArgumentException.class, () -> {
            instance.setValorTotal(-179.80);
        });
    }

}
