package textanalyse.analyse;

import textanalyse.daten.TextAnalyse;
import textanalyse.ui.Balkendiagramm;
import textanalyse.util.TextUtil;
import textanalyse.util.Wort;
import textanalyse.util.BalkendiagrammKonsole;
import textanalyse.ui.TextAnalyseFenster;

import javax.swing.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record WörterHäufigkeitsAnalyse(Map<String, Integer> wörter) implements Analyse {
    public static WörterHäufigkeitsAnalyse wörterZählen(String text) {
        List<Wort> wörter = TextUtil.wörterExtrahieren(text, 0);
        Map<String, Integer> häufigkeit = new HashMap<>();
        for (Wort wort : wörter) {
            häufigkeit.put(wort.inhalt().toLowerCase(), häufigkeit.getOrDefault(wort.inhalt().toLowerCase(), 0)+1);
        }
        return new WörterHäufigkeitsAnalyse(häufigkeit);
    }

    @Override
    public String getKonsolenAusgabe() {
        return new BalkendiagrammKonsole(wörter).toString();
    }

    @Override
    public void graphischAusgeben(TextAnalyseFenster fenster, TextAnalyse daten) {
        JDialog dialog = new JDialog(fenster.getFenster());
        dialog.setContentPane(new Balkendiagramm(wörter, fenster, daten));
        dialog.setTitle("Wörter");
        dialog.pack();
        dialog.setVisible(true);
    }
}
