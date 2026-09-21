package models;

import models.Vendedor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class VendedorTest {

    public VendedorTest() {
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
    public void testSetLogin() {
        Vendedor instance = new Vendedor(10, "João da Silva", "12345678901", "joao@email.com", "61988887777", true, "joao.silva", "hash123456");

        instance.setLogin("paulo.costa");

        assertEquals("paulo.costa", instance.getLogin());
    }

    @Test
    public void testSetLoginVazio() {
        Vendedor instance = new Vendedor(10, "João da Silva", "12345678901", "joao@email.com", "61988887777", true, "joao.silva", "hash123456");

        assertThrows(IllegalArgumentException.class, () -> {
            instance.setLogin("");
        });
    }

    @Test
    public void testSetLoginNULL() {
        Vendedor instance = new Vendedor(10, "João da Silva", "12345678901", "joao@email.com", "61988887777", true, "joao.silva", "hash123456");

        assertThrows(IllegalArgumentException.class, () -> {
            instance.setLogin(null);
        });
    }

    @Test
    public void testSetLoginMaiorQue50() {
        Vendedor instance = new Vendedor(10, "João da Silva", "12345678901", "joao@email.com", "61988887777", true, "joao.silva", "hash123456");

        String loginGrande = "a".repeat(51);

        assertThrows(IllegalArgumentException.class, () -> {
            instance.setLogin(loginGrande);
        });
    }

    @Test
    public void testSetLoginIgualA50() {
        Vendedor instance = new Vendedor(10, "João da Silva", "12345678901", "joao@email.com", "61988887777", true, "joao.silva", "hash123456");

        String login = "a".repeat(50);

        instance.setLogin(login);

        assertEquals(login, instance.getLogin());
    }

    @Test
    public void testSetSenhaHash() {
        Vendedor instance = new Vendedor(10, "João da Silva", "12345678901", "joao@email.com", "61988887777", true, "joao.silva", "hash123456");

        instance.setSenhaHash("hash862526");

        assertEquals("hash862526", instance.getSenhaHash());
    }

    @Test
    public void testSetSenhaHashVazio() {
        Vendedor instance = new Vendedor(10, "João da Silva", "12345678901", "joao@email.com", "61988887777", true, "joao.silva", "hash123456");

        assertThrows(IllegalArgumentException.class, () -> {
            instance.setSenhaHash("");
        });
    }

    @Test
    public void testSetSenhaHashNULL() {
        Vendedor instance = new Vendedor(10, "João da Silva", "12345678901", "joao@email.com", "61988887777", true, "joao.silva", "hash123456");

        assertThrows(IllegalArgumentException.class, () -> {
            instance.setSenhaHash(null);
        });
    }

    @Test
    public void testSetSenhaHashMenorQue6() {
        Vendedor instance = new Vendedor(10, "João da Silva", "12345678901", "joao@email.com", "61988887777", true, "joao.silva", "hash123456");

        String senhaHashPequena = "hash8";

        assertThrows(IllegalArgumentException.class, () -> {
            instance.setSenhaHash(senhaHashPequena);
        });
    }

}
