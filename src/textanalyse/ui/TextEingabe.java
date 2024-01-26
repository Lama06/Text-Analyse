package textanalyse.ui;

import textanalyse.daten.TextAnalyse;

import javax.swing.*;

public final class TextEingabe {
    private final TextAnalyse daten;
    private final JTextPane widget = new JTextPane();

    public TextEingabe(TextAnalyse daten) {
        this.daten = daten;
        widget.setDocument(daten.getDokument());
        widget.setEditable(true);

    }

    public JTextPane getWidget() {
        return widget;
    }
}
