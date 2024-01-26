package textanalyse.analyse.suche;

import textanalyse.analyse.Analyse;
import textanalyse.daten.TextAnalyse;
import textanalyse.util.TextUtil;
import textanalyse.util.Wort;
import textanalyse.util.Farbe;
import textanalyse.ui.TextAnalyseFenster;

import javax.swing.text.Element;
import java.util.ArrayList;
import java.util.List;

public record SuchenAnalyse(List<SuchErgebnis> ergebnisse) implements Analyse {
    public static SuchenAnalyse wortSuchen(String text, String suche) {
        List<SuchErgebnis> ergebnisse = new ArrayList<>();
        List<Wort> wörter = TextUtil.wörterExtrahieren(text, 15);
        for (Wort wort : wörter) {
            if (!wort.inhalt().equalsIgnoreCase(suche)) {
                continue;
            }
            ergebnisse.add(new SuchErgebnis(wort));
        }
        return new SuchenAnalyse(ergebnisse);
    }

    @Override
    public String getKonsolenAusgabe() {
        if (ergebnisse.isEmpty()) {
            return Farbe.ROT + "Nichts gefunden!" + Farbe.STANDARD;
        }

        StringBuilder text = new StringBuilder();
        for (SuchErgebnis ergebnis : ergebnisse) {
            text.append(ergebnis.toString()).append("\n");
        }
        return text.toString();
    }

    @Override
    public void graphischAusgeben(TextAnalyseFenster fenster, TextAnalyse daten) {
        for (SuchErgebnis ergebnis : ergebnisse) {
            Element zeile = daten.getDokument().getDefaultRootElement().getElement(ergebnis.wort().zeile());
            daten.getDokument().setCharacterAttributes(zeile.getStartOffset()+ergebnis.wort().anfang(),
                    ergebnis.wort().inhalt().length(),
                    daten.getDokument().getStyle("Markierung"),
                    true);
        }
    }
}
