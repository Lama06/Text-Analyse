package textanalyse.analyse.suche;

import textanalyse.util.Wort;
import textanalyse.util.Farbe;

public record SuchErgebnis(Wort wort) {
    @Override
    public String toString() {
        return "Zeile %s: %s".formatted(wort.zeile(), wort.kontext().replace(wort.inhalt(), Farbe.ROT + wort.inhalt().toUpperCase() + Farbe.STANDARD));
    }
}