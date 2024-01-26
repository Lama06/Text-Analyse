package textanalyse.analyse;

import textanalyse.daten.TextAnalyse;
import textanalyse.ui.Balkendiagramm;
import textanalyse.util.BalkendiagrammKonsole;
import textanalyse.ui.TextAnalyseFenster;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public record BuchstabenHäufigkeitAnalyse(Map<Character, Integer> buchstaben) implements Analyse {
    public static BuchstabenHäufigkeitAnalyse buchstabenZählen(String text) {
        Map<Character, Integer> buchstaben = new HashMap<>();
        for (int i = 0; i < text.length(); i++) {
            char buchstabe = Character.toLowerCase(text.charAt(i));
            if (!Character.isLetterOrDigit(buchstabe)) {
                continue;
            }
            buchstaben.put(buchstabe, buchstaben.getOrDefault(buchstabe, 0)+1);
        }
        return new BuchstabenHäufigkeitAnalyse(buchstaben);
    }

    @Override
    public String getKonsolenAusgabe() {
        Map<String, Integer> buchstabenHäufigkeit = new HashMap<>();
        for (Map.Entry<Character, Integer> eintrag : buchstaben.entrySet()) {
            buchstabenHäufigkeit.put(Character.toString(eintrag.getKey()), eintrag.getValue());
        }
        BalkendiagrammKonsole buchstabenDiagramm = new BalkendiagrammKonsole(buchstabenHäufigkeit);
        return buchstabenDiagramm.toString();
    }

    @Override
    public void graphischAusgeben(TextAnalyseFenster fenster, TextAnalyse daten) {
        Map<String, Integer> buchstabenHäufigkeit = new HashMap<>();
        for (Map.Entry<Character, Integer> eintrag : buchstaben.entrySet()) {
            buchstabenHäufigkeit.put(Character.toString(eintrag.getKey()), eintrag.getValue());
        }

        JDialog dialog = new JDialog(fenster.getFenster());
        dialog.setTitle("Buchstaben");
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(new Balkendiagramm(buchstabenHäufigkeit, fenster, daten), BorderLayout.CENTER);
        dialog.setContentPane(panel);
        dialog.pack();
        dialog.setVisible(true);
    }
}
