package models;

import java.time.LocalDateTime;
import models.Cliente;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ClienteTest {

    public ClienteTest() {
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
    public void testSetDataCadastro() {
        Cliente instance = new Cliente(10, 2, "João", "12345678900", "joao@email.com", "61999999999", true, LocalDateTime.now());

        LocalDateTime data = LocalDateTime.of(2026, 9, 20, 14, 30);

        instance.setDataCadastro(data);

        assertEquals(LocalDateTime.of(2026, 9, 20, 14, 30), instance.getDataCadastro());
    }

    @Test
    public void testSetDataCadastroNULL() {
        Cliente instance = new Cliente(10, 2, "João", "12345678900", "joao@email.com", "61999999999", true, LocalDateTime.now());

        instance.setDataCadastro(null);

        assertNotNull(instance.getDataCadastro());
    }

    @Test
    public void testSetDataCadastroFuturo() {
        Cliente instance = new Cliente(10, 2, "João", "12345678900", "joao@email.com", "61999999999", true, LocalDateTime.now());

        LocalDateTime dataFutura = LocalDateTime.now().plusDays(1);

        assertThrows(IllegalArgumentException.class, () -> {
            instance.setDataCadastro(dataFutura);
        });
    }

}
