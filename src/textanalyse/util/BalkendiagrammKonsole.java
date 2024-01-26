package textanalyse.util;

import java.util.List;
import java.util.Map;

public record BalkendiagrammKonsole(Map<String, Integer> werte) {
    private static final int BREITE = 30;

    public BalkendiagrammKonsole {
        if (werte.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    private int maximalerWert() {
        return werte.values().stream().mapToInt(i -> i).max().orElseThrow();
    }

    private int maximaleKeyLänge() {
        return werte.keySet().stream().mapToInt(String::length).max().orElseThrow();
    }

    @Override
    public String toString() {
        if (werte.isEmpty()) {
            return "";
        }

        StringBuilder ergebnis = new StringBuilder();

        int maximalerWert = maximalerWert();
        int maximaleWertLänge = Integer.toString(maximalerWert).length();
        int maximaleKeyLänge = maximaleKeyLänge();

        List<Map.Entry<String, Integer>> werteSortiert = werte.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed()).toList();

        for (Map.Entry<String, Integer> eintrag : werteSortiert) {
            if (eintrag.getValue() == 1) {
                continue;
            }
            int punkte = 1 + (int) (((float) eintrag.getValue() / (float) maximalerWert) * BREITE);
            ergebnis.append(TextUtil.verlängern(TextUtil.großschreiben(eintrag.getKey()), maximaleKeyLänge))
                    .append(" ".repeat(3))
                    .append(TextUtil.verlängern(Integer.toString(eintrag.getValue()), maximaleWertLänge))
                    .append(" ".repeat(3))
                    .append(Farbe.GELB)
                    .append("*".repeat(punkte))
                    .append(Farbe.STANDARD)
                    .append('\n');
        }
        return ergebnis.toString();
    }
}
