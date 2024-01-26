package textanalyse.analyse;

import textanalyse.daten.TextAnalyse;
import textanalyse.ui.TextAnalyseFenster;

import javax.swing.*;

public interface Analyse {
    String getKonsolenAusgabe();

    default void graphischAusgeben(TextAnalyseFenster fenster, TextAnalyse daten) {
        JOptionPane.showMessageDialog(fenster.getFenster(), getKonsolenAusgabe());
    }
}
