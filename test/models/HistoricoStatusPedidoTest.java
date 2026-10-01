package models;

import java.time.LocalDateTime;
import java.time.LocalTime;
import models.HistoricoStatusPedido;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class HistoricoStatusPedidoTest {

    public HistoricoStatusPedidoTest() {
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
    public void testSetDataAlteracao() {
        HistoricoStatusPedido instance = new HistoricoStatusPedido(10, LocalDateTime.of(2026, 9, 20, 14, 30), "PENDENTE");

        instance.setDataAlteracao(LocalDateTime.of(2026, 5, 12, 14, 30));

        assertEquals(LocalDateTime.of(2026, 5, 12, 14, 30), instance.getDataAlteracao());
    }

    @Test
    public void testSetDataAlteracaoNULL() {
        HistoricoStatusPedido instance = new HistoricoStatusPedido(10, LocalDateTime.of(2026, 9, 20, 14, 30), "PENDENTE");

        instance.setDataAlteracao(null);

        assertNotNull(instance.getDataAlteracao());
    }

    @Test
    public void testSetDataAlteracaoFuturo() {
        HistoricoStatusPedido instance = new HistoricoStatusPedido(10, LocalDateTime.of(2026, 9, 20, 14, 30), "PENDENTE");

        LocalDateTime dataFutura = LocalDateTime.of(2027, 9, 20, 14, 30);

        assertThrows(IllegalArgumentException.class, () -> {
            instance.setDataAlteracao(dataFutura);
        });
    }

    @Test
    public void testSetStatus() {
        HistoricoStatusPedido instance = new HistoricoStatusPedido(10, LocalDateTime.of(2026, 9, 20, 14, 30), "PENDENTE");

        instance.setStatus("FINALIZADO");

        assertEquals("FINALIZADO", instance.getStatus());
    }

    @Test
    public void testSetStatusNULL() {
        HistoricoStatusPedido instance = new HistoricoStatusPedido(10, LocalDateTime.of(2026, 9, 20, 14, 30), "PENDENTE");

        assertThrows(IllegalArgumentException.class, () -> {
            instance.setStatus(null);
        });
    }

    @Test
    public void testSetStatusVazio() {
        HistoricoStatusPedido instance = new HistoricoStatusPedido(10, LocalDateTime.of(2026, 9, 20, 14, 30), "PENDENTE");

        assertThrows(IllegalArgumentException.class, () -> {
            instance.setStatus("");
        });
    }

    @Test
    public void testSetStatusMinusculo() {
        HistoricoStatusPedido instance = new HistoricoStatusPedido(10, LocalDateTime.of(2026, 9, 20, 14, 30), "PENDENTE");

        instance.setStatus("pago");

        assertEquals("PAGO", instance.getStatus());
    }

    @Test
    public void testSetStatusInvalido() {
        HistoricoStatusPedido instance = new HistoricoStatusPedido(10, LocalDateTime.of(2026, 9, 20, 14, 30), "PENDENTE");

        assertThrows(IllegalArgumentException.class, () -> {
            instance.setStatus("EM_PROCESSAMENTO");
        });
    }

}
