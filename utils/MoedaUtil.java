package utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.Locale;

public class MoedaUtil {

    private static final Locale LOCALE_BR = new Locale("pt", "BR");
    private static final DecimalFormat FORMATADOR_MOEDA;

    static {
        DecimalFormatSymbols simbolos = new DecimalFormatSymbols(LOCALE_BR);
        simbolos.setCurrencySymbol("R$");
        simbolos.setMonetaryDecimalSeparator(',');
        simbolos.setGroupingSeparator('.');

        FORMATADOR_MOEDA = new DecimalFormat("R$ #,##0.00", simbolos);
    }

    public static String formatar(double valor) {
        return FORMATADOR_MOEDA.format(valor);
    }

    public static double parse(String textoMoeda) {
        if (textoMoeda == null || textoMoeda.trim().isEmpty()) {
            return 0.0;
        }
        try {
            // Remove o prefixo "R$", espaços e ajusta para o parse correto
            String limpo = textoMoeda.replace("R$", "").trim();
            Number numero = FORMATADOR_MOEDA.parse(limpo);
            return numero.doubleValue();
        } catch (ParseException e) {
            throw new IllegalArgumentException("Valor monetário inválido. Utilize o formato R$ 0,00");
        }
    }
}
