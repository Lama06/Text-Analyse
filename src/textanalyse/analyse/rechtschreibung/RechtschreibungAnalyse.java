package textanalyse.analyse.rechtschreibung;

import textanalyse.analyse.Analyse;
import org.languagetool.JLanguageTool;
import org.languagetool.Languages;
import org.languagetool.rules.RuleMatch;
import textanalyse.daten.TextAnalyse;
import textanalyse.util.TextUtil;
import textanalyse.util.Wort;
import textanalyse.ui.TextAnalyseFenster;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Das Ergebnis der Rechtschreibprüfung
 * @param fehler die gefundenen Fehler
 */
public record RechtschreibungAnalyse(List<RechtschreibFehler> fehler) implements Analyse {
    /**
     * Findet Rechtschreibfehler, indem es für jedes Wort eine Anfrage an ein Online-Wörterbuch macht und den Status-Code
     * überprüft
     * @param text der zu prüfende Text
     * @return das Ergebnis der Rechtschreibprüfung
     */
    public static RechtschreibungAnalyse fehlerFinden1(String text) {
        var fehler = new ArrayList<RechtschreibFehler>();
        var wiktionary = new Wiktionary();
        var wörter = TextUtil.wörterExtrahieren(text, 4);
        for (var wort : wörter) {
            boolean richtig;
            try {
                richtig = wiktionary.istRichtigGeschrieben(wort.inhalt());
            } catch (IOException e) {
                return new RechtschreibungAnalyse(null);
            }
            if (richtig) {
                continue;
            }
            fehler.add(new RechtschreibFehler(wort, ""));
        }
        return new RechtschreibungAnalyse(fehler);
    }

    /**
     * Findet Rechtschreibfehler mithilfe einer Bibliothek
     * @param text der zu prüfende Text
     * @return das Ergebnis der Rechtschreibprüfung
     */
    public static RechtschreibungAnalyse fehlerFinden2(String text) {
        var fehler = new ArrayList<RechtschreibFehler>();

        var languageTool = new JLanguageTool(Languages.getLanguageForShortCode("de-DE"));

        // Fehler finden
        List<RuleMatch> matches;
        try {
            matches = languageTool.check(text);
        } catch (IOException e) {
            return new RechtschreibungAnalyse(null);
        }

        // Fehler in Liste speichern
        for (var match : matches) {
            var falschesWort = text.substring(match.getFromPos(), match.getToPos());
            var satz = match.getSentence().getText().replace("\r\n", "");
            fehler.add(new RechtschreibFehler(new Wort(falschesWort, match.getLine(), match.getFromPosSentence(), satz), match.getMessage()));
        }

        return new RechtschreibungAnalyse(fehler);
    }

    /**
     * @return ein mehrzeiliger String, der alle gefundenen Fehler auflistet
     */
    @Override
    public String getKonsolenAusgabe() {
        if (fehler == null) {
            return "Fehler beim Überprüfen der Rechtschreibung";
        }
        if (fehler.isEmpty()) {
            return "Keinen Fehler gefunden";
        }
        var ergebnis = new StringBuilder();
        for (RechtschreibFehler einFehler : fehler) {
            ergebnis.append(einFehler.toString()).append("\n");
        }
        return ergebnis.toString();
    }

    /**
     * Markiert alle gefundenen Fehler im Dokumt.
     * Falls fenster nicht null ist, wird zusätzlich ein Dialog mit einer Liste aller Fehler und deren Erklärungen geöffnet.
     * @param fenster das dem zu öffnenden Dialog übergerodnete Fenster oder null, falls kein Dialog geöffnet werden soll
     * @param daten die Daten des Programmes
     */
    @Override
    public void graphischAusgeben(TextAnalyseFenster fenster, TextAnalyse daten) {
        if (fehler == null) {
            JOptionPane.showMessageDialog(fenster.getFenster(), "Fehler beim Prüfen der Rechtschreibung", "Fehler", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Wörter im Text markieren
        for (RechtschreibFehler einFehler : fehler) {
            var zeile = daten.getDokument().getDefaultRootElement().getElement(einFehler.wort().zeile());
            daten.getDokument().setCharacterAttributes(
                    zeile.getStartOffset()+einFehler.wort().anfang(),
                    einFehler.wort().inhalt().length(),
                    daten.getDokument().getStyle("Markierung"),
                    true
            );
        }

        // Dialog öffnen
        if (fehler.isEmpty() || fenster == null) {
            return;
        }
        JOptionPane.showMessageDialog(fenster.getFenster(), getKonsolenAusgabe(), "Ergebnis der Rechtschreibprüfung", JOptionPane.WARNING_MESSAGE);
    }
}
