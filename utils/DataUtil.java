package utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DataUtil {

    private static final DateTimeFormatter FORMATADOR_BR = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    private static final DateTimeFormatter FORMATADOR_DATA_APENAS = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static String formatar(LocalDateTime dataHora) {
        if (dataHora == null) {
            return "";
        }
        return dataHora.format(FORMATADOR_BR);
    }

    public static String formatarData(LocalDateTime dataHora) {
        if (dataHora == null) {
            return "";
        }
        return dataHora.format(FORMATADOR_DATA_APENAS);
    }

    public static LocalDateTime parse(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return null;
        }
        try {
            return LocalDateTime.parse(texto, FORMATADOR_BR);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Data inválida. Utilize o formato dd/MM/yyyy HH:mm:ss");
        }
    }
}
