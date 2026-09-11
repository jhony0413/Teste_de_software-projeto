package controllers;

import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Tela {

    private static final Scanner ler = new Scanner(System.in);
    private static final DateTimeFormatter formatadorData = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /* Ler linha de texto */
    public static String lerString() {
        return ler.nextLine().trim();
    }

    /* Ler número natural */
    public static int lerInt() {
        while (true) {
            try {
                int entrada = Integer.parseInt(ler.nextLine().trim());
                return entrada;
            } catch (NumberFormatException e) {
                imprimirString("Digite um número inteiro válido.");
            }
        }
    }

    /* Ler número real */
    public static double lerDouble() {
        while (true) {
            try {
                // Suporta ponto ou vírgula trocando vírgula por ponto
                String texto = ler.nextLine().trim().replace(',', '.');
                return Double.parseDouble(texto);
            } catch (NumberFormatException e) {
                imprimirString("Digite um número decimal válido.");
            }
        }
    }

    /* Ler data */
    public static LocalDate lerLocalDate() {
        while (true) {
            try {
                return LocalDate.parse(ler.nextLine().trim(), formatadorData);
            } catch (DateTimeParseException e) {
                imprimirString("Data inválida. Use o formato dd/MM/yyyy.");
            }
        }
    }

    /* Ler resposta de sim ou não */
    public static boolean lerBoolean() {
        while (true) {
            String entrada = ler.nextLine().trim().toLowerCase();
            switch (entrada) {
                case "sim", "s", "1" -> {
                    return true;
                }
                case "não", "nao", "n", "0" -> {
                    return false;
                }
                default -> {
                    imprimirString("Resposta inválida. Digite (1/sim/s) ou (0/não/n).");
                }
            }
        }
    }

    /* Imprimir linha de texto */
    public static void imprimirString(String saida) {
        System.out.printf(saida);
    }
}
