package textanalyse.analyse.rechtschreibung;

import textanalyse.util.Wort;
import textanalyse.util.Farbe;

/**
 * Ein gefunderer Rechtschreibfehler
 * @param wort das falsch geschriebene Wort
 * @param nachricht die Erklärung des Fehlers
 */
public record RechtschreibFehler(Wort wort, String nachricht) {
    /**
     * Konvertiert den Fehler zu einem String mit allen Informationen
     * @return ein String, der die Zeile des falsch geschriebenen Wortes,
     *      das Wort an sich, den Kontext, in dem es vorkommt, und die Erklärung des Fehlers enthält
     */
    @Override
    public String toString() {
        var nachrichtUmklammert = nachricht().isEmpty() ? "" : "("+Farbe.GELB+nachricht+Farbe.STANDARD+")";
        return "Zeile %s: %s %s".formatted(
                wort.zeile()+1,
                wort.kontext().replace(wort.inhalt(), Farbe.ROT + wort.inhalt().toUpperCase() + Farbe.STANDARD),
                nachrichtUmklammert
        );
    }
}
