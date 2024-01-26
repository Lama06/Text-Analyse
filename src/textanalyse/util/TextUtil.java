package textanalyse.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public final class TextUtil {
    private static final Set<String> SATZZEICHEN = Set.of(".", "!", "?", ",");

    public static List<Wort> wörterExtrahieren(String text, int kontextBereich) {
        List<Wort> ergebnis = new ArrayList<>();
        String[] zeilen = text.lines().toArray(String[]::new);
        for (int zeileIndex = 0; zeileIndex < zeilen.length; zeileIndex++) {
            String zeile = zeilen[zeileIndex];
            StringBuilder wort = new StringBuilder();
            for (int i = 0; i < zeile.length(); i++) {
                char buchstabe = zeile.charAt(i);
                if (buchstabe == ' ' || buchstabe == ',' || buchstabe == '.') {
                    if (wort.isEmpty()) {
                        continue;
                    }
                    String kontext = zeile.substring(Math.max(0, i-kontextBereich), Math.min(zeile.length(), i+kontextBereich));
                    ergebnis.add(new Wort(wort.toString(), zeileIndex, i-wort.length(), kontext));
                    wort.setLength(0);
                    continue;
                }
                wort.append(buchstabe);
            }
            if (!wort.isEmpty()) {
                String kontext = zeile.substring(Math.max(0, zeile.length()-kontextBereich));
                ergebnis.add(new Wort(wort.toString(), zeileIndex, zeile.length()-wort.length(), kontext));
            }
        }
        return ergebnis;
    }

    public static String verlängern(String text, int länge) {
        if (text.length() >= länge) {
            return text;
        }
        StringBuilder ergebnis = new StringBuilder(text);
        while (ergebnis.length() < länge) {
            ergebnis.append(' ');
        }
        return ergebnis.toString();
    }

    public static String großschreiben(String text) {
        if (text.isEmpty()) {
            return text;
        }
        StringBuilder ergebnis = new StringBuilder();
        ergebnis.append(Character.toUpperCase(text.charAt(0)));
        for (int i = 1; i < text.length(); i++) {
            ergebnis.append(text.charAt(i));
        }
        return ergebnis.toString();
    }
}
