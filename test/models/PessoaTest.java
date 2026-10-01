package models;

import models.Pessoa;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PessoaTest {

    public PessoaTest() {
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

    public class PessoaImpl extends Pessoa {

        public PessoaImpl(int idPessoa, String nome, String cpf, String email, String telefone, boolean ativo) {
            super(idPessoa, nome, cpf, email, telefone, ativo);
        }

    }

    @Test
    public void testSetCpf() {
        Pessoa instance = new PessoaImpl(10, "João da Silva", "12345678901", "joao@email.com", "61988887777", true);

        instance.setCpf("12345674523");

        assertEquals("12345674523", instance.getCpf());
    }

    @Test
    public void testSetCpfNULL() {
        Pessoa instance = new PessoaImpl(10, "João da Silva", "12345678901", "joao@email.com", "61988887777", true);

        assertThrows(IllegalArgumentException.class, () -> {
            instance.setCpf(null);
        });
    }

    @Test
    public void testSetCpfDiferenteDe11() {
        Pessoa instance = new PessoaImpl(10, "João da Silva", "12345678901", "joao@email.com", "61988887777", true);

        assertThrows(IllegalArgumentException.class, () -> {
            instance.setCpf("1234567732");
        });
    }

    @Test
    public void testSetCpfFormatado() {
        Pessoa instance = new PessoaImpl(10, "João da Silva", "12345678901", "joao@email.com", "61988887777", true);

        instance.setCpf("123.456.789-01");

        assertEquals("12345678901", instance.getCpf());
    }

}
