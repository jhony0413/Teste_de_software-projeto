package models;

import models.Fornecedor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FornecedorTest {

    public FornecedorTest() {
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
    public void testSetCnpj() {
        Fornecedor instance = new Fornecedor(10, 2, "12345678000195", "Calçados Brasil LTDA", "contato@calcadosbrasil.com", "61988887777", true);

        instance.setCnpj("12345678000191");

        assertEquals("12345678000191", instance.getCnpj());
    }

    @Test
    public void testSetCnpjNULL() {
        Fornecedor instance = new Fornecedor(10, 2, "12345678000195", "Calçados Brasil LTDA", "contato@calcadosbrasil.com", "61988887777", true);

        assertThrows(IllegalArgumentException.class, () -> {
            instance.setCnpj(null);
        });
    }

    @Test
    public void testSetCnpjDiferenteDe14() {
        Fornecedor instance = new Fornecedor(10, 2, "12345678000195", "Calçados Brasil LTDA", "contato@calcadosbrasil.com", "61988887777", true);

        assertThrows(IllegalArgumentException.class, () -> {
            instance.setCnpj("1234567890123");
        });
    }

    @Test
    public void testSetCnpjFormatado() {
        Fornecedor instance = new Fornecedor(10, 2, "12345678000195", "Calçados Brasil LTDA", "contato@calcadosbrasil.com", "61988887777", true);

        instance.setCnpj("12.345.678/9012-34");

        assertEquals("12345678901234", instance.getCnpj());
    }

    @Test
    public void testSetNome() {
        Fornecedor instance = new Fornecedor(10, 2, "12345678000195", "Calçados Brasil LTDA", "contato@calcadosbrasil.com", "61988887777", true);

        instance.setNome("Calçados");

        assertEquals("Calçados", instance.getNome());
    }

    @Test
    public void testSetNomeNULL() {
        Fornecedor instance = new Fornecedor(10, 2, "12345678000195", "Calçados Brasil LTDA", "contato@calcadosbrasil.com", "61988887777", true);

        assertThrows(IllegalArgumentException.class, () -> {
            instance.setNome(null);
        });
    }

    @Test
    public void testSetNomeMaiorQue100() {
        Fornecedor instance = new Fornecedor(10, 2, "12345678000195", "Calçados Brasil LTDA", "contato@calcadosbrasil.com", "61988887777", true);

        String nomeGrande = "a".repeat(101);

        assertThrows(IllegalArgumentException.class, () -> {
            instance.setNome(nomeGrande);
        });
    }

    @Test
    public void testSetNomeIgualA100() {
        Fornecedor instance = new Fornecedor(10, 2, "12345678000195", "Calçados Brasil LTDA", "contato@calcadosbrasil.com", "61988887777", true);

        String nome = "a".repeat(100);

        instance.setNome(nome);

        assertEquals(nome, instance.getNome());
    }

    @Test
    public void testSetNomeEspacosVazios() {
        Fornecedor instance = new Fornecedor(10, 2, "12345678000195", "Calçados Brasil LTDA", "contato@calcadosbrasil.com", "61988887777", true);

        instance.setNome("  Calçados  ");

        assertEquals("Calçados", instance.getNome());
    }

    @Test
    public void testSetEmail() {
        Fornecedor instance = new Fornecedor(10, 2, "12345678000195", "Calçados Brasil LTDA", "contato@calcadosbrasil.com", "61988887777", true);

        instance.setEmail("teste@email.com");

        assertEquals("teste@email.com", instance.getEmail());
    }

    @Test
    public void testSetEmailNULL() {
        Fornecedor instance = new Fornecedor(10, 2, "12345678000195", "Calçados Brasil LTDA", "contato@calcadosbrasil.com", "61988887777", true);

        assertThrows(IllegalArgumentException.class, () -> {
            instance.setEmail(null);
        });
    }

    @Test
    public void testSetEmailInvalido() {
        Fornecedor instance = new Fornecedor(10, 2, "12345678000195", "Calçados Brasil LTDA", "contato@calcadosbrasil.com", "61988887777", true);

        assertThrows(IllegalArgumentException.class, () -> {
            instance.setEmail("emailinvalido.com");
        });
    }

    @Test
    public void testSetEmailRemoveEspacos() {
        Fornecedor instance = new Fornecedor(10, 2, "12345678000195", "Calçados Brasil LTDA", "contato@calcadosbrasil.com", "61988887777", true);

        instance.setEmail("  teste@email.com  ");

        assertEquals("teste@email.com", instance.getEmail());
    }

    @Test
    public void testSetTelefone() {
        Fornecedor instance = new Fornecedor(10, 2, "12345678000195", "Calçados Brasil LTDA", "contato@calcadosbrasil.com", "61988887777", true);

        instance.setTelefone("61988865437");

        assertEquals("61988865437", instance.getTelefone());
    }

    @Test
    public void testSetTelefoneNULL() {
        Fornecedor instance = new Fornecedor(10, 2, "12345678000195", "Calçados Brasil LTDA", "contato@calcadosbrasil.com", "61988887777", true);

        instance.setTelefone(null);

        assertNull(instance.getTelefone());
    }

    @Test
    public void testSetTelefoneVazio() {
        Fornecedor instance = new Fornecedor(10, 2, "12345678000195", "Calçados Brasil LTDA", "contato@calcadosbrasil.com", "61988887777", true);

        instance.setTelefone("");

        assertNull(instance.getTelefone());
    }

    @Test
    public void testSetTelefoneMaiorQue11() {
        Fornecedor instance = new Fornecedor(10, 2, "12345678000195", "Calçados Brasil LTDA", "contato@calcadosbrasil.com", "61988887777", true);

        String numeroGrande = "2".repeat(12);

        assertThrows(IllegalArgumentException.class, () -> {
            instance.setTelefone(numeroGrande);
        });
    }

    @Test
    public void testSetTelefoneDentre11E12() {
        Fornecedor instance = new Fornecedor(10, 2, "12345678000195", "Calçados Brasil LTDA", "contato@calcadosbrasil.com", "61988887777", true);

        String numero = "1".repeat(11);

        instance.setTelefone(numero);

        assertEquals(numero, instance.getTelefone());
    }

    @Test
    public void testSetTelefoneFormatado() {
        Fornecedor instance = new Fornecedor(10, 2, "12345678000195", "Calçados Brasil LTDA", "contato@calcadosbrasil.com", "61988887777", true);

        instance.setTelefone("(61) 98888-7777");

        assertEquals("61988887777", instance.getTelefone());
    }

}
