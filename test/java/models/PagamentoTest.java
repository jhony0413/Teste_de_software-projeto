package models;

import java.time.LocalDateTime;
import models.Pagamento;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PagamentoTest {

    public PagamentoTest() {
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
    public void testSetFormaPagamento() {
        Pagamento instance = new Pagamento(10, 25, "PIX", LocalDateTime.of(2026, 5, 20, 15, 30), 179.80, "APROVADO");

        instance.setFormaPagamento("DINHEIRO");

        assertEquals("DINHEIRO", instance.getFormaPagamento());
    }

    @Test
    public void testSetFormaPagamentoNULL() {
        Pagamento instance = new Pagamento(10, 25, "PIX", LocalDateTime.of(2026, 5, 20, 15, 30), 179.80, "APROVADO");

        assertThrows(IllegalArgumentException.class, () -> {
            instance.setFormaPagamento(null);
        });
    }

    @Test
    public void testSetFormaPagamentoVazio() {
        Pagamento instance = new Pagamento(10, 25, "PIX", LocalDateTime.of(2026, 5, 20, 15, 30), 179.80, "APROVADO");

        assertThrows(IllegalArgumentException.class, () -> {
            instance.setFormaPagamento("");
        });
    }

    @Test
    public void testSetFormaPagamentoMinusculo() {
        Pagamento instance = new Pagamento(10, 25, "PIX", LocalDateTime.of(2026, 5, 20, 15, 30), 179.80, "APROVADO");

        instance.setFormaPagamento("dinheiro");

        assertEquals("DINHEIRO", instance.getFormaPagamento());
    }

    @Test
    public void testSetFormaPagamentoInvalido() {
        Pagamento instance = new Pagamento(10, 25, "PIX", LocalDateTime.of(2026, 5, 20, 15, 30), 179.80, "APROVADO");

        assertThrows(IllegalArgumentException.class, () -> {
            instance.setFormaPagamento("VALE ALIMENTACAO");
        });
    }

    @Test
    public void testSetDataHora() {
        Pagamento instance = new Pagamento(10, 25, "PIX", LocalDateTime.of(2026, 5, 20, 15, 30), 179.80, "APROVADO");

        instance.setDataHora(LocalDateTime.of(2026, 5, 11, 14, 30));

        assertEquals(LocalDateTime.of(2026, 5, 11, 14, 30), instance.getDataHora());
    }

    @Test
    public void testSetDataHoraNULL() {
        Pagamento instance = new Pagamento(10, 25, "PIX", LocalDateTime.of(2026, 5, 20, 15, 30), 179.80, "APROVADO");

        instance.setDataHora(null);

        assertNotNull(instance.getDataHora());
    }

    @Test
    public void testSetDataHoraFuturo() {
        Pagamento instance = new Pagamento(10, 25, "PIX", LocalDateTime.of(2026, 5, 20, 15, 30), 179.80, "APROVADO");

        LocalDateTime dataFutura = LocalDateTime.of(2027, 3, 24, 14, 30);

        assertThrows(IllegalArgumentException.class, () -> {
            instance.setDataHora(dataFutura);
        });
    }

    @Test
    public void testSetValor() {
        Pagamento instance = new Pagamento(10, 25, "PIX", LocalDateTime.of(2026, 5, 20, 15, 30), 179.80, "APROVADO");

        instance.setValor(144.90);

        assertEquals(144.90, instance.getValor());
    }

    @Test
    public void testSetValorIgualA0() {
        Pagamento instance = new Pagamento(10, 25, "PIX", LocalDateTime.of(2026, 5, 20, 15, 30), 179.80, "APROVADO");

        assertThrows(IllegalArgumentException.class, () -> {
            instance.setValor(0);
        });
    }

    @Test
    public void testSetStatus() {
        Pagamento instance = new Pagamento(10, 25, "PIX", LocalDateTime.of(2026, 5, 20, 15, 30), 179.80, "PROCESSANDO");

        instance.setStatus("APROVADO");

        assertEquals("APROVADO", instance.getStatus());
    }

    @Test
    public void testSetStatusNULL() {
        Pagamento instance = new Pagamento(10, 25, "PIX", LocalDateTime.of(2026, 5, 20, 15, 30), 179.80, "PROCESSANDO");

        assertThrows(IllegalArgumentException.class, () -> {
            instance.setStatus(null);
        });
    }

    @Test
    public void testSetStatusVazio() {
        Pagamento instance = new Pagamento(10, 25, "PIX", LocalDateTime.of(2026, 5, 20, 15, 30), 179.80, "PROCESSANDO");

        assertThrows(IllegalArgumentException.class, () -> {
            instance.setStatus("");
        });
    }

    @Test
    public void testSetStatusMinusculo() {
        Pagamento instance = new Pagamento(10, 25, "PIX", LocalDateTime.of(2026, 5, 20, 15, 30), 179.80, "PROCESSANDO");

        instance.setStatus("aprovado");

        assertEquals("APROVADO", instance.getStatus());
    }

    @Test
    public void testSetStatusInvalido() {
        Pagamento instance = new Pagamento(10, 25, "PIX", LocalDateTime.of(2026, 5, 20, 15, 30), 179.80, "PROCESSANDO");

        assertThrows(IllegalArgumentException.class, () -> {
            instance.setStatus("EM_ANDAMENTO");
        });
    }

}
