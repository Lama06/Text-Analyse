package textanalyse.analyse.anapher;

import textanalyse.util.Farbe;

public record Anapher(int zeile, int gleicherAnfang, String zeile1, String zeile2) {
    @Override
    public String toString() {
        String gleicherAnfang = zeile1.substring(0, gleicherAnfang());
        String gleicherAnfangFarbig = Farbe.ROT + gleicherAnfang + Farbe.STANDARD;
        return "Satz %s:\n%s\n%s".formatted(zeile + 1,
                gleicherAnfangFarbig + zeile1.substring(gleicherAnfang.length()),
                gleicherAnfangFarbig + zeile2.substring(gleicherAnfang.length()));
    }
}
