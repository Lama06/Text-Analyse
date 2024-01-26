package textanalyse.analyse.anapher;

import textanalyse.analyse.Analyse;
import textanalyse.daten.TextAnalyse;
import textanalyse.ui.TextAnalyseFenster;

import javax.swing.text.Element;
import java.util.ArrayList;
import java.util.List;

public record AnaphernAnalyse(List<Anapher> anaphern) implements Analyse {
    public static AnaphernAnalyse anaphernFinden(String text) {
        List<Anapher> anaphern = new ArrayList<>();
        String[] zeilen = text.split("\r\n");
        for (int i = 1; i < zeilen.length; i++) {
            if (zeilen[i].charAt(0) == zeilen[i-1].charAt(0)) {
                int gleicherAnfang = 0;
                while (zeilen[i].charAt(gleicherAnfang) == zeilen[i-1].charAt(gleicherAnfang)) {
                    gleicherAnfang++;
                }
                anaphern.add(new Anapher(i-1, gleicherAnfang, zeilen[i-1], zeilen[i]));
            }
        }
        return new AnaphernAnalyse(anaphern);
    }

    @Override
    public String getKonsolenAusgabe() {
        if (anaphern.isEmpty()) {
            return "Es gibt keine Anaphern.";
        }
        StringBuilder ergebnis = new StringBuilder().append("Der Text enthält ").append(anaphern.size()).append(" Anaphern:\n");
        for (Anapher anapher : anaphern) {
            ergebnis.append(anapher).append("\n");
        }
        return ergebnis.toString();
    }

    @Override
    public void graphischAusgeben(TextAnalyseFenster fenster, TextAnalyse daten) {
        for (Anapher anapher : anaphern) {
            Element zeile = daten.getDokument().getDefaultRootElement().getElement(anapher.zeile());
            Element zeile2 = daten.getDokument().getDefaultRootElement().getElement(anapher.zeile()+1);
            daten.getDokument().setCharacterAttributes(zeile.getStartOffset(),
                    anapher.gleicherAnfang(),
                    daten.getDokument().getStyle("Markierung"),
                    true);
            daten.getDokument().setCharacterAttributes(zeile2.getStartOffset(),
                    anapher.gleicherAnfang(),
                    daten.getDokument().getStyle("Markierung"),
                    true);
        }
    }
}
